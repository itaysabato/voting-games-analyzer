package il.ac.huji.cs.itays04.rational;

import il.ac.huji.cs.itays04.games.api.Game;
import il.ac.huji.cs.itays04.games.api.GameState;
import il.ac.huji.cs.itays04.games.api.SocialWelfareCalculator;
import il.ac.huji.cs.itays04.games.api.UtilityCalculator;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.Set;

public class BigFractionAverageSocialWelfareCalculator
        implements SocialWelfareCalculator<BigFraction, BigFraction> {

    @Override
    public BigFraction getRatio(BigFraction w1, BigFraction w2) {
        final BigFraction ratio;

        if (w2.equals(BigFraction.ZERO)) {
            if (w1.equals(BigFraction.ZERO)) {
                ratio = BigFraction.ONE;
            }
            else {
                //todo: got a better idea?
                ratio = new BigFraction(666);
            }
        }
        else if (w1.compareTo(BigFraction.ZERO) < 0) {
            ratio = w2.divide(w1);
        }
        else {
            ratio = w1.divide(w2);
        }

        return ratio;
    }

    @Override
    public <T extends GameState> BigFraction calculateWelfare(Game<T, BigFraction, BigFraction> game,
                                                              UtilityCalculator<T, BigFraction> utilityCalculator,
                                                              T gameState) {

        BigFraction sum = BigFraction.ZERO;

        for (int i = 0; i < game.getNumberOfPlayers(); i++) {
            final BigFraction utility = utilityCalculator.calculateUtility(gameState, i);
            sum = sum.add(utility);
        }

        return sum.divide(game.getNumberOfPlayers());
    }

    @Override
    public <T extends GameState> BigFraction calculateAverageWelfare(Game<T, BigFraction, BigFraction> game,
                                                                     UtilityCalculator<T, BigFraction> utilityCalculator,
                                                                     Set<T> states) {
        BigFraction sum = BigFraction.ZERO;

        for (T state : states) {
            final BigFraction welfare = calculateWelfare(game, utilityCalculator, state);
            sum = sum.add(welfare);
        }

        return sum.divide(states.size());
    }

    @Override
    public String toString() {
        return "Average";
    }
}
