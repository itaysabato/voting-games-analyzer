package il.ac.huji.cs.itays04.games.impl;

import il.ac.huji.cs.itays04.games.api.Game;
import il.ac.huji.cs.itays04.games.api.GameState;

import java.util.HashSet;
import java.util.Optional;
import java.util.function.Predicate;

public class SimpleGameTraverser {

    public <T extends GameState<T>> Optional<? extends T> traverseGameUntil(Game<T, ?, ?> game, HashSet<T> visited, Predicate<T> haltCondition) {
        HashSet<T> toVisit = new HashSet<>();
        toVisit.addAll(game.getTruthfulStates().keySet());

        while(!toVisit.isEmpty()) {
            final HashSet<T> nextToVisit = new HashSet<>();

            Optional<T> haltState = expand(game, toVisit, visited, nextToVisit, haltCondition);

            if (haltState.isPresent()) {
                return haltState;
            }

            toVisit = nextToVisit;
        }

        return Optional.empty();
    }

    private <T extends GameState<T>> Optional<T> expand(Game<T, ?, ?> game, HashSet<T> toVisit, HashSet<T> visited, HashSet<T> nextToVisit, Predicate<T> haltCondition) {
        for (T state : toVisit) {

            if (haltCondition.test(state)) {
                return Optional.of(state);
            }
            else {
                visited.add(state);

                game.getAllPossibleMoves(state)
                        .stream()
                        .sequential()
                        .filter(s -> !toVisit.contains(s) && !visited.contains(s))
                        .forEach(nextToVisit::add);
            }
        }

        return Optional.empty();
    }

}
