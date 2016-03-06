package il.ac.huji.cs.itays04.games.api;

import java.util.Set;

public interface SocialWelfareCalculator<T extends GameState<T>, W extends Comparable<W>> {
    W getRatio(W w1, W w2);
    W calculateWelfare(T gameState);
    W calculateAverageWelfare(Set<T> states);
}
