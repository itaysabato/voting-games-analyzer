package il.ac.huji.cs.itays04.games.impl;

import il.ac.huji.cs.itays04.games.api.Game;
import il.ac.huji.cs.itays04.games.api.GameState;
import il.ac.huji.cs.itays04.games.api.NashEquilibriumFinder;

import java.util.HashSet;
import java.util.Optional;

public class SimpleNashEquilibriumFinder implements NashEquilibriumFinder {
    private final SimpleGameTraverser simpleGameTraverser;

    public SimpleNashEquilibriumFinder(SimpleGameTraverser simpleGameTraverser) {
        this.simpleGameTraverser = simpleGameTraverser;
    }

    @Override
    public <T extends GameState<T>, U extends Number & Comparable<U>> Optional<? extends T> findNE(Game<T, U, ?> game) {
        return simpleGameTraverser.traverseGameUntil(game, new HashSet<>(), s -> isNash(game, s));
    }

    private <T extends GameState<T>, U extends Number & Comparable<U>> boolean isNash(Game<T, U, ?> game, T state) {
        Optional<? extends T> improvement = game.getUtilityCalculator().getImprovement(game, state);
        return !improvement.isPresent();
    }
}
