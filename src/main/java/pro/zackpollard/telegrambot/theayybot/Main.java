package pro.zackpollard.telegrambot.theayybot;

import com.darkprograms.speech.recognizer.RecognizerChunked;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.message.send.*;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.message.*;
import pro.zackpollard.telegrambot.api.keyboards.KeyboardButton;
import pro.zackpollard.telegrambot.api.keyboards.ReplyKeyboardMarkup;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zack Pollard
 */
public class Main implements Listener {

    private final TelegramBot telegramBot;
    private final String GOOGLE_API_KEY;
    public static String YANDEX_API_KEY;
    private static final Pattern ripRegex = Pattern.compile("\\br+(?<i>i+)(?<p>p+)(?:erino)?\\b");  // Pattern.CASE_INSENSITIVE unnecessary because we're matching against lowercaseContent

    public Main(String[] args) {

        if (args.length >= 2) {

            this.telegramBot = TelegramBot.login(args[0]);
            this.GOOGLE_API_KEY = args[1];
            YANDEX_API_KEY = args[2];

            telegramBot.getEventsManager().register(this);

            telegramBot.startUpdates(false);

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        telegramBot = null;
        this.GOOGLE_API_KEY = null;

        System.exit(-1);
    }

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {

        if (event.getCommand().equalsIgnoreCase("choice")) {

            String[] args = event.getArgsString().split(",");

            if (args.length >= 2) {
                telegramBot.sendMessage(event.getChat(), SendableTextMessage.builder().message("I say " + args[new Random().nextInt(args.length)].trim()).build());
            } else {

                telegramBot.sendMessage(event.getChat(), SendableTextMessage.builder().message("Please send two or more arguments seperated by commas.").build());
            }
        } else if (event.getCommand().equalsIgnoreCase("ayyorlmao")) {

            telegramBot.sendMessage(event.getMessage().getChat(), SendableTextMessage.builder().message("Ayy or Lmao, that is the question...").replyTo(event.getMessage()).replyMarkup(ReplyKeyboardMarkup.builder().selective(true).oneTime(true).addRow(KeyboardButton.builder().text("Ayy").build(), KeyboardButton.builder().text("Lmao").build()).resize(true).build()).build());
        /**} else if (event.getCommand().equalsIgnoreCase("geturl")) {

            if (event.getArgs().length != 0) {

                try {
                    event.getChat().sendMessage(SendableDocumentMessage.builder().document(new InputFile(new URL(event.getArgsString()))).build(), telegramBot);
                } catch (MalformedURLException e) {
                    event.getChat().sendMessage("URL was malformed, correct the URL and try again.", telegramBot);
                }
            } else {

                event.getChat().sendMessage("No URL Specified.", telegramBot);
            }**/
        } else if (event.getCommand().equalsIgnoreCase("translate")) {

            String textLanguage = Translation.detectLanguage(event.getArgsString());
            if (textLanguage != null && !textLanguage.equals("en")) {

                String translatedText = Translation.translateText(event.getArgsString(), "en");

                if (translatedText != null && !event.getArgsString().equals(translatedText)) {

                    Locale locale = Locale.forLanguageTag(textLanguage);
                    event.getChat().sendMessage("Translation from " + locale.getDisplayName() + ": " + translatedText);
                }
            }
        } else if(event.getCommand().toLowerCase().startsWith("r/")) {

            String lowerCaseCommand = event.getCommand().toLowerCase();

            int location = lowerCaseCommand.indexOf("r/");
            String beginning = lowerCaseCommand.substring(location);
            int end = beginning.indexOf(' ');
            if(end <= 2) {
                end = beginning.length();
            }
            String redditURL = beginning.substring(0, end);

            telegramBot.sendMessage(event.getMessage().getChat(), SendableTextMessage.builder().message("[For u bby /" + redditURL + "](http://reddit.com/" + redditURL + ")").parseMode(ParseMode.MARKDOWN).build());
        }
    }

    @Override
    public void onVoiceMessageReceived(VoiceMessageReceivedEvent event) {

        event.getContent().getContent().downloadFile(telegramBot, new File("./latestVoice.oga"));

        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec("ffmpeg -i latestVoice.oga current.flac -y");
            pr.waitFor();

            RecognizerChunked recognizer = new RecognizerChunked(GOOGLE_API_KEY);
            recognizer.addResponseListener(new ResponseListener(telegramBot, event.getChat()));
            recognizer.getRecognizedDataForFlac(new File("current.flac"), 48000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTextMessageReceived(TextMessageReceivedEvent event) {

        String lowercaseContent = event.getContent().getContent().toLowerCase();

        System.out.println("(" + event.getChat().getId() + " - " + event.getChat().getName() + ")" + " - (" + event.getMessage().getSender().getFullName() + " - " + event.getMessage().getSender().getUsername() + ") --- " + lowercaseContent);
        
        Matcher ripMatcher = ripRegex.matcher(lowercaseContent);

        if (event.getMessage().getSender().getId() == 55395012 || event.getMessage().getSender().getId() == 91845503) {

            if (lowercaseContent.contains("nuke") || lowercaseContent.contains("bomb")) {

                telegramBot.sendMessage(event.getChat(), SendableTextMessage.builder().message("Reported to NSA.").replyTo(event.getMessage()).build());
            }
        }

        if(lowercaseContent.contains(" /r/")) {

            int location = lowercaseContent.indexOf("/r/");
            String beginning = lowercaseContent.substring(location);
            int end = beginning.indexOf(' ');
            if(end <= 2) {
                end = beginning.length();
            }

            String redditURL = beginning.substring(0, end);

            telegramBot.sendMessage(event.getMessage().getChat(), SendableTextMessage.builder().message("[For u bby " + redditURL + "](http://reddit.com" + redditURL + ")").parseMode(ParseMode.MARKDOWN).build());
        } else if (lowercaseContent.endsWith(" is love") && !lowercaseContent.equals(" is love")) {

            telegramBot.sendMessage(event.getMessage().getChat(), SendableTextMessage.builder().message(event.getContent().getContent().substring(0, event.getContent().getContent().length() - 7) + "is life").build());
        } else if (lowercaseContent.endsWith(" are love") && !lowercaseContent.equals(" are love")) {

            telegramBot.sendMessage(event.getMessage().getChat(), SendableTextMessage.builder().message(event.getContent().getContent().substring(0, event.getContent().getContent().length() - 7) + "are life").build());
        } else if (ripMatcher.matches()) {
            String response = String.format("%sn %sieces", ripMatcher.group("i"), ripMatcher.group("p"));

            telegramBot.sendMessage(event.getMessage().getChat(), SendableTextMessage.builder().message(response).build());
        } else if (lowercaseContent.contains("ayy") && lowercaseContent.contains("lmao") || lowercaseContent.contains("alien")) {

            telegramBot.sendMessage(event.getMessage().getChat(), SendableTextMessage.builder().message("ayy lmao").build());
        } else if (lowercaseContent.contains("ayy")) {

            String trimmedString = lowercaseContent.substring(lowercaseContent.indexOf("ayy") + 3).trim();

            if (trimmedString.contains(" ")) {

                trimmedString = trimmedString.substring(0, trimmedString.indexOf(" "));
            }

            int trailingYs = trailingCharactersCount(trimmedString, 'y');

            String additionalOs = "";

            for (int i = 0; i < trailingYs; ++i) {

                additionalOs += "o";
            }

            telegramBot.sendMessage(event.getMessage().getChat(), SendableTextMessage.builder().message("lmao" + additionalOs).build());
        } else if (lowercaseContent.contains("lmao")) {

            String trimmedString = lowercaseContent.substring(lowercaseContent.indexOf("lmao") + 4).trim();

            if (trimmedString.contains(" ")) {

                trimmedString = trimmedString.substring(0, trimmedString.indexOf(" "));
            }

            int trailingOs = trailingCharactersCount(trimmedString, 'o');

            String additionalYs = "";

            for (int i = 0; i < trailingOs; ++i) {

                additionalYs += "y";
            }

            telegramBot.sendMessage(event.getMessage().getChat(), SendableTextMessage.builder().message("ayy" + additionalYs).build());
        } else if(lowercaseContent.contains("intense")) {

            System.out.print("intensity detected...");
            try {
                telegramBot.sendMessage(event.getMessage().getChat(), SendableVideoMessage.builder().video(new InputFile(new URL("http://www.zackpollard.pro/at-file-uploads/TADAM TAM TATAM.mp4"))).caption("It's getting intense up in here...").build());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private int trailingCharactersCount(String string, char trail) {

        int count = 0;

        for (char character : string.toCharArray()) {

            if (character == trail) {

                ++count;
            } else {

                return count;
            }
        }

        return count;
    }

    @Override
    public void onStickerMessageReceived(StickerMessageReceivedEvent event) {

        if (event.getContent().getContent().getFileId().equals("BQADBAADTQADzJm8AsWHAqcGmP2hAg")) {

            telegramBot.sendMessage(event.getMessage().getChat(), SendableStickerMessage.builder().sticker(new InputFile("BQADBAADVAADzJm8AlQq_rm3coV0Ag")).build());
        } else if (event.getContent().getContent().getFileId().equals("BQADBAADVAADzJm8AlQq_rm3coV0Ag")) {

            telegramBot.sendMessage(event.getMessage().getChat(), SendableStickerMessage.builder().sticker(new InputFile("BQADBAADTQADzJm8AsWHAqcGmP2hAg")).build());
        }
    }

    public static void main(String[] args) {

        new Main(args);
    }
}
