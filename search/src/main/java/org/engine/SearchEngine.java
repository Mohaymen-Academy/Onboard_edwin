package org.engine;

import java.util.*;
import java.util.function.Predicate;


class Utils {
    // Refactor: abstract normalizer
    public static String normalizeString(String s) {
        return s
                .strip()
                .replaceAll("'|\"", "")
                .toLowerCase();
    }

    public static void printDocsNameIndex(String[] docsNameIndex) {
        for (int i = 1; i < SearchEngine.MAX_DOCS && docsNameIndex[i] != null && !docsNameIndex[i].isEmpty(); i++) {
            System.out.printf("%d -> \"%s\"\n", i, docsNameIndex[i]);
        }
    }
}

public class SearchEngine {
    /*
        Implements inverted index algorithm
     */

    public static final boolean DEBUG = true;

    protected static final int MAX_DOCS = 1000;
    protected static final HashSet<String> stopWords = new HashSet<>(Arrays.asList(
            "is", "the", "i", "a", "an", "and", "of", "to", "in", "for",
            "on", "with", "as", "by", "this", "that", "it", "at", "from",
            "which", "but", "or", "be", "not"
    )); // Refactor: take as param


    private HashMap<String, SortedSet<Integer>> searchIndex;
    private final String[] docsNameIndex = new String[MAX_DOCS];
    private int lastDocIndex = 0;

    public SearchEngine(HashMap<String, String> docs) {
        refreshIndex(docs);
//        Utils.printDocsNameIndex(docsNameIndex);
    }

    private boolean addDoc(String docName) {
        if (docName.isEmpty()) {
            return false;
        } else {
            docsNameIndex[++lastDocIndex] = docName;
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

    private void refreshSearchIndex(int docIndex, ArrayList<String> tokens) {
        tokens.forEach(word ->
                searchIndex
                        .computeIfAbsent(word, ignored -> new TreeSet<>())
                        .add(docIndex)
        );
    }

    public void refreshIndex(HashMap<String, String> docs) {
        searchIndex = new HashMap<>();
        String normDocName;

        for (String docName : docs.keySet()) {
            normDocName = Utils.normalizeString(docName);
            if (addDoc(normDocName)) {
                int docIndex = lastDocIndex;
                ArrayList<String> tokens = tokenize(docs.get(docName));
                refreshSearchIndex(lastDocIndex, tokens);

                if (DEBUG && docIndex == 1) {
                    System.out.println(docName);
                    System.out.println(tokens);
                    System.out.println(tokens.size());
                }
            }
        }
    }

    public List<String> search(String query) {
        query = Utils.normalizeString(query);
        return searchIndex.getOrDefault(query, new TreeSet<>())
                .stream()
                .map(i -> docsNameIndex[i])
                .toList();
    }
}
