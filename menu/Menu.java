package menu;

import appCore.AppCore;
import data.Payment;
import data.PaymentDeadlineComparator;
import data.YearPayments;
import dictionary.APP_MSG;
import utils.Utils;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utils.Utils.hitEnterToContinue;

public class Menu {

    private Path dataFile;

    public Menu() {}

    public Menu(Path dataFile) {
        this.dataFile = dataFile;
    }

    private void printMainMenu() {
        printMainScreen();
        System.out.println(
                APP_MSG.MAIN_MENU.getMessage());
        System.out.println(APP_MSG.USER_CHOICE.getMessage());
    }

    private void printMainScreen() {
        YearPayments y = Utils.loadXmlData(dataFile.toFile());
        if (y == null) {
            return;
        }
        double saldo = (y.getPaidBillsStream()
                .map(Payment::getAmountOfPayment)
                .mapToDouble(Double::doubleValue)
                .sum()) - y.getAllPaymentsSum();

        if (saldo < 0) {
            System.out.printf(APP_MSG.SALDO_SUMMARY.getMessage(), Utils.formatDate(LocalDate.now()), saldo, System.lineSeparator());
            String ifDate = "";
            Predicate<Payment> current = element -> element.getPaymentDeadline().getMonthValue() >= LocalDate.now().getMonthValue();
            int currentUnpaid = getUnpaidCount(y.getUnpaidBillsStream(), current);
            if (currentUnpaid > 0) {
                List<Payment> list = y.getUnpaidBillsStream()
                        .filter(element -> element.getPaymentDeadline().getMonthValue() >= LocalDate.now().getMonthValue())
                        .sorted(new PaymentDeadlineComparator())
                        .collect(Collectors.toList());
                LocalDate lowCurrentDeadline = list.get(0).getPaymentDeadline();
                LocalDate highCurrentDeadline = list.get(list.size() - 1).getPaymentDeadline();
                ifDate = String.format(APP_MSG.DEADLINE_BETWEEN.getMessage(), Utils.formatDate(lowCurrentDeadline), Utils.formatDate(highCurrentDeadline));
            }

            double currentUnpaidValue = getUnpaidSum(y.getUnpaidBillsStream(), current);
            System.out.printf(APP_MSG.CURRENT_UNPAID_SUMMARY.getMessage()
                    , currentUnpaid, currentUnpaidValue, ifDate, System.lineSeparator());
            ifDate = "";
            Predicate<Payment> older = element -> element.getPaymentDeadline().getMonthValue() < LocalDate.now().getMonthValue();
            int olderUnpaid = getUnpaidCount(y.getUnpaidBillsStream(), older);
            if (olderUnpaid > 0) {
                LocalDate oldestDeadline = y.getUnpaidBillsStream().min(new PaymentDeadlineComparator())
                        .orElse(new Payment("błąd", LocalDate.MIN))
                        .getPaymentDeadline();
                int daysCount = LocalDate.now().getDayOfYear() - oldestDeadline.getDayOfYear();
                ifDate = String.format(APP_MSG.OLDEST_UNPAID_DEADLINE.getMessage(), daysCount, oldestDeadline.format(Utils.dateTimeFormatter));
            }
            double olderUnpaidValue = getUnpaidSum(y.getUnpaidBillsStream(), older);
            System.out.printf(APP_MSG.OLDER_UNPAID_SUMMARY.getMessage()
                    , olderUnpaid, olderUnpaidValue, ifDate, System.lineSeparator());
        } else {
            System.out.printf(APP_MSG.ALL_PAID.getMessage(), LocalDate.now().format(Utils.dateTimeFormatter));
        }
    }

    public void start() {
        AppCore appCore = new AppCore(dataFile);
        if (firstPayment()) {
            appCore.addNewPayment();
        }
        while (true) {
            printMainMenu();
            int choice = AppCore.inputGivenRangeInt(1, 6);
            switch (choice) {
                case 1:
                    appCore.paymentViewer();
                    hitEnterToContinue();
                    break;
                case 2:
                    appCore.doThePayment();
                    hitEnterToContinue();
                    break;
                case 3:
                    appCore.addNewPayment();
                    hitEnterToContinue();
                    break;
                case 4:
                    appCore.deletePayment();
                    hitEnterToContinue();
                    break;
                case 5:
                    Curiosities c = new Curiosities(dataFile);
                    c.showCuriosity();
                    break;
                case 6:
                    System.out.println(APP_MSG.GOODBYE.getMessage());
                    System.exit(0);
            }
        }
    }




    private boolean firstPayment() {
        YearPayments y = Utils.loadXmlData(dataFile.toFile());
        if (y == null) {
            return true;
        }
        return y.getBillsStream().count() == 0;
    }

    private int getUnpaidCount(Stream<Payment> s, Predicate<Payment> p) {
        return (int) s.filter(p)
                .count();
    }

    private double getUnpaidSum(Stream<Payment> s, Predicate<Payment> p) {
        return s.filter(p)
                .map(Payment::getAmountOfPayment)
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}