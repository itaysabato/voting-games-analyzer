package il.ac.huji.cs.itays04.ne;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class SimpleNashEquilibriumFinder<T extends GameState<T>> implements NashEquilibriumFinder<T> {
    private final PrintStream printStream;
    private final UtilityCalculator<T> utilityCalculator;

    public SimpleNashEquilibriumFinder(PrintStream printStream, UtilityCalculator<T> utilityCalculator) {
        this.printStream = printStream;
        this.utilityCalculator = utilityCalculator;
    }

    @Override
    public Optional<? extends T> findNE(T initialState) {
        HashSet<T> toVisit = new HashSet<>();
        toVisit.add(initialState);

        final HashSet<T> visited = new HashSet<>();

        while(!toVisit.isEmpty()) {
            final HashSet<T> nextToVisit = new HashSet<>();

            Optional<T> ne = expand(toVisit, visited, nextToVisit);

            if (ne.isPresent()) {
                return ne;
            }

            toVisit = nextToVisit;
        }

        return Optional.empty();
    }

    private Optional<T> expand(HashSet<T> toVisit, HashSet<T> visited, HashSet<T> nextToVisit) {
        for (T state : toVisit) {

            if (isNash(state)) {
                return Optional.of(state);
            }
            else {
                visited.add(state);

                state.getAllPossibleMoves()
                        .stream()
                        .filter(s -> !visited.contains(s))
                        .forEach(nextToVisit::add);
            }
        }

        return Optional.empty();
    }

    private boolean isNash(T state) {
        for (int i = 0; i < state.getNumberOfPlayers(); i++) {
            Collection<? extends T> moves = state.getPossibleMovesForPlayer(i);

            final int finalI = i;
            Optional<? extends T> improvement = moves.stream()
                    .filter(m -> utilityCalculator.compare(m, state, finalI) > 0)
                    .findAny();

            if (improvement.isPresent()) {
                printStream.println("Player "+ i + " can improve:\t" + state + "\t>>>>>\t" + improvement);
                return false;
            }
        }

        return true;
    }
}
