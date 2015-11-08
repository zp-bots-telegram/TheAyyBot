package pro.zackpollard.telegrambot.theayybot;

import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;

/**
 * @author Zack Pollard
 */
public class ResponseListener implements GSpeechResponseListener {

    private final TelegramBot telegramBot;
    private final String chatID;

    public ResponseListener(TelegramBot telegramBot, String chatID) {

        this.telegramBot = telegramBot;
        this.chatID = chatID;
    }

    public void onResponse(GoogleResponse googleResponse) {

        telegramBot.sendMessage(TelegramBot.getChat(chatID), SendableTextMessage.builder().message("Speech -> Text Conversion: " + googleResponse.getResponse()).build());
    }
}