package il.ac.huji.cs.itays04.cli;

import com.beust.jcommander.IStringConverter;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.fraction.ProperBigFractionFormat;

public class BigFractionConverter implements IStringConverter<BigFraction> {
    private static final ProperBigFractionFormat FORMAT = new ProperBigFractionFormat();

    @Override
    public BigFraction convert(String value) {
        return FORMAT.parse(value);
    }
}
