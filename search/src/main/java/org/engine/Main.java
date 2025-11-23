package org.engine;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Reader reader = new Reader();
        reader.readDocs("search/SWBooks");

        HashMap<String, String> docs = reader.getDocs();
        SearchEngine engine = new SearchEngine(docs);

    }
}