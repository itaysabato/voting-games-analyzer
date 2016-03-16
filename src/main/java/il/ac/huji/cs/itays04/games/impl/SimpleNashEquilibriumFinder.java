package il.ac.huji.cs.itays04.games.impl;

import il.ac.huji.cs.itays04.games.api.Game;
import il.ac.huji.cs.itays04.games.api.GameState;
import il.ac.huji.cs.itays04.games.api.NashEquilibriumFinder;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Optional;

public class SimpleNashEquilibriumFinder implements NashEquilibriumFinder {
    private long counter = 0;

    private final PrintStream printStream;
    private final SimpleGameTraverser simpleGameTraverser;

    public SimpleNashEquilibriumFinder(PrintStream printStream,
                                       SimpleGameTraverser simpleGameTraverser) {
        this.printStream = printStream;
        this.simpleGameTraverser = simpleGameTraverser;
    }

    @Override
    public <T extends GameState<T>, U extends Number & Comparable<U>> Optional<? extends T> findNE(Game<T, U, ?> game) {
        counter = 0;
        return simpleGameTraverser.traverseGameUntil(game, new HashSet<>(), s -> isNash(game, s));
    }

    private <T extends GameState<T>, U extends Number & Comparable<U>> boolean isNash(Game<T, U, ?> game, T state) {
        for (int i = 0; i < game.getNumberOfPlayers(); i++) {

            Optional<? extends T> improvement = game.getUtilityCalculator().getImprovement(game, state, i);

            if (improvement.isPresent()) {
                printStream.println((++counter) + " - Player "+ i + " can improve:\t" + state + "\t>>>>>\t" + improvement.get());
                return false;
            }
        }

        return true;
    }
}
