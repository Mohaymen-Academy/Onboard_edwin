package org.engine;

import interfaces.Normalizer;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Utils {
    // Only for debug purposes
    public static void printDocsNameIndex(String[] docsNameIndex) {
        for (int i = 1; i < SearchEngine.MAX_DOCS && docsNameIndex[i] != null && !docsNameIndex[i].isEmpty(); i++) {
            System.out.printf("%d -> \"%s\"\n", i, docsNameIndex[i]);
        }
    }
}

class AdvanceQueryParser {
    private final HashSet<String> andPatterns, orPatterns, notPatterns;

    public AdvanceQueryParser(String query) {
        andPatterns = new HashSet<>();
        orPatterns = new HashSet<>();
        notPatterns = new HashSet<>();

        Arrays.stream(query.split(" "))
                .filter(Predicate.not(String::isBlank))
                .forEach(pattern -> {
                    String head = pattern.substring(0, 1);
                    String tail = pattern.substring(1);
                    switch (head) {
                        case "+" -> orPatterns.add(tail);
                        case "-" -> notPatterns.add(tail);
                        default -> andPatterns.add(pattern);
                    }
                });
    }

    public HashSet<String> getAndPatterns() {
        return andPatterns;
    }

    public HashSet<String> getOrPatterns() {
        return orPatterns;
    }

    public HashSet<String> getNotPatterns() {
        return notPatterns;
    }
}

public class SearchEngine {
    /*
     * Implements inverted index algorithm
     */

    protected static final int MAX_DOCS = 1000;
    protected static HashSet<String> stopWords = new HashSet<>(Arrays.asList(
            "is", "the", "i", "a", "an", "and", "of", "to", "in", "for",
            "on", "with", "as", "by", "this", "that", "it", "at", "from",
            "which", "but", "or", "be", "not"));

    private Normalizer normalizer;
    private HashMap<String, SortedSet<Integer>> searchIndex;
    private final String[] docsNameIndex = new String[MAX_DOCS];
    private int lastDocIndex = 0;

    public SearchEngine(HashMap<String, String> docs, Normalizer normalizer) {
        this.normalizer = normalizer;
        refreshIndex(docs);
        // Utils.printDocsNameIndex(docsNameIndex);
    }

    public void setStopWords(Set<String> stopWords) {
        this.stopWords.clear();
        this.stopWords.addAll(stopWords);
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
                .map(this.normalizer::normalize)
                .filter(Predicate.not(stopWords::contains))
                .filter(Predicate.not(String::isBlank))
                .toList());
    }

    private void refreshSearchIndex(int docIndex, ArrayList<String> tokens) {
        tokens.forEach(word -> searchIndex
                .computeIfAbsent(word, ignored -> new TreeSet<>())
                .add(docIndex));
    }

    public void refreshIndex(HashMap<String, String> docs) {
        searchIndex = new HashMap<>();
        String normDocName;

        for (String docName : docs.keySet()) {
            normDocName = normalizer.normalize(docName);
            if (addDoc(normDocName)) {
                int docIndex = lastDocIndex;
                ArrayList<String> tokens = tokenize(docs.get(docName));
                refreshSearchIndex(lastDocIndex, tokens);

                if (Main.DEBUG && docIndex == 1) {
                    System.out.println(docName);
                    System.out.println(tokens);
                    System.out.println(tokens.size());
                }
            }
        }
    }

    private TreeSet<String> findDocs(String word) {
        return searchIndex
                .getOrDefault(word, new TreeSet<>())
                .stream()
                .map(i -> docsNameIndex[i])
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private Set<String> findAllDocs(Set<String> words) {
        return words
                .stream()
                .flatMap(doc -> findDocs(doc).stream())
                .collect(Collectors.toSet());
    }

    public TreeSet<String> search(String query) {
        TreeSet<String> result = new TreeSet<>();

        query = normalizer.normalize(query);
        AdvanceQueryParser parser = new AdvanceQueryParser(query);
        if (Main.DEBUG) {
            System.out.printf("AND Patterns = %s\nOR PATTERNS = %s\nNOT PATTERNS = %s\n",
                    parser.getAndPatterns(),
                    parser.getOrPatterns(),
                    parser.getNotPatterns());
        }

        Set<String> orPatterns = parser.getOrPatterns();

        result.addAll(findAllDocs(orPatterns));
        if (!orPatterns.isEmpty() && result.isEmpty()) {
            // Force that at least one OR pattern matches
            return result;
        }

        Set<String> andPatterns = parser.getAndPatterns();
        if (!andPatterns.isEmpty()) {
            Set<String> foundDocs = findAllDocs(andPatterns);
            if (result.isEmpty())
                result.addAll(foundDocs);
            else
                result.retainAll(foundDocs);
        }

        result.removeAll(findAllDocs(parser.getNotPatterns()));
        // n.b. Searches like "-word" give no result

        return result;
    }
}
