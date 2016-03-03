package il.ac.huji.cs.itays04.games;

import java.util.Optional;

public interface NashEquilibriumFinder<T extends GameState<T>> {
    Optional<? extends T> findNE(T initialState);
}
