package data;

public enum Months {
    JANUARY("Styczen", 1),
    FEBRUARY("Luty", 2),
    MARCH("Marzec", 3),
    APRIL("Kwiecien", 4),
    MAY("Maj", 5),
    JUNE("Czerwiec", 6),
    JULY("Lipiec", 7),
    AUGUST("Sierpien", 8),
    SEPTEMBER("Wrzesien", 9),
    OCTOBER("Pazdziernik", 10),
    NOVEMBER("Listopad", 11),
    DECEMBER("Grudzien", 12);
    private final String monthDescription;
    private final int ID;

    Months(String monthDescription, int ID) {
        this.monthDescription = monthDescription;
        this.ID = ID;
    }

    public String getMonthDescription() {
        return monthDescription;
    }


    public int getID() {
        return ID;
    }
}
