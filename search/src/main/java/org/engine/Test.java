package org.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public record Test(InvertedSearchEngine engine) {
    private void runAndPrintQuery(String query) {
        System.out.printf("Query = %s\n%s\n\n", query, engine.search(query));
    }

    private void samplesTest() {
        ArrayList<String> queries = new ArrayList<>(List.of(
                "+builder",
                "meetings +automation +notincludedword -email"
        ));

        queries.forEach(this::runAndPrintQuery);
    }

    private void runQueryLoop() {
        try (Scanner in = new Scanner(System.in)) {
            String query;
            do {
                System.out.println("Enter query to search:");
                query = in.nextLine();
                System.out.println(engine.search(query));
            } while (in.hasNext());
        }
    }

    public void runSamplesThenLoop() {
        samplesTest();
        runQueryLoop();
    }
}