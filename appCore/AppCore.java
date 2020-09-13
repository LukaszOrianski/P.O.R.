package appCore;

import data.MonthPayments;
import data.Payment;
import data.YearPayments;
import dictionary.APP_MSG;
import utils.Utils;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static utils.Utils.int2MonthDescription;
import static utils.Utils.updateXmlDataFile;

public class AppCore {

    private final Path dataFile;

    public AppCore(Path dataFile) {
        this.dataFile = dataFile;
    }

    public void deletePayment() {
        YearPayments y = Utils.loadXmlData(dataFile.toFile());
        if (y == null) {
            return;
        }
        Payment payment = getPaymentToOperate(getChosenMonthList(y.getMonthPaymentsYearList()));
        List<Payment> monthList = y.getMonthPaymentsYearList().get(y.getMonthIndex(payment.getPaymentDeadline().getMonthValue())).getPaymentList();
        monthList.remove(payment);
        if (monthList.isEmpty()) {
            y.getMonthPaymentsYearList().remove(y.getMonthIndex(payment.getPaymentDeadline().getMonthValue()));
        }
        payment.showPayment();
        System.out.printf(APP_MSG.U_SURE.getMessage(), "skaskować");
        if (yesOrNo("t", "n")) {
            if (!Utils.updateXmlDataFile(dataFile.toFile(), y)) {
                System.out.println(APP_MSG.DATA_ERROR.getMessage());
            }
        }
    }

    public void addNewPayment() {
        YearPayments y = Utils.loadXmlData(dataFile.toFile());
        if (y == null) {
            System.out.println(APP_MSG.DATA_ERROR.getMessage());
            return;
        }
        System.out.println("Nowy rachunek na rok " + y.getThisYear() + ":");
        List<Payment> list = y.getBillsStream().collect(Collectors.toList());
        Payment newPayment;
        boolean goFurther;
        do {
            System.out.println(APP_MSG.ENTER_BILL_NAME.getMessage());
            String name = enterRegexData();
            System.out.println(APP_MSG.ENTER_DEADLINE.getMessage());
            LocalDate deadline = enterDate(y.getThisYear());
            newPayment = new Payment(name, deadline);
            if (list.contains(newPayment)) {
                goFurther = false;
                System.out.println(APP_MSG.BILL_EXIST.getMessage());
            } else {
                goFurther = true;
            }
        } while (!goFurther);
        System.out.println(APP_MSG.ENTER_BILL_AMOUNT.getMessage());
        newPayment.setAmountOfPayment(enterDouble());
        System.out.println(APP_MSG.ENTER_PAY_PERIOD.getMessage());
        newPayment.setPayPeriod(inputGivenRangeInt(1, 12));
        int newMonth = y.getMonthIndex(newPayment.getPaymentDeadline().getMonthValue());
        if (newMonth < 0) {
            y.getMonthPaymentsYearList().add(new MonthPayments(newPayment.getPaymentDeadline().getMonthValue()));
        }
        y.getMonthPaymentsYearList().get(y.getMonthIndex(newPayment.getPaymentDeadline().getMonthValue())).getPaymentList().add(newPayment);
        System.out.printf(APP_MSG.U_SURE.getMessage(), "dodać");
        if (yesOrNo("t", "n")) {
            if (!updateXmlDataFile(dataFile.toFile(), y)) {
                System.out.println(APP_MSG.DATA_ERROR.getMessage());
            }
        }
    }

    public void doThePayment() {
        YearPayments y = Utils.loadXmlData(dataFile.toFile());
        if (y == null) {
            return;
        }
        Payment payment = getPaymentToOperate(getChosenMonthList(y.getMonthPaymentsYearList()));
        payment.showPayment();
        payment.pay();
        System.out.printf(APP_MSG.U_SURE.getMessage(), "zapłacić");
        if (yesOrNo("t", "n")) {
            if (!updateXmlDataFile(dataFile.toFile(), y)) {
                System.out.println(APP_MSG.DATA_ERROR.getMessage());
                return;
            }
            System.out.printf(APP_MSG.BILL_PAID.getMessage()
                    , payment.getChargeName(), int2MonthDescription(payment.getPaymentDeadline().getMonthValue()));
        }
    }

    public void paymentViewer() {
        YearPayments y = Utils.loadXmlData(dataFile.toFile());
        if (y == null) {
            System.out.println(APP_MSG.DATA_ERROR.getMessage());
            return;
        }
        getPaymentToOperate(getChosenMonthList(y.getMonthPaymentsYearList())).showPayment();
    }

    private double enterDouble() {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        double amount = 0;
        while (input.equals("")) {
            input = scanner.nextLine();
            try {
                amount = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                input = "";
            }
        }
        return amount;
    }

    private List<Payment> getChosenMonthList(List<MonthPayments> months) {
        for (int i = 0; i < months.size(); i++) {
            System.out.printf("%s....%s(%s)%n", i + 1, months.get(i).getMonth().getMonthDescription()
                    , months.get(i).getPaymentList().size());
        }
        System.out.println(APP_MSG.USER_CHOICE.getMessage());
        return months.get(inputGivenRangeInt(1, months.size()) - 1).getPaymentList();
    }

    private Payment getPaymentToOperate(List<Payment> payments) {
        for (int i = 0; i < payments.size(); i++) {
            System.out.printf("%s....%s%n", i + 1, payments.get(i).getChargeName());
        }
        System.out.println(APP_MSG.USER_CHOICE.getMessage());
        return payments.get(inputGivenRangeInt(1, payments.size()) - 1);
    }

    private LocalDate enterDate(int year) {
        Scanner scanner = new Scanner(System.in);
        String input;
        LocalDate localDate = null;

        while (localDate == null) {
            input = scanner.nextLine();
            try {
                localDate = LocalDate.parse(input, Utils.dateTimeFormatter);
                if (localDate.getYear() != year) {
                    localDate = null;
                    throw new DateTimeParseException(null, "", 0);
                }
            } catch (DateTimeParseException e) {
                System.out.println(APP_MSG.INPUT_ERROR.getMessage());
            }
        }
        return localDate;
    }

    public static int inputGivenRangeInt(int a, int b) {
        Scanner scanner = new Scanner(System.in);
        String input;
        int userChoice = -1;
        do {
            try {
                input = scanner.nextLine();
                userChoice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                input = null;
            }
            if (userChoice < a || userChoice > b) {
                input = null;
                System.out.printf(dictionary.APP_MSG.OUT_OF_RANGE.getMessage(), a, b);
            }
        } while (input == null);
        return userChoice;
    }

    private boolean yesOrNo(String y, String n) {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equals(y) && !input.equals(n)) {
            input = scanner.nextLine();
        }
        return input.equals(y);
    }

    private String enterRegexData() {
        Scanner scanner = new Scanner(System.in);
        String input;
        Pattern pattern = Pattern.compile(Utils.paymentNamePattern);
        Matcher matcher;
        do {
            input = scanner.nextLine();
            matcher = pattern.matcher(input);
        } while (!matcher.matches());
        return input;
    }
}
