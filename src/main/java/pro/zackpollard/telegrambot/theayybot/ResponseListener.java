package pro.zackpollard.telegrambot.theayybot;

import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;

/**
 * @author Zack Pollard
 */
public class ResponseListener implements GSpeechResponseListener {

    private final TelegramBot telegramBot;
    private final Chat chat;

    public ResponseListener(TelegramBot telegramBot, Chat chat) {

        this.telegramBot = telegramBot;
        this.chat = chat;
    }

    public void onResponse(GoogleResponse googleResponse) {

        telegramBot.sendMessage(chat, SendableTextMessage.builder().message("Speech -> Text Conversion: " + googleResponse.getResponse()).build());
        System.out.println("(" + chat.getId() + " - " + chat.getName() + ")" + " - Voice Message - " + googleResponse.getResponse());
    }
}