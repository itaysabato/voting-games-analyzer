package il.ac.huji.cs.itays04.cli.parsing;

import com.beust.jcommander.ParameterDescription;

import java.lang.reflect.AccessibleObject;
import java.util.Comparator;

public class PriorityComparator implements Comparator<ParameterDescription> {
    public static PriorityComparator instance = new PriorityComparator();

    @Override
    public int compare(ParameterDescription pd1, ParameterDescription pd2) {
        return getPriority(pd1) - getPriority(pd2);
    }

    private short getPriority(ParameterDescription pd) {
        AccessibleObject member = getAnnotated(pd);

        final ExtendedParameter extendedParameter = member.getAnnotation(ExtendedParameter.class);
        return extendedParameter == null ?
                Short.MAX_VALUE : extendedParameter.priority();
    }

    private AccessibleObject getAnnotated(ParameterDescription pd) {
        final String name = pd.getParameterized().getName();

        AccessibleObject member;
        try {
            member = Arguments.class.getDeclaredField(name);
        }
        catch (NoSuchFieldException e) {
            try {
                member = Arguments.class.getMethod(name);
            }
            catch (NoSuchMethodException e1) {
                throw new IllegalStateException("Arguments member [" + name + "] does not exist.", e);
            }
        }
        return member;
    }
}
