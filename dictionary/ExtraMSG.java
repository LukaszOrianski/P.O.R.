package dictionary;

public enum ExtraMSG {
    ONE("Srednia równowartość Twoich rachunków to %s browarów w plenerze.", 1, 3.0),
    TWO("Srednia równowartość Twoich rachunków to %s świadczeń 500+.", 2, 500.0),
    THREE("Srednia równowartość Twoich rachunków to %s sredniej ceny m2 mieszkania w W-wie", 3, 10000.0),
    FOUR("Srednia równowartość Twoich rachunków to %s obiadów w stołówce.", 4, 20.0),
    FIVE("Srednia równowartość Twoich rachunków to %s browarów w klubie.", 5, 10.0),
    SIX("Srednia równowartość Twoich rachunków to %s litrów benzyny 95.",6,4.70);
    private final String message;
    private final int ID;
    private final double price;

    ExtraMSG(String message, int id, double price) {
        this.message = message;
        this.ID = id;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public int getID() {
        return ID;
    }

    public String getMessage() {
        return message;
    }
}
