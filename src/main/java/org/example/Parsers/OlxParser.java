package org.example.Parsers;

import org.example.BotInstance;
import org.example.DatabaseStorage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OlxParser implements SiteParser {

    @Override
    public void parseAndNotify(String url) {
        try {
            System.out.println("Начало сканирования страницы: " + url);
            Document doc = Jsoup.connect(url).get();
            Elements cards = doc.select("div[data-cy='ad-card-title']");

            for (Element card : cards) {
                String link = card.selectFirst("a.css-qo0cxu").attr("href");
                String title = card.selectFirst("h4.css-1s3qyje").text();
                String price = card.selectFirst("p[data-testid='ad-price']").text();
                String productId = link;

                if (!DatabaseStorage.isProductSent(productId)) {
                    String message = String.format(
                            "Новое объявление:\nНазвание: %s\nЦена: %s\nСсылка: %s%s",
                            title, price, "olx.kz", link
                    );

                    for (Long chatId : DatabaseStorage.getAllChatIds()) {
                        new BotInstance().sendTextMessage(chatId, message);
                    }
                    System.out.println("Olx :" +productId);
                    DatabaseStorage.saveProduct(productId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
