package il.ac.huji.cs.itays04.rational;

import il.ac.huji.cs.itays04.games.api.GameAnalysis;
import il.ac.huji.cs.itays04.games.api.GamePrices;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.Optional;

public class RationalAggregator {
    private RationalAnalysisAggregation currentAggregation = new RationalAnalysisAggregation(
            0,
            BigFraction.ZERO,
            BigFraction.ZERO,
            BigFraction.ZERO,
            new GamePrices<>(
                    BigFraction.ZERO,
                    BigFraction.ZERO,
                    Optional.empty(),
                    Optional.empty())
    );

    public RationalAnalysisAggregation getCurrentAggregation() {
        return currentAggregation;
    }

    public synchronized void add(GameAnalysis<?, BigFraction> gameAnalysis) {
        currentAggregation = add(currentAggregation, gameAnalysis);
    }

    private RationalAnalysisAggregation add(RationalAnalysisAggregation aggregation, GameAnalysis<?, BigFraction> gameAnalysis) {
        final int numberOfGames = aggregation.getNumberOfGames();

        return new RationalAnalysisAggregation(
                numberOfGames + 1,
                avg(numberOfGames, aggregation.getAvgNeCount(), gameAnalysis.getNeCount()),
                avg(numberOfGames, aggregation.getFractionWithNe(), gameAnalysis.getNeCount() > 0 ? 1 : 0),
                avg(numberOfGames, aggregation.getConvergingFraction(), gameAnalysis.getNumberOfNonSingularSccs() == 0 ? 1 : 0),
                avg(aggregation, gameAnalysis.getPrices())
        );
    }

    private GamePrices<BigFraction> avg(RationalAnalysisAggregation aggregation, GamePrices<BigFraction> newValue) {
        final int numberOfGames = aggregation.getNumberOfGames();
        final GamePrices<BigFraction> currentAvg = aggregation.getAvgPrices();
        final BigFraction numberOfGamesWithNe = aggregation.getFractionWithNe().multiply(numberOfGames);

        return new GamePrices<>(
                avg(numberOfGames, currentAvg.getSocialOptimum(), newValue.getSocialOptimum()),
                avg(numberOfGames, currentAvg.getPriceOfSinking(), newValue.getPriceOfSinking()),
                avg(numberOfGamesWithNe, currentAvg.getPriceOfAnarchy(), newValue.getPriceOfAnarchy()),
                avg(numberOfGamesWithNe, currentAvg.getPriceOfStability(), newValue.getPriceOfStability())
        );
    }

    private Optional<BigFraction> avg(BigFraction currentWeight, Optional<BigFraction> currentValue, Optional<BigFraction> newValue) {
        if (!currentValue.isPresent()) {
            return newValue;
        }
        else if (!newValue.isPresent()) {
            return currentValue;
        }
        else {
            final BigFraction avg = avg(currentWeight, currentValue.get(), newValue.get());
            return Optional.of(avg);
        }
    }

    private BigFraction avg(int currentWeight, BigFraction currentAvg, long newValue) {
        return avg(currentWeight, currentAvg, new BigFraction(newValue));
    }

    private BigFraction avg(int currentWeight, BigFraction currentAvg, BigFraction newValue) {
        return avg(new BigFraction(currentWeight), currentAvg, newValue);
    }

    private BigFraction avg(BigFraction currentWeight, BigFraction currentAvg, BigFraction newValue) {
        return currentAvg.multiply(currentWeight).add(newValue).divide(currentWeight.add(1));
    }
}
