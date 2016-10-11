package il.ac.huji.cs.itays04.rational;

import com.google.common.base.Preconditions;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.Objects;

public class RationalEntity {
    final BigFraction position;

    public RationalEntity(BigFraction position) {
        Preconditions.checkNotNull(position);
        this.position = position;
    }

    public BigFraction getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return NumberUtils.fractionToString(position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RationalEntity that = (RationalEntity) o;
        return Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}
