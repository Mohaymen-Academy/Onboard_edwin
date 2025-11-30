package org.search.app;

import org.search.engine.impl.InvertedSearchEngine;
import org.search.io.Reader;

import java.util.HashMap;

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

        new Test(engine).runSamplesThenLoop();
    }

}
