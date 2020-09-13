package utils;

import data.MonthPayments;
import data.Months;
import data.YearPayments;

import javax.xml.bind.JAXB;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Scanner;

public class Utils {

    private Utils() {
    }

    public static final String paymentNamePattern = "[a-zA-Z_0-9]{1,20}";

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static String int2MonthDescription(int monthID) {
        for (Months element : Months.values()) {
            if (element.getID() == monthID) {
                return element.getMonthDescription();
            }
        }
        return null;
    }

    public static Months int2Months(int monthID) {
        for (Months element : Months.values()) {
            if (element.getID() == monthID) {
                return element;
            }
        }
        return null;
    }

    public static YearPayments loadXmlData(File dataFile) {
        if (!dataFile.exists()) {
            return null;
        }
        YearPayments y;
        if ((y = JAXB.unmarshal(dataFile, YearPayments.class)) != null) {
            y.getMonthPaymentsYearList().sort(Comparator.comparingInt(e -> e.getMonth().getID()));
            return y;
        }
        return null;
    }

    public static boolean updateXmlDataFile(File dataFile, YearPayments y) {
        if (dataFile == null || y == null) {
            return false;
        }
        y.removeEmptyMonth();
        y.getMonthPaymentsYearList().sort(Comparator.comparingInt((MonthPayments e) -> e.getMonth().getID()).reversed());
        JAXB.marshal(y, dataFile);
        return true;
    }

    public static String formatDate(LocalDate localDate) {
        return localDate.format(dateTimeFormatter);
    }

    public static void hitEnterToContinue() {
        System.out.println("Aby kontynuować, wciśnij \"Enter\"");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}