package data;

import utils.Utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
public class Payment implements Comparable<Payment> {
    @XmlAttribute(name = "Name")
    private String chargeName;
    @XmlElement(name = "AmountToPay")
    private double amountOfPayment;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement(name = "PaymentDeadline")
    private LocalDate paymentDeadline;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement(name = "PaymentPaidDate")
    private LocalDate paymentPaidDate;
    @XmlElement(name = "PayPeriod")
    private int payPeriod;
    @XmlAttribute(name = "IsItPaid")
    private boolean isItPaid;

    public Payment() {
    }

    public Payment(String chargeName, double amountOfPayment, LocalDate paymentDeadline, LocalDate paymentPaidDate, int payPeriod) {
        this.chargeName = chargeName;
        this.amountOfPayment = amountOfPayment;
        this.paymentDeadline = paymentDeadline;
        this.paymentPaidDate = paymentPaidDate;
        this.payPeriod = payPeriod;
        this.isItPaid = paymentPaidDate != null;
    }

    public Payment(String chargeName, double amountOfPayment, LocalDate paymentDeadline, int payPeriod) {
        this.chargeName = chargeName;
        this.amountOfPayment = amountOfPayment;
        this.paymentDeadline = paymentDeadline;
        this.payPeriod = payPeriod;
        this.isItPaid = false;
    }
    public Payment(String chargeName,LocalDate paymentDeadline){
        this.chargeName = chargeName;
        this.paymentDeadline = paymentDeadline;
    }

    public Payment(Payment original) {
        this.chargeName = original.chargeName;
        this.amountOfPayment = original.amountOfPayment;
        this.paymentDeadline = original.paymentDeadline;
        this.paymentPaidDate = original.paymentPaidDate;
        this.payPeriod = original.payPeriod;
        this.isItPaid = original.paymentPaidDate != null;
    }

    public Payment(Payment original, LocalDate newDeadline) {
        this.chargeName = original.chargeName;
        this.amountOfPayment = original.amountOfPayment;
        this.paymentDeadline = newDeadline;
        this.paymentPaidDate = original.paymentPaidDate;
        this.payPeriod = original.payPeriod;
        this.isItPaid = original.paymentPaidDate != null;
    }


    @Override
    public String toString() {
        String month = "";
        for (Months element : Months.values()) {
            if (element.getID() == paymentDeadline.getMonthValue()) {
                month = element.getMonthDescription();
            }
        }
        return String.format("%s : %s | Wysokość : %s | Termin spłaty : %s | Ile dni do spłaty : %s",
                month, chargeName, amountOfPayment, paymentDeadline,
                (!this.isItPaid) ?
                        paymentDeadline.getDayOfYear() - LocalDate.now().getDayOfYear() : "Zapłacone !!!");
    }

    @Override
    public int compareTo(Payment o) {
        int outcome = chargeName.compareTo(o.chargeName);
        if (outcome == 0) {
            if (paymentDeadline.isAfter(o.paymentDeadline)) {
                return 1;
            }
            if (paymentDeadline.isBefore(o.paymentDeadline)) {
                return -1;
            } else {
                return 0;
            }
        }
        return outcome;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Payment){
            return this.compareTo((Payment) obj) == 0;
        }
        return false;
    }

    public void pay() {
        if (isItPaid) {
            System.out.println("Ten rachunek jest już zapłacony.");
            return;
        }
        isItPaid = true;
        paymentPaidDate = LocalDate.now();
    }

    public void showPayment() {
        System.out.printf("1....Nazwa...........: %s%s2....Wysokość........: %s%s3....Termin..........: %s%s" +
                        "4....Zapłacone.......: %s%s"
                , chargeName, System.lineSeparator(), amountOfPayment, System.lineSeparator()
                , paymentDeadline.format(Utils.dateTimeFormatter), System.lineSeparator(), isItPaid, System.lineSeparator());
        if (isItPaid()) {
            System.out.printf("5....Data.Zapłaty....: %s%s", paymentPaidDate.format(Utils.dateTimeFormatter), System.lineSeparator());
        }
    }

    public String getChargeName() {
        return chargeName;
    }


    public double getAmountOfPayment() {
        return amountOfPayment;
    }

    public void setAmountOfPayment(double amountOfPayment) {
        this.amountOfPayment = amountOfPayment;
    }

    public LocalDate getPaymentDeadline() {
        return paymentDeadline;
    }

    public void setPaymentDeadline(LocalDate paymentDeadline) {
        this.paymentDeadline = paymentDeadline;
    }

    public LocalDate getPaymentPaidDate() {
        return paymentPaidDate;
    }

    public void setPaymentPaidDate(LocalDate paymentPaidDate) {
        this.paymentPaidDate = paymentPaidDate;
    }

    public int getPayPeriod() {
        return payPeriod;
    }

    public void setPayPeriod(int payPeriod) {
        this.payPeriod = payPeriod;
    }

    public boolean isItPaid() {
        return isItPaid;
    }

    public void setItPaid(boolean itPaid) {
        this.isItPaid = itPaid;
    }
}