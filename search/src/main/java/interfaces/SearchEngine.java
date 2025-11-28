package interfaces;

import java.util.Map;
import java.util.Set;

public interface SearchEngine {
    public void addEntry(String key, String value);
    public void addEntries(Map<String, String> entries);

    public void clearIndex();

    public Set<String> search(String query);
}
