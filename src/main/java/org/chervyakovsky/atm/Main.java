package org.chervyakovsky.atm;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        System.out.println(new Date());
        UserInterface userInterface = new UserInterface();
        while (true) {
            try {
                if (userInterface.checkingCardByNumber()) {
                    if (userInterface.checkingPinCode()) {
                        userInterface.startWork();
                    }
                }
            } catch (IOException | ParseException e) {
                System.out.println("Внутренняя ошибка банкомата! ".concat(e.getMessage()));
            }
        }
    }
}
