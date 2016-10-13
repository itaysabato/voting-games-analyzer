package il.ac.huji.cs.itays04.games.api;

import java.util.Optional;

public interface NashEquilibriumFinder {
    <T extends GameState, U extends Number & Comparable<U>> Optional<? extends T> findNE(Game<T, U, ?> game);
}
