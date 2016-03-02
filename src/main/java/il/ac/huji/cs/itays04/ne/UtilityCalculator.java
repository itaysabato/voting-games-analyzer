package il.ac.huji.cs.itays04.ne;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

public interface UtilityCalculator<T extends GameState<T>> {

    BigDecimal calculateUtility(T gameState, int playerIndex);

    default int compare(T s1, T s2, int playerIndex) {
        BigDecimal u1 = calculateUtility(s1, playerIndex);
        BigDecimal u2 = calculateUtility(s2, playerIndex);
        return u1.compareTo(u2);
    }

    default Optional<? extends T> getImprovement(T state, final int playerIndex) {

        Collection<? extends T> moves = state.getPossibleMovesForPlayer(playerIndex);

        return moves.stream()
                .sequential()
                .filter(move -> compare(move, state, playerIndex) > 0)
                .findAny();
    }
}
