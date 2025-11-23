package org.engine;

import java.util.*;
import java.util.function.Predicate;


class Utils {
    public static String normalizeString(String s) {
        return s
                .strip()
                .toLowerCase();
    }

    public static void printDocsNameIndex(String[] docsNameIndex) {
        for (int i = 1; i < SearchEngine.MAX_DOCS && docsNameIndex[i] != null && !docsNameIndex[i].isEmpty(); i++) {
            System.out.printf("%d -> \"%s\"\n", i, docsNameIndex[i]);
        }
    }
}

public class SearchEngine {

    protected static final int MAX_DOCS = 1000;
    protected static final HashSet<String> stopWords = new HashSet<>(Arrays.asList(
            "is", "the", "i", "a", "an", "and", "of", "to", "in", "for",
            "on", "with", "as", "by", "this", "that", "it", "at", "from",
            "which", "but"
    ));


    private HashMap<String, LinkedList<Integer>> searchIndex;
    private final String[] docsNameIndex = new String[MAX_DOCS];
    private int docsCount = 0;

    public SearchEngine(HashMap<String, String> docs) {
        refreshIndex(docs);
//        Utils.printDocsNameIndex(docsNameIndex);
    }

    private boolean addDoc(String docName) {
        String normDoc = Utils.normalizeString(docName);
        if (normDoc.isEmpty()) {
            return false;
        } else {
            docsNameIndex[++docsCount] = normDoc;
            return true;
        }

    }

    private ArrayList<String> tokenize(String doc) {
        return new ArrayList<>(Arrays
                .stream(doc.split("[ \\p{P}\\d]+"))
                .map(Utils::normalizeString)
                .filter(Predicate.not(String::isBlank))
                .toList()
        );

        /* TODO: compare the number of tokens compare with `wc -w` there are fewer
         e.g: Working Effectively With Legacy Code.txt: 8190 (by `wc -w`) VS 8075 (by `tokenize`) */
    }

    public void refreshIndex(HashMap<String, String> docs) {
        searchIndex = new HashMap<>();

        for (String docName : docs.keySet()) {
            if (addDoc(docName)) {
                int docIndex = docsCount;
                ArrayList<String> tokens = tokenize(docs.get(docName));
                if (docIndex == 1) {
                    System.out.println(docName);
                    System.out.println(tokens);
                    System.out.println(tokens.size());
                }
            }
        }
    }

}
