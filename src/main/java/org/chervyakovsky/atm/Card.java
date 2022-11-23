package org.chervyakovsky.atm;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Card implements Serializable {
    private String cardNumber;
    private int codePIN;
    private int balance;
    private boolean isBlocked;
    private int attemptCounter = 3;
    private Date lockDate;

    public Card() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCodePIN() {
        return codePIN;
    }

    public void setCodePIN(int codePIN) {
        this.codePIN = codePIN;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public int getAttemptCounter() {
        return attemptCounter;
    }

    public void setAttemptCounter(int attemptCounter) {
        this.attemptCounter = attemptCounter;
    }

    public Date getLockDate() {
        return lockDate;
    }

    public void setLockDate(Date lockDate) {
        this.lockDate = lockDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String string = cardNumber.concat(" ")
                .concat("pin:").concat(String.valueOf(codePIN)).concat(" ")
                .concat("balance:").concat(String.valueOf(balance)).concat(" ")
                .concat("blocking:").concat(String.valueOf(isBlocked)).concat(" ")
                .concat("attemptCounter:").concat(String.valueOf(attemptCounter)).concat(" ")
                .concat("lockDate:");
        if (lockDate == null) {
            return string.concat("null");
        } else {
            return string.concat(formatter.format(lockDate));
        }
    }
}
