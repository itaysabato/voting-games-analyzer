package il.ac.huji.cs.itays04.games.api;

public interface GameAnalyzer<T extends GameState<T>> {
    BestResponseGraph<T> calculateBestResponseGraph(T initialState);

    <W extends Comparable<W>> GamePrices<W> getPrices(BestResponseGraph<T> brg, SocialWelfareCalculator<T,W> calculator);

}
