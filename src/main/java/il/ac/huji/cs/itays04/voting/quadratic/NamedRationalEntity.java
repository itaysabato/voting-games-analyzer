package il.ac.huji.cs.itays04.voting.quadratic;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Objects;

public class NamedRationalEntity extends RationalEntity {
    private final String name;

    public NamedRationalEntity(BigFraction position, String name) {
        super(position);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " = " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NamedRationalEntity that = (NamedRationalEntity) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
