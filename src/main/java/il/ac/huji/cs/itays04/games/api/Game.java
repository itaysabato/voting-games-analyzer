package il.ac.huji.cs.itays04.games.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface Game<T extends GameState, U extends Number & Comparable<U>, W extends Number & Comparable<W>> {
    int getNumberOfPlayers();

    Map<? extends T, ? extends W> getTruthfulStates();

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

    SocialWelfareCalculator<U, W> getSocialWelfareCalculator();
}
