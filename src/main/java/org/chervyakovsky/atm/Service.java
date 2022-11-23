package org.chervyakovsky.atm;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Pattern;

public class Service {
    private static final String REG_CARD_NUMBER = "\\d{4}-\\d{4}-\\d{4}-\\d{4}";
    private static final String REG_PIN_CODE = "\\d{4}";
    private final StorageService<Card> cardStorageService = new CardStorageService();
    private final StorageService<Atm> atmStorageService = new AtmStorageService();

    public boolean cardNumberValidation(String cardNumber) {
        Pattern pattern = Pattern.compile(REG_CARD_NUMBER);
        return pattern.matcher(cardNumber).matches();
    }

    public boolean pinCodeValidation(String pinCode) {
        Pattern pattern = Pattern.compile(REG_PIN_CODE);
        return pattern.matcher(pinCode).matches();
    }

    public Card findCardByNumber(String cardNumber) throws ParseException, IOException {
        List<String> cardList = cardStorageService.read();
        for (String tempCardData : cardList) {
            if (tempCardData.contains(cardNumber)) {
                return Parser.cardParser(tempCardData);
            }
        }
        return null;
    }

    public int getBalanceCard(Card card) {
        return card.getBalance();
    }

    public boolean putMoneyInCard(int replenishmentAmount, Card card) throws IOException {
        boolean result = false;
        Atm atm = getAtm();
        if (replenishmentAmount < 1_000_000) {
            card.setBalance(card.getBalance() + replenishmentAmount);
            atm.setCash(atm.getCash() + replenishmentAmount);
            atmStorageService.save(atm);
            cardStorageService.save(card);
            result = true;
        }
        return result;
    }

    public int takeMoneyFromCard(int withdrawalAmount, Card card) throws IOException {
        int status;
        Atm atm = getAtm();
        if (withdrawalAmount > card.getBalance()) {
            status = 1;
        } else if (withdrawalAmount > atm.getCash()) {
            status = 2;
        } else {
            status = 3;
            card.setBalance(card.getBalance() - withdrawalAmount);
            atm.setCash(atm.getCash() - withdrawalAmount);
            cardStorageService.save(card);
            atmStorageService.save(atm);
        }
        return status;
    }

    public void updateCard(Card card) throws IOException {
        cardStorageService.save(card);
    }

    private Atm getAtm() throws IOException {
        List<String> atmList = atmStorageService.read();
        return Parser.atmParser(atmList.get(0));
    }

}