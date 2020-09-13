package data;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MonthsAdapter extends XmlAdapter<String, Months> {
    @Override
    public Months unmarshal(String v) {
        for (Months element : Months.values()) {
            if (element.getMonthDescription().equals(v)) {
                return element;
            }
        }
        return null;
    }

    @Override
    public String marshal(Months v) {
        return v.getMonthDescription();
    }
}
