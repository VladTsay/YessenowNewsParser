package org.example.Parsers;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParsingManager {
    private final ExecutorService executor;
    private final Map<SiteParser, String> parserUrls;

    public ParsingManager(Map<SiteParser, String> parserUrls) {
        this.executor = Executors.newFixedThreadPool(parserUrls.size());
        this.parserUrls = parserUrls;
    }

    public void startParsing() {
        for (Map.Entry<SiteParser, String> entry : parserUrls.entrySet()) {
            SiteParser parser = entry.getKey();
            String url = entry.getValue();
            executor.submit(() -> parser.parseAndNotify(url));
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}

