package il.ac.huji.cs.itays04.games.api;

import java.util.Set;

public interface SocialWelfareCalculator<U extends Number & Comparable<U>, W extends Number & Comparable<W>> {
    W getRatio(W w1, W w2);

    <T extends GameState> W calculateWelfare(Game<T, U, W> game, UtilityCalculator<T, U> utilityCalculator, T gameState);

    default <T extends GameState>  W calculateWelfare(Game<T, U, W> game, T gameState) {
        return calculateWelfare(game, game.getUtilityCalculator(), gameState);
    }

    <T extends GameState>  W calculateAverageWelfare(Game<T, U, W> game, UtilityCalculator<T, U> utilityCalculator, Set<T> states);

    default <T extends GameState>  W calculateAverageWelfare(Game<T, U, W> game, Set<T> states) {
        return calculateAverageWelfare(game, game.getUtilityCalculator(), states);
    }
}
