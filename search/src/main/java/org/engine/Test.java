package org.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public record Test(InvertedSearchEngine engine) {
    private void runAndPrintQuery(String query) {
        System.out.printf("Query = %s\n", query);
        System.out.println(engine.search(query));
        System.out.println();
    }

    private void samplesTest() {
        ArrayList<String> queries = new ArrayList<>(List.of(
                "+builder",
                "meetings +automation +notincludedword -email"));

        queries.forEach(this::runAndPrintQuery);
    }

    private void runQueryLoop() {
        try (Scanner in = new Scanner(System.in)) {
            String query;
            do {
                query = in.nextLine();
                System.out.println("SEARCH:");
                System.out.println(engine.search(query));
            } while (in.hasNext());
        }
    }

    public void runSamplesThenLoop() {
        samplesTest();
        runQueryLoop();
    }
}