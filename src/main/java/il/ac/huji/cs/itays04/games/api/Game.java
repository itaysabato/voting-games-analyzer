package il.ac.huji.cs.itays04.games.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface Game<T extends GameState<T>, U extends Number & Comparable<U>, W extends Number & Comparable<W>> {
    int getNumberOfPlayers();

    Set<? extends T> getInitialStates();

    Set<? extends T> getPossibleMovesForPlayer(T state, int playerIndex);

    default Set<? extends T> getAllPossibleMoves(T state) {
        Set<T> gameStates = new HashSet<>();

        for (int i = 0; i < getNumberOfPlayers(); i++) {

            Collection<? extends T> possibleMovesForPlayer = getPossibleMovesForPlayer(state, i);
            gameStates.addAll(possibleMovesForPlayer);
        }

        return gameStates;
    }

    UtilityCalculator<T, U> getUtilityCalculator();

    SocialWelfareCalculator<T, U, W> getSocialWelfareCalculator();
}
