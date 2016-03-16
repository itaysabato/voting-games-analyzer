package il.ac.huji.cs.itays04.games.api;

import java.util.Set;

public interface SocialWelfareCalculator<T extends GameState<T>, U extends Number & Comparable<U>, W extends Number & Comparable<W>> {
    W getRatio(W w1, W w2);
    W calculateWelfare(Game<T, U, W> game, T gameState);
    W calculateAverageWelfare(Game<T, U, W> game, Set<T> states);
}
