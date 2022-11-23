package org.chervyakovsky.atm;

import java.io.Serializable;

public class Atm implements Serializable {
    private int cash;

    public Atm() {
    }

    public Atm(int cash) {
        this.cash = cash;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    @Override
    public String toString() {
        return "cash=" + cash;
    }
}
