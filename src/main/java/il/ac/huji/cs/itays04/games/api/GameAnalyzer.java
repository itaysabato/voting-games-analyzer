package il.ac.huji.cs.itays04.games.api;

public interface GameAnalyzer<T extends GameState<T>> {
    GameStateGraph<T> calculateBestResponseGraph(Game<T> game, UtilityCalculator<T, ?> utilityCalculator);

    <W extends Comparable<W>> GamePrices<W> getPrices(GameStateGraph<T> bestResponseGraph, SocialWelfareCalculator<T,W> calculator);

}
