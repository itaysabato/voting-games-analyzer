package il.ac.huji.cs.itays04.games.api;

import il.ac.huji.cs.itays04.utils.ImmutableDirectedGraphWithScc;

public interface GameAnalyzer {
    <T extends GameState, U extends Number & Comparable<U>>
    ImmutableDirectedGraphWithScc<T> calculateBestResponseGraph(Game<T, U, ?> game);

    <T extends GameState, W extends Number & Comparable<W>> GameAnalysis<T, W> analyze(
            Game<T, W, W> game,
            ImmutableDirectedGraphWithScc<T> bestResponseGraph);

}
