package data;

import utils.Utils;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class MonthPayments implements Comparable<MonthPayments>{
    @XmlJavaTypeAdapter(MonthsAdapter.class)
    @XmlAttribute(name = "Name")
    private Months month;
    @XmlElement(name = "Payment")
    private List<Payment> paymentList = new ArrayList<>();

    public MonthPayments(Months month, List<Payment> paymentList) {
        this.month = month;
        this.paymentList = paymentList;
    }
    public MonthPayments(int monthID,List<Payment> paymentList){
        this.month = Utils.int2Months(monthID);
        this.paymentList = paymentList;
    }
    public MonthPayments(int monthID){
        this.month = Utils.int2Months(monthID);
        this.paymentList = new ArrayList<>();
    }

    public MonthPayments(Months month){
        this.month = month;
        this.paymentList = new ArrayList<>();
    }
    public MonthPayments(){}

    @Override
    public String toString() {
        return "Payments{" +
                "month=" + month +
                ", paymentList=" + paymentList +
                '}';
    }

    public Months getMonth() {
        return month;
    }

    public void setMonth(Months month) {
        this.month = month;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }


    @Override
    public int compareTo(MonthPayments monthPayments) {
        return this.month.getMonthDescription().compareTo(monthPayments.month.getMonthDescription());
    }
}
