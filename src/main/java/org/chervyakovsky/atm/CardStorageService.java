package org.chervyakovsky.atm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class CardStorageService implements StorageService<Card> {

    private static final String CARDS_DATA_FILE = "./src/main/resources/data/cards.txt";
    private List<String> cardsList = new ArrayList<>();


    @Override
    public List<String> read() throws IOException {
        this.cardsList.clear();
        if (checkFile(CARDS_DATA_FILE)) {
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(CARDS_DATA_FILE))) {
                while (reader.ready()) {
                    this.cardsList.add(reader.readLine());
                }
            } catch (IOException e) {
                throw new IOException("The problem with reading the file", e);
            }
        } else {
            throw new IOException("File not found");
        }
        return this.cardsList;
    }

    @Override
    public void save(Card card) throws IOException {
        ListIterator<String> iterator = this.cardsList.listIterator();
        while (iterator.hasNext()) {
            String tempCard = iterator.next();
            if (tempCard.contains(card.getCardNumber())) {
                iterator.set(card.toString());
            }
        }
        if (checkFile(CARDS_DATA_FILE)) {
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(CARDS_DATA_FILE))) {
                for (String value : this.cardsList) {
                    writer.write(value + "\n");
                }
            } catch (IOException e) {
                throw new IOException("The problem with writing the file", e);
            }
        } else {
            throw new IOException("File not found");
        }
    }
}
