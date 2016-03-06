package il.ac.huji.cs.itays04.games.api;

import il.ac.huji.cs.itays04.utils.ImmutableDirectedGraphWithScc;

public interface GameAnalyzer {
    <T extends GameState<T>, U extends Comparable<U>> ImmutableDirectedGraphWithScc<T> calculateBestResponseGraph(
            Game<T> game,
            UtilityCalculator<T, U> utilityCalculator);

    <T extends GameState<T>, W extends Comparable<W>> GamePrices<W> getPrices(
            ImmutableDirectedGraphWithScc<T> bestResponseGraph,
            SocialWelfareCalculator<T,W> calculator);

}
