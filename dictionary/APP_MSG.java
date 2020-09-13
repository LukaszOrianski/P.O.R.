package dictionary;

public enum APP_MSG {
    USER_CHOICE(".....Twój wybór ?"),
    DATA_ERROR("Blad odczytu/zapisu danych"),
    INPUT_ERROR("Bledne dane, prosze ponownie"),
    OUT_OF_RANGE("Wpisano błędną wartość (prawidłowy zakres %s-%s)%n"),
    MAIN_MENU("1.... Przeglądaj rachunki" + System.lineSeparator() +
            "2.... Zapłać rachunek" + System.lineSeparator() +
            "3.... Dodaj rachunek" + System.lineSeparator() +
            "4.... Usuń rachunek" + System.lineSeparator() +
            "5.... Ciekawostki" + System.lineSeparator() +
            "6.... Wyjście z aplikacji"),
    BILL_PAID("%s za %s zapłacone !%n"),
    U_SURE("Potwierdź - napewno chcesz %s rachunek (t/n)?"),
    GOODBYE("Do widzenia!"),
    SALDO_SUMMARY("Saldo Twoich rachunków na dzień %s : %s PLN%s"),
    DEADLINE_BETWEEN(" z terminem płatności między %s a %s"),
    CURRENT_UNPAID_SUMMARY("Rachunki niezapłacone (bieżące): %s / %s PLN %s + %s"),
    OLDER_UNPAID_SUMMARY("Rachunki niezapłacone (po terminie) : %s / %s PLN. %s %s"),
    ALL_PAID("Na dzień dzisiejszy, tj. %s, wszystkie rachunki są opłacone !!!%n"),
    OLDEST_UNPAID_DEADLINE(" Najstarszy termin płatności minął %s dni temu (%s)."),
    BILL_EXIST("Rachunek istnieje. Proszę jeszcze raz."),
    ENTER_BILL_NAME("Proszę podać nazwę (maks 20 liter/cyfr)"),
    ENTER_DEADLINE("Proszę wprowadzić termin zapłaty (dd-mm-yyyy)"),
    ENTER_BILL_AMOUNT("Prosze podac kwote rachunku"),
    ENTER_PAY_PERIOD("Prosze podac okres płatności w miesiacach (1-12)");

    private final String message;

    APP_MSG(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
