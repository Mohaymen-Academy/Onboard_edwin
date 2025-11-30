package org.search.engine;

import java.util.Map;
import java.util.Set;

public interface SearchEngine {
    void addEntry(String key, String value);
    void addEntries(Map<String, String> entries);

    void clearIndex();

    Set<String> search(String query);
}
