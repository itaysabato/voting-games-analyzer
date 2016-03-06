package il.ac.huji.cs.itays04.games.impl;

import il.ac.huji.cs.itays04.games.api.Game;
import il.ac.huji.cs.itays04.games.api.GameState;
import il.ac.huji.cs.itays04.games.api.NashEquilibriumFinder;
import il.ac.huji.cs.itays04.games.api.UtilityCalculator;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Optional;

public class SimpleNashEquilibriumFinder<T extends GameState<T>> implements NashEquilibriumFinder<T> {
    private long counter = 0;

    private final PrintStream printStream;
    private final SimpleGameTraverser simpleGameTraverser;
    private final UtilityCalculator<T, ?> utilityCalculator;

    public SimpleNashEquilibriumFinder(PrintStream printStream,
                                       SimpleGameTraverser simpleGameTraverser,
                                       UtilityCalculator<T, ?> utilityCalculator) {
        this.printStream = printStream;
        this.simpleGameTraverser = simpleGameTraverser;
        this.utilityCalculator = utilityCalculator;
    }

    @Override
    public Optional<? extends T> findNE(Game<T> game) {
        counter = 0;
        return simpleGameTraverser.traverseGameUntil(game, new HashSet<>(), s -> isNash(game, s));
    }

    private boolean isNash(Game<T> game, T state) {
        for (int i = 0; i < game.getNumberOfPlayers(); i++) {

            Optional<? extends T> improvement = utilityCalculator.getImprovement(game, state, i);

            if (improvement.isPresent()) {
                printStream.println((++counter) + " - Player "+ i + " can improve:\t" + state + "\t>>>>>\t" + improvement.get());
                return false;
            }
        }

        return true;
    }
}
