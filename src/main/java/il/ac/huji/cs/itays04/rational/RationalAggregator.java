package il.ac.huji.cs.itays04.rational;

import il.ac.huji.cs.itays04.games.api.GameAnalysis;
import il.ac.huji.cs.itays04.games.api.GamePrices;
import il.ac.huji.cs.itays04.voting.quadratic.AnalysisWithRandomDicComparison;
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
                    Optional.empty()),
            Optional.empty());

    public RationalAnalysisAggregation getCurrentAggregation() {
        return currentAggregation;
    }

    public synchronized void add(AnalysisWithRandomDicComparison<?, BigFraction> gameAnalysis) {
        currentAggregation = add(currentAggregation, gameAnalysis);
    }

    private RationalAnalysisAggregation add(RationalAnalysisAggregation aggregation, AnalysisWithRandomDicComparison<?, BigFraction> analysisWithComparison) {
        final int numberOfGames = aggregation.getNumberOfGames();
        GameAnalysis<?, BigFraction> gameAnalysis = analysisWithComparison.getGameAnalysis();

        return new RationalAnalysisAggregation(
                numberOfGames + 1,
                avg(numberOfGames, aggregation.getAvgNeCount(), gameAnalysis.getNeCount()),
                avg(numberOfGames, aggregation.getFractionWithNe(), gameAnalysis.getNeCount() > 0 ? 1 : 0),
                avg(numberOfGames, aggregation.getConvergingFraction(), gameAnalysis.getNumberOfNonSingularSccs() == 0 ? 1 : 0),
                avg(aggregation, gameAnalysis.getPrices()),
                avgBetterThanDic(aggregation, analysisWithComparison));
    }

    private Optional<BigFraction> avgBetterThanDic(RationalAnalysisAggregation aggregation, AnalysisWithRandomDicComparison<?, BigFraction> analysisWithComparison) {
        final Optional<BigFraction> randomDicPoSRatio = analysisWithComparison.getRandomDicPoSRatio();

        Optional<BigFraction> newValue = randomDicPoSRatio.isPresent() ?
                Optional.of(randomDicPoSRatio.get().compareTo(BigFraction.ONE) <= 0 ? BigFraction.ONE : BigFraction.ZERO)
                : Optional.empty();

        return avg(withNeCount(aggregation), aggregation.getBetterPoSThanDicFraction(), newValue);
    }

    private GamePrices<BigFraction> avg(RationalAnalysisAggregation aggregation, GamePrices<BigFraction> newValue) {
        final int numberOfGames = aggregation.getNumberOfGames();
        final GamePrices<BigFraction> currentAvg = aggregation.getAvgPrices();
        final BigFraction numberOfGamesWithNe = withNeCount(aggregation);

        return new GamePrices<>(
                avg(numberOfGames, currentAvg.getSocialOptimum(), newValue.getSocialOptimum()),
                avg(numberOfGames, currentAvg.getPriceOfSinking(), newValue.getPriceOfSinking()),
                avg(numberOfGamesWithNe, currentAvg.getPriceOfAnarchy(), newValue.getPriceOfAnarchy()),
                avg(numberOfGamesWithNe, currentAvg.getPriceOfStability(), newValue.getPriceOfStability())
        );
    }

    private BigFraction withNeCount(RationalAnalysisAggregation aggregation) {
        final int nGames = aggregation.getNumberOfGames();
        return aggregation.getFractionWithNe().multiply(nGames);
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
