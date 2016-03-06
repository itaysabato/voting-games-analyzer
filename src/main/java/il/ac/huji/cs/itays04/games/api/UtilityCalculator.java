package il.ac.huji.cs.itays04.games.api;

import java.util.Collection;
import java.util.Optional;

public interface UtilityCalculator<T extends GameState<T>, U extends Comparable<U>> {

    U calculateUtility(T gameState, int playerIndex);

    default int compare(T s1, T s2, int playerIndex) {
        U u1 = calculateUtility(s1, playerIndex);
        U u2 = calculateUtility(s2, playerIndex);
        return u1.compareTo(u2);
    }

    default Optional<? extends T> getImprovement(Game<T> game, T state, final int playerIndex) {

        Collection<? extends T> moves = game.getPossibleMovesForPlayer(state, playerIndex);

        return moves.stream()
                .sequential()
                .filter(move -> compare(move, state, playerIndex) > 0)
                .findAny();
    }
}
