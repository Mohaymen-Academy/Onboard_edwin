package org.engine;

import java.util.*;
import java.util.function.Predicate;


class Utils {
    // Refactor: abstract normalizer
    public static String normalizeString(String s) {
        return s
                .strip()
                .replaceAll("\'|\"", "")
                .toLowerCase();
    }

    public static void printDocsNameIndex(String[] docsNameIndex) {
        for (int i = 1; i < SearchEngine.MAX_DOCS && docsNameIndex[i] != null && !docsNameIndex[i].isEmpty(); i++) {
            System.out.printf("%d -> \"%s\"\n", i, docsNameIndex[i]);
        }
    }
}

public class SearchEngine {
    public static final boolean DEBUG = true;

    protected static final int MAX_DOCS = 1000;
    protected static final HashSet<String> stopWords = new HashSet<>(Arrays.asList(
            "is", "the", "i", "a", "an", "and", "of", "to", "in", "for",
            "on", "with", "as", "by", "this", "that", "it", "at", "from",
            "which", "but", "or", "be", "not"
    )); // Refactor: take as param


    private HashMap<String, LinkedList<Integer>> searchIndex;
    private final String[] docsNameIndex = new String[MAX_DOCS];
    private int docsCount = 0;

    public SearchEngine(HashMap<String, String> docs) {
        refreshIndex(docs);
//        Utils.printDocsNameIndex(docsNameIndex);
    }

    private boolean addDoc(String docName) {
        String normDocName = Utils.normalizeString(docName);
        if (normDocName.isEmpty()) {
            return false;
        } else {
            docsNameIndex[++docsCount] = normDocName;
            return true;
        }
    }

    private ArrayList<String> tokenize(String doc) {
        return new ArrayList<>(Arrays
                .stream(doc.split("\\W|[0-9]"))
                .map(Utils::normalizeString)
                .filter(Predicate.not(stopWords::contains))
                .filter(Predicate.not(String::isBlank))
                .toList()
        );
    }

    public void refreshIndex(HashMap<String, String> docs) {
        searchIndex = new HashMap<>();

        for (String docName : docs.keySet()) {
            if (addDoc(docName)) {
                int docIndex = docsCount;
                ArrayList<String> tokens = tokenize(docs.get(docName));

                if (DEBUG && docIndex == 1) {
                    System.out.println(docName);
                    System.out.println(tokens);
                    System.out.println(tokens.size());
                }
            }
        }
    }

}
