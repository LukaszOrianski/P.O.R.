package data;

import javax.xml.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@XmlRootElement(name = "YearPaymentsByMonths")
@XmlAccessorType(XmlAccessType.FIELD)
public class YearPayments {
    @XmlElement(name = "Month")
    private List<MonthPayments> monthPaymentsYearList;
    @XmlAttribute(name = "Year")
    private int thisYear;

    public YearPayments() {
        this.monthPaymentsYearList = new ArrayList<>(12);
    }

    public YearPayments(int thisYear) {
        this.monthPaymentsYearList = new ArrayList<>(Collections.singleton(new MonthPayments(LocalDate.now().getMonthValue())));
        this.thisYear = thisYear;
    }

    public YearPayments(List<MonthPayments> monthPaymentsYearList, int thisYear) {
        this.monthPaymentsYearList = monthPaymentsYearList;
        this.thisYear = thisYear;
    }

    public int getMonthIndex(int month) {
        for (int i = 0; i < monthPaymentsYearList.size(); i++) {
            if (monthPaymentsYearList.get(i).getMonth().getID() == month) {
                return i;
            }
        }
        return -1;
    }

    public Stream<Payment> getPaidBillsStream() {
        return getBillsStream(true);
    }

    public Stream<Payment> getUnpaidBillsStream() {
        return getBillsStream(false);
    }

    public Stream<Payment> getBillsStream(boolean paidStatus) {
        Predicate<Payment> isPaid;
        if (paidStatus) {
            isPaid = Payment::isItPaid;
        } else {
            isPaid = payment -> !payment.isItPaid();
        }
        return monthPaymentsYearList.stream()
                .map((yearList) -> yearList.getPaymentList().stream()
                        .filter(isPaid)
                        .collect(Collectors.toList()))
                .flatMap(List::stream);
    }

    public Stream<Payment> getBillsStream() {
        return monthPaymentsYearList.stream()
                .map((monthList) -> new ArrayList<>(monthList.getPaymentList()))
                .flatMap(List::stream);
    }

    public void removeEmptyMonth() {
        monthPaymentsYearList.removeIf(element -> element.getPaymentList().isEmpty());
    }


    public double getAllPaymentsSum() {
        return monthPaymentsYearList.stream()
                .map((result) -> result.getPaymentList().stream()
                        .map(Payment::getAmountOfPayment)
                        .mapToDouble(Double::doubleValue)
                        .sum())
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public List<MonthPayments> getMonthPaymentsYearList() {
        return monthPaymentsYearList;
    }


    public int getThisYear() {
        return thisYear;
    }
}
