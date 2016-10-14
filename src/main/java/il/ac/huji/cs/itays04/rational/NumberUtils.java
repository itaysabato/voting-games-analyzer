package il.ac.huji.cs.itays04.rational;

import org.apache.commons.math3.fraction.BigFraction;

import java.text.NumberFormat;

public class NumberUtils {

    public static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance();

    static {
        NUMBER_FORMAT.setGroupingUsed(false);
        NUMBER_FORMAT.setMaximumFractionDigits(4);
    }

    public static String fractionToString(BigFraction fraction) {
        final String string = fraction.toString();
        return string + (string.contains("/") ?
                " (" + format(fraction.doubleValue()) + ")" : "");
    }

    public static String format(Number value) {
        return value instanceof  BigFraction ?
                fractionToString((BigFraction) value) :
                format(value.doubleValue());
    }

    public static String format(double value) {
        return NUMBER_FORMAT.format(value);
    }
}
