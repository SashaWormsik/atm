package org.chervyakovsky.atm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AtmStorageService implements StorageService<Atm> {

    private static final String ATM_DATA_FILE = "./src/main/resources/data/atm.txt";

    @Override
    public List<String> read() throws IOException {
        checkFile(ATM_DATA_FILE);
        ArrayList<String> arrayList = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(ATM_DATA_FILE))) {
            while (reader.ready()) {
                arrayList.add(reader.readLine());
            }
        } catch (IOException e) {
            throw new IOException("The problem with reading the file", e);
        }
        return arrayList;
    }

    @Override
    public void save(Atm atm) throws IOException {
        if (checkFile(ATM_DATA_FILE)) {
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ATM_DATA_FILE))) {
                writer.write(atm.toString());
            } catch (IOException e) {
                throw new IOException("The problem with writing the file", e);
            }
        } else {
            throw new IOException("File not found");
        }
    }
}
