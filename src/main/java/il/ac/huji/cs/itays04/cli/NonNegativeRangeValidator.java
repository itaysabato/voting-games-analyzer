package il.ac.huji.cs.itays04.cli;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

import java.util.List;

public class NonNegativeRangeValidator implements IValueValidator<List<Integer>> {
    @Override
    public void validate(String name, List<Integer> list) throws ParameterException {
        if (list.size() > 2) {
            throw new ParameterException("Option [" + name + "] must have at most two values: " + list);
        }

        for (Integer integer : list) {
            if (integer < 0) {
                throw new ParameterException("Option [" + name + "] values must be non-negative integers: " + list);
            }
        }
    }
}
