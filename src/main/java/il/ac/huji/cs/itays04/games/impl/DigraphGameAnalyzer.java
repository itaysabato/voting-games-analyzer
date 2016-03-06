package il.ac.huji.cs.itays04.games.impl;

import il.ac.huji.cs.itays04.games.api.*;

public class DigraphGameAnalyzer<T extends GameState<T>> implements GameAnalyzer<T> {
    @Override
    public GameStateGraph<T> calculateBestResponseGraph(Game<T> game, UtilityCalculator<T, ?> utilityCalculator) {
        return null;
    }

    @Override
    public <W extends Comparable<W>> GamePrices<W> getPrices(GameStateGraph<T> brg, SocialWelfareCalculator<T, W> calculator) {
        return null;
    }
}
