package data;

import java.util.Comparator;

public class PaymentDeadlineComparator implements Comparator<Payment> {
    @Override
    public int compare(Payment o1, Payment o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        if (o1.getPaymentDeadline().isAfter(o2.getPaymentDeadline())) {
            return 1;
        } else if (o1.getPaymentDeadline().isBefore(o2.getPaymentDeadline())) {
            return -1;
        } else {
            return 0;
        }
    }
}
