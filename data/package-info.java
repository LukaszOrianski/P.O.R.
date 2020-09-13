@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(type = LocalDate.class, value = LocalDateAdapter.class),
        @XmlJavaTypeAdapter(type = Months.class, value = MonthsAdapter.class)
})

package data;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import java.time.LocalDate;