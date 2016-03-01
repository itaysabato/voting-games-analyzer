package il.ac.huji.cs.itays04.ne;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Expected to implement equals, hashcode and toString in a meaningful way.
 * @param <T>
 */
public interface GameState<T extends GameState<T>> {

    int getNumberOfPlayers();

    Collection<? extends T> getPossibleMovesForPlayer(int index);

    default Collection<? extends T> getAllPossibleMoves() {
        LinkedList<T> gameStates = new LinkedList<>();

        for (int i = 0; i < getNumberOfPlayers(); i++) {

            Collection<? extends T> possibleMovesForPlayer = getPossibleMovesForPlayer(i);
            gameStates.addAll(possibleMovesForPlayer);
        }

        return gameStates;
    }

}
