package org.engine;

import java.util.HashMap;
import java.util.LinkedList;


class Utils {
    public static String normalizeString(String s) {
        return s.strip().toLowerCase();
    }

    public static void printDocsNameIndex(String[] docsNameIndex) {
        for (int i = 0; i < SearchEngine.MAX_DOCS && docsNameIndex[i] != null && !docsNameIndex[i].isEmpty(); i++) {
            System.out.printf("%d -> \"%s\"\n", i, docsNameIndex[i]);
        }
    }
}

public class SearchEngine {

    protected static final int MAX_DOCS = 1000;

    private HashMap<String, LinkedList<Integer>> searchIndex;
    private final String[] docsNameIndex = new String[MAX_DOCS];
    private int docsCount = 0;

    public SearchEngine(HashMap<String, String> docs) {
        refreshIndex(docs);
        Utils.printDocsNameIndex(docsNameIndex);
    }

    private void addDoc(String docName) {
        String normDoc = Utils.normalizeString(docName);
        if (!normDoc.isEmpty()) docsNameIndex[docsCount++] = normDoc;
    }

    public void refreshIndex(HashMap<String, String> docs) {
        searchIndex = new HashMap<>();

        for (String docName : docs.keySet()) {
            addDoc(docName);
        }
    }

}
