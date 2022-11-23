package org.chervyakovsky.atm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Date;

public class UserInterface {
    private final Service service = new Service();
    private Card card;

    public boolean checkingCardByNumber() throws IOException, ParseException {
        System.out.println("Введите номер карты!");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String cardNumber = bufferedReader.readLine();
        if (!service.cardNumberValidation(cardNumber)) {
            System.out.println("Вы ввели некоректный формат номера карты! Повторите попытку");
            return false;
        }
        this.card = service.findCardByNumber(cardNumber);
        if (this.card == null) {
            System.out.println("Данной карты нет в базе данных!");
            return false;
        }
        return true;
    }

    public boolean checkingPinCode() throws IOException { // todo не очень хороший метод, можно разбить (проверка блокировки и проверка самого пин)
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        boolean result = false;
        if (this.card.getLockDate() != null && checkBlockDate()) {
            this.card.setBlocked(false);
            this.card.setAttemptCounter(3);
            this.card.setLockDate(null);
            service.updateCard(this.card);
        }
        while (this.card.getAttemptCounter() > 0 && !this.card.isBlocked()) {
            System.out.println("Введите ПИН код:");
            String pinCode = bufferedReader.readLine();
            if (service.pinCodeValidation(pinCode)) {
                int pinCodeInt = Integer.parseInt(pinCode);
                if (this.card.getCodePIN() == pinCodeInt) {
                    this.card.setAttemptCounter(3);
                    this.service.updateCard(this.card);
                    result = true;
                    break;
                } else {
                    System.out.println("Вы ввели неверный ПИН!");
                    this.card.setAttemptCounter(this.card.getAttemptCounter() - 1);
                    this.service.updateCard(this.card);
                    result = false;
                }
            } else {
                System.out.println("Вы ввели некоректный формат ПИН! Повторите попытку");
            }
        }
        if (!result) {
            System.out.println("Карточка заблокирована");
            this.card.setBlocked(true);
            this.card.setLockDate(new Date());
            service.updateCard(this.card);
        }
        return result;
    }

    public void startWork() throws IOException {
        boolean status = true;
        while (status) {
            menu();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String userInput = bufferedReader.readLine();
            switch (userInput) {
                case "1":
                    System.out.printf("Баланс вашего счёта равен: %d\n", service.getBalanceCard(card));
                    status = continueOrNot();
                    break;
                case "2":
                    System.out.print("Введите сумму пополнения: ");
                    int replenishmentAmount = Integer.parseInt(bufferedReader.readLine());
                    boolean temp = service.putMoneyInCard(replenishmentAmount, this.card);
                    System.out.printf("Операция %sБаланс Вашего счёта: %d\n",
                            temp ? "выполнена!\n" : "не выполнена!\nСумма пополнения не должна превышать 1_000_000\n",
                            this.card.getBalance());
                    status = continueOrNot();
                    break;
                case "3":
                    System.out.print("Введите сумму вывода: ");
                    int withdrawalAmount = Integer.parseInt(bufferedReader.readLine());
                    switch (service.takeMoneyFromCard(withdrawalAmount, this.card)) {
                        case 1:
                            System.out.println("Операция не выполнена! Ваш баланс менее запрашиваемой суммы\n");
                            break;
                        case 2:
                            System.out.println("Операция не выполнена! Недостаточно средств в банкомате\n");
                            break;
                        case 3:
                            System.out.printf("Операция выполнена.\nБаланс Вашего счёта: %d\n", card.getBalance());
                            break;
                    }
                    status = continueOrNot();
                    break;
                case "0":
                    System.out.println("До новых встреч!");
                    status = false;
                    break;
                default:
                    System.out.println("Wrong input");
                    status = continueOrNot();
            }
        }
    }

    private boolean checkBlockDate() {
        Date dateNow = new Date();
        long timeDifference = dateNow.getTime() - this.card.getLockDate().getTime();
        return timeDifference > (24 * 60 * 60 * 1000);
    }

    private void menu() {
        System.out.print("\tНажмите 1 узнать балланс счёта.\n" +
                "\tНажмите 2 пополнить Ваш счёт.\n" +
                "\tНажмите 3 снять наличные.\n" +
                "\tНажмите 0 для завершения работы.\n");
    }

    private boolean continueOrNot() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\tПродолжить?\n1 - ДА  \"другая кнопка\"- НЕТ");
        String userInput = bufferedReader.readLine();
        if (userInput.equals("1")) {
            return true;
        } else {
            System.out.println("До новых встреч!");
            return false;
        }
    }
}

