package org.chervyakovsky.atm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface StorageService<T> {

    List<String> read() throws IOException;

    void save(T object) throws IOException;

    default boolean checkFile(String fileName) throws FileNotFoundException {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new FileNotFoundException("FileName is null or empty string");
        }
        File file = new File(fileName);
        return file.exists();
    }
}
