package il.ac.huji.cs.itays04.games.api;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.Set;

public class BigFractionAverageSocialWelfareCalculator<T extends GameState<T>> implements SocialWelfareCalculator<T, BigFraction> {
    private final UtilityCalculator<T, BigFraction> utilityCalculator;

    public BigFractionAverageSocialWelfareCalculator(UtilityCalculator<T, BigFraction> utilityCalculator) {
        this.utilityCalculator = utilityCalculator;
    }

    @Override
    public BigFraction getRatio(BigFraction w1, BigFraction w2) {
        final BigFraction ratio;

        if (w2.equals(BigFraction.ZERO)) {
            if (w1.equals(BigFraction.ZERO)) {
                ratio = BigFraction.ONE;
            }
            else {
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
    public BigFraction calculateWelfare(Game<T> game, T gameState) {
        BigFraction sum = BigFraction.ZERO;

        for (int i = 0; i < game.getNumberOfPlayers(); i++) {
            final BigFraction utility = utilityCalculator.calculateUtility(gameState, i);
            sum = sum.add(utility);
        }

        return sum.divide(game.getNumberOfPlayers());
    }

    @Override
    public BigFraction calculateAverageWelfare(Game<T> game, Set<T> states) {
        BigFraction sum = BigFraction.ZERO;

        for (T state : states) {
            final BigFraction welfare = calculateWelfare(game, state);
            sum = sum.add(welfare);
        }

        return sum.divide(states.size());
    }
}
