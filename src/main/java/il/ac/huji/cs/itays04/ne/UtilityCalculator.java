package il.ac.huji.cs.itays04.ne;

import java.math.BigDecimal;

public interface UtilityCalculator<T extends GameState<T>> {

    BigDecimal calculateUtility(T gameState, int player);

    default int compare(T s1, T s2, int player) {
        BigDecimal u1 = calculateUtility(s1, player);
        BigDecimal u2 = calculateUtility(s2, player);
        return u1.compareTo(u2);
    }
}
