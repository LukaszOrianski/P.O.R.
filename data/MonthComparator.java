package data;

import java.util.Comparator;

public class MonthComparator implements Comparator<MonthPayments> {
    @Override
    public int compare(MonthPayments o1, MonthPayments o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        if (o1.getMonth().getID() < o2.getMonth().getID()) {
            return 1;
        }
        if (o1.getMonth().getID() > o2.getMonth().getID()) {
            return -1;
        } else {
            return 0;
        }
    }
}
