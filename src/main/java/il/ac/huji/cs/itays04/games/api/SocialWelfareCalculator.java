package il.ac.huji.cs.itays04.games.api;

import java.util.Set;

public interface SocialWelfareCalculator<T extends GameState<T>, U extends Number & Comparable<U>, W extends Number & Comparable<W>> {
    W getRatio(W w1, W w2);

    W calculateWelfare(Game<T, U, W> game, UtilityCalculator<T, U> utilityCalculator, T gameState);

    default W calculateWelfare(Game<T, U, W> game, T gameState) {
        return calculateWelfare(game, game.getUtilityCalculator(), gameState);
    }

    W calculateAverageWelfare(Game<T, U, W> game, UtilityCalculator<T, U> utilityCalculator, Set<T> states);

    default W calculateAverageWelfare(Game<T, U, W> game, Set<T> states) {
        return calculateAverageWelfare(game, game.getUtilityCalculator(), states);
    }
}
