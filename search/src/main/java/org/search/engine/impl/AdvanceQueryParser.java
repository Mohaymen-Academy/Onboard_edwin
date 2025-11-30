package org.search.engine.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Predicate;

public class AdvanceQueryParser {
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
