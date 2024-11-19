package org.example.Parsers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.example.DatabaseStorage;
import org.example.BotInstance;

public class KaspiParser implements SiteParser{

    public void parseAndNotify(String url) {
        try {
            System.out.println("Начало сканирования страницы: " + url);
            Document doc = Jsoup.connect(url).get();

            // Находим все карточки товара
            Elements cards = doc.select("div[data-test-id='listing-item']");

            // Проверка, если карточки есть
            if (cards.isEmpty()) {
                System.out.println("Карточки не найдены на странице.");
                return;
            }

            // Проходим по всем карточкам
            for (Element card : cards) {
                // Извлекаем название товара
                Element titleElement = card.selectFirst("h4[data-test-id='listing-item-title']");
                String title = titleElement != null ? titleElement.text() : "Название не найдено";

                // Извлекаем цену товара
                Element priceElement = card.selectFirst("span[data-test-id='listing-item-price']");
                String price = priceElement != null ? priceElement.text() : "Цена не найдена";

                // Извлекаем ссылку на товар
                String link = card.selectFirst("a").attr("href");
                String fullLink = "obyavleniya.kaspi.kz" + link; // Если ссылка относительная, добавляем к базовому URL

                String productId = link;

                if (!DatabaseStorage.isProductSent(productId)) {
                    // Формирование сообщения
                    String message = String.format(
                            "Новое объявление:\nНазвание: %s\nЦена: %s\nСсылка: %s",
                            title, price, fullLink
                    );

                    // Отправка сообщения всем подписчикам
                    for (Long chatId : DatabaseStorage.getAllChatIds()) {
                        new BotInstance().sendTextMessage(chatId, message);
                    }

                    System.out.println("Kaspi :"+productId);
                    DatabaseStorage.saveProduct(productId);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
