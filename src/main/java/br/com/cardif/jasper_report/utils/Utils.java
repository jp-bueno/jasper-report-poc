package br.com.cardif.jasper_report.utils;

import java.util.Locale;

public class Utils {

    public static String convetToStringFromInteger(Integer value) {
        return value.toString();
    }

    public static String formatDecimals(Double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }
}
