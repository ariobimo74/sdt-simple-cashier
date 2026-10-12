package id.co.softwaredeveloperstoday.cms.dashboard.web.util.common;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class ServiceUtil {

    public static String convertPriceWithCurrency(Double price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(0);

        if (Objects.nonNull(price)) return formatter.format(price);
        else return formatter.format(0L);
    }

}
