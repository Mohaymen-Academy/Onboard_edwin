package org.engine;

import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Reader reader = new Reader();
        reader.readDocs("search/src/main/resources/SWBooks");
        // TODO: use best practices to read under resources (resource stream?)
        
        HashMap<String, String> docs = reader.getDocs();
        SearchEngine engine = new SearchEngine(docs);

        try (Scanner in = new Scanner(System.in)) {
            do {
                System.out.println("SEARCH:");
                System.out.println(engine.search(in.next()));
            }
            while (in.hasNext());
        }
    }
}
