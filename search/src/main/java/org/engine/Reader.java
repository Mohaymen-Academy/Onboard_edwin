package org.engine;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;

public class Reader {

    private HashMap<String, String> docs = new HashMap<>();

    public void readDocs(String dirName) {
        docs.clear();
        Path directory = Paths.get(dirName);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.txt")) {
            for (Path file : stream) {
                String fileName = file.getFileName().toString();
                System.out.printf("Reading: %s\n----------------------\n", fileName);
                docs.put(fileName, Files.readString(file));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> getDocs() {
        return docs;
    }
}
