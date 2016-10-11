package il.ac.huji.cs.itays04.games.api;

import il.ac.huji.cs.itays04.rational.NumberUtils;

import java.util.Optional;

public class GamePrices<W extends Number & Comparable<W>> {
    private final W socialOptimum;
    private final W priceOfSinking;
    private final Optional<W> priceOfAnarchy;
    private final Optional<W> priceOfStability;

    public GamePrices(W socialOptimum, W priceOfSinking, Optional<W> priceOfAnarchy, Optional<W> priceOfStability) {
        this.socialOptimum = socialOptimum;
        this.priceOfSinking = priceOfSinking;
        this.priceOfAnarchy = priceOfAnarchy;
        this.priceOfStability = priceOfStability;
    }

    public W getSocialOptimum() {
        return socialOptimum;
    }

    public W getPriceOfSinking() {
        return priceOfSinking;
    }

    public Optional<W> getPriceOfAnarchy() {
        return priceOfAnarchy;
    }

    public Optional<W> getPriceOfStability() {
        return priceOfStability;
    }

    @Override
    public String toString() {
        return '{' +
                "\n    \"socialOptimum\" : " + NumberUtils.format(socialOptimum.doubleValue()) +
                ",\n    \"priceOfSinking\" : " + NumberUtils.format(priceOfSinking.doubleValue()) +
                (priceOfAnarchy.isPresent() ? ",\n    \"priceOfAnarchy\" : " + NumberUtils.format(priceOfAnarchy.get().doubleValue()) : "") +
                (priceOfStability.isPresent() ? ",\n    \"priceOfStability\" : " + NumberUtils.format(priceOfStability.get().doubleValue()) : "") +
                "\n}";
    }
}
