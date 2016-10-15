package il.ac.huji.cs.itays04.cli.parsing;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD, METHOD })
public @interface ExtendedParameter {
    short priority() default 0;
}
