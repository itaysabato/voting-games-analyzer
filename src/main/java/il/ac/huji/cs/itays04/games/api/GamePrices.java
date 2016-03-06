package il.ac.huji.cs.itays04.games.api;

import com.google.common.base.MoreObjects;

public class GamePrices<W extends Comparable<W>> {
    private final W socialOptimum;
    private final W priceOfAnarchy;
    private final W priceOfSinking;
    private final W priceOfStability;

    public GamePrices(W socialOptimum, W priceOfAnarchy, W priceOfSinking, W priceOfStability) {
        this.socialOptimum = socialOptimum;
        this.priceOfAnarchy = priceOfAnarchy;
        this.priceOfSinking = priceOfSinking;
        this.priceOfStability = priceOfStability;
    }

    public W getSocialOptimum() {
        return socialOptimum;
    }

    public W getPriceOfAnarchy() {
        return priceOfAnarchy;
    }

    public W getPriceOfSinking() {
        return priceOfSinking;
    }

    public W getPriceOfStability() {
        return priceOfStability;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("socialOptimum", socialOptimum)
                .add("priceOfAnarchy", priceOfAnarchy)
                .add("priceOfSinking", priceOfSinking)
                .add("priceOfStability", priceOfStability)
                .toString();
    }
}
