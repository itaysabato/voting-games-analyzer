package il.ac.huji.cs.itays04.rational;

import il.ac.huji.cs.itays04.games.api.GamePrices;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.Optional;

public class RationalAnalysisAggregation {
    private final int numberOfGames;
    private final BigFraction avgNeCount;
    private final BigFraction fractionWithNe;
    private final BigFraction convergingFraction;
    private final GamePrices<BigFraction> avgPrices;
    private final Optional<BigFraction> betterPoSThanDicFraction;

    public RationalAnalysisAggregation(
            int numberOfGames,
            BigFraction avgNeCount,
            BigFraction fractionWithNe,
            BigFraction convergingFraction,
            GamePrices<BigFraction> avgPrices,
            Optional<BigFraction> betterPoSThanDicFraction) {

        this.numberOfGames = numberOfGames;
        this.avgNeCount = avgNeCount;
        this.fractionWithNe = fractionWithNe;
        this.convergingFraction = convergingFraction;
        this.avgPrices = avgPrices;
        this.betterPoSThanDicFraction = betterPoSThanDicFraction;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public BigFraction getAvgNeCount() {
        return avgNeCount;
    }

    public BigFraction getFractionWithNe() {
        return fractionWithNe;
    }

    public BigFraction getConvergingFraction() {
        return convergingFraction;
    }

    public GamePrices<BigFraction> getAvgPrices() {
        return avgPrices;
    }

    public Optional<BigFraction> getBetterPoSThanDicFraction() {
        return betterPoSThanDicFraction;
    }

    @Override
    public String toString() {
        final Optional<BigFraction> priceOfAnarchy = avgPrices.getPriceOfAnarchy();
        final Optional<BigFraction> priceOfStability = avgPrices.getPriceOfStability();

        return "{" +
                "\n    \"numberOfGames\" : " + numberOfGames +
                ",\n    \"avgNeCount\" : " + NumberUtils.format(avgNeCount.doubleValue()) +
                ",\n    \"percentageWithNe\" : " + NumberUtils.format(fractionWithNe.percentageValue()) +
                ",\n    \"convergingPercentage\" : " + NumberUtils.format(convergingFraction.percentageValue()) +
                ",\n    \"avgPrices\" : {" +
                "\n        \"socialOptimum\" : " + NumberUtils.format(avgPrices.getSocialOptimum().doubleValue()) +
                ",\n        \"priceOfSinking\" : " + NumberUtils.format(avgPrices.getPriceOfSinking().doubleValue()) +
                (priceOfAnarchy.isPresent() ? ",\n        \"priceOfAnarchy\" : " + NumberUtils.format(priceOfAnarchy.get().doubleValue()) : "") +
                (priceOfStability.isPresent() ? ",\n        \"priceOfStability\" : " + NumberUtils.format(priceOfStability.get().doubleValue()) : "") +
                "\n    }" +
                (betterPoSThanDicFraction.isPresent() ?
                        ",\n    \"betterPoSThanRandomizedDictatorshipPercentage\" : " +
                                NumberUtils.format(betterPoSThanDicFraction.get().percentageValue())
                        : "") +
                "\n}";
    }
}
