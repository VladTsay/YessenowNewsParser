package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotInstance extends TelegramLongPollingBot {
    private static final String BOT_USERNAME = "YessenowAdParserBot";
    private static final String BOT_TOKEN = "7532842142:AAE8QsLDrMZ4EatjLBABZ6xRx0b8EmZ_D9k";

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (messageText.equalsIgnoreCase("/start")) {
                DatabaseStorage.addChatId(chatId);
                sendTextMessage(chatId, "Вы подписаны на уведомления о новых товарах!");
            } else {
                sendTextMessage(chatId, "Неизвестная команда. Попробуйте /start.");
            }
        }
    }

    public void sendTextMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

