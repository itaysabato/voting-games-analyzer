package il.ac.huji.cs.itays04.utils;

import org.apache.commons.math3.fraction.BigFraction;

public class NumberUtils {
    public static String fractionToString(BigFraction fraction) {
        final String string = fraction.toString();
        return string.contains("/") ? numberToString(fraction) : string;
    }

    public static String numberToString(Number value) {
        return value + " (" + value.doubleValue() + ")";
    }

}
