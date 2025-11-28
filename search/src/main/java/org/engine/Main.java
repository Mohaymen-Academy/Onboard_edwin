package org.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

class Test {
    private final InvertedSearchEngine engine;

    public Test(InvertedSearchEngine engine) {
        this.engine = engine;
    }

    void runAndPrintQuery(String query) {
        System.out.printf("Query = %s\n", query);
        System.out.println(engine.search(query));
        System.out.println();
    }

    void samplesTest() {
        ArrayList<String> queries = new ArrayList<>(List.of(
                "+builder",
                "meetings +automation +notincludedword -email"));

        queries.forEach(this::runAndPrintQuery);
    }
}

public class Main {
    public static final boolean DEBUG = true;

    public static void main(String[] args) {
        Reader reader = new Reader();
        reader.readDocs("search/src/main/resources/SWBooks");
        // TODO: use best practices to read under resources (resource stream?)

        HashMap<String, String> docs = reader.getDocs();
        InvertedSearchEngine engine = new InvertedSearchEngine(
                docs, s -> s
                .strip()
                .replaceAll("'|\"", "")
                .toLowerCase()
        );

        new Test(engine).samplesTest();

        try (Scanner in = new Scanner(System.in)) {
            String query;
            do {
                query = in.nextLine();
                System.out.println("SEARCH:");
                System.out.println(engine.search(query));
            } while (in.hasNext());
        }
    }

}
