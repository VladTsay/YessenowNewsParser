package org.example;
import org.example.Parsers.KaspiParser;
import org.example.Parsers.OlxParser;
import org.example.Parsers.ParsingManager;
import org.example.Parsers.SiteParser;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            BotInstance bot = new BotInstance();
            botsApi.registerBot(bot);

            Map<SiteParser, String> parserUrls = Map.of(
                    new OlxParser(), "https://www.olx.kz/elektronika/telefony-i-aksesuary/man/?search%5Border%5D=created_at:desc"
                    ,new OlxParser(), "https://www.olx.kz/elektronika/kompyutery-i-komplektuyuschie/man/?search%5Border%5D=created_at:desc&view=list"
                    ,new OlxParser(), "https://www.olx.kz/elektronika/noutbuki-i-aksesuary/man/?search%5Border%5D=created_at:desc&view=list"
                    ,               new KaspiParser(), "https://obyavleniya.kaspi.kz/aktau/elektronika/computery/?sortBy%5Bdate%5D=desc"
                    ,new KaspiParser(), "https://obyavleniya.kaspi.kz/aktau/elektronika/telefony/?sortBy%5Bdate%5D=desc"
            );

            ParsingManager parsingManager = new ParsingManager(parserUrls);

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
            scheduler.scheduleAtFixedRate(parsingManager::startParsing, 0, 1, TimeUnit.MINUTES);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                parsingManager.shutdown();
                System.out.println("Программа завершена.");
            }));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

