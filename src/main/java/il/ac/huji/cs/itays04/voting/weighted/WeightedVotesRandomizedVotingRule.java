package il.ac.huji.cs.itays04.voting.weighted;

import il.ac.huji.cs.itays04.voting.RandomizedVotingRule;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class WeightedVotesRandomizedVotingRule implements RandomizedVotingRule {

    /**
     * Calculates the (non-negative!) weight to be applied to the current voteCount of the given candidate.
     * @param candidate The candidate to get the weight for.
     * @param voteCount The vote count for the given candidate.
     * @return A non-negative rational weight.
     */
    protected abstract <C> BigFraction getWeight(C candidate, BigFraction voteCount);

    @Override
    public String toString() {
        return getClass().getName();
    }

    @Override
    public <C> Map<C, BigFraction> getWinnerDistribution(List<C> votes, Set<C> candidatesSet) {
        final Map<C, BigFraction> histogram = calculateHistogram(votes);
        final BigFraction totalWeight = applyWeights(histogram);
        return normalize(histogram, totalWeight);
    }

    public <C> Map<C, BigFraction> normalize(Map<C, BigFraction> weightsMap, BigFraction totalWeight) {
        for (Map.Entry<C, BigFraction> entry : weightsMap.entrySet()) {
            final BigFraction normalizedValue = entry.getValue().divide(totalWeight);
            entry.setValue(normalizedValue);
        }

        return weightsMap;
    }

    private <C> Map<C, BigFraction> calculateHistogram(List<C> votes) {
        final Map<C, BigFraction> histogram = new HashMap<>();

        for (C candidate : votes) {
            BigFraction count = histogram.getOrDefault(candidate, BigFraction.ZERO);
            histogram.put(candidate, count.add(1));
        }

        return histogram;
    }

    private <C> BigFraction applyWeights(Map<C, BigFraction> histogram) {
        BigFraction total = BigFraction.ZERO;

        for (Map.Entry<C, BigFraction> entry : histogram.entrySet()) {
            final C candidate = entry.getKey();
            BigFraction voteCount = entry.getValue();

            final BigFraction weight = getWeight(candidate, voteCount);
            if (weight.compareTo(BigFraction.ZERO) < 0) {
                throw new IllegalStateException("Got negative weight for candidate [" + candidate + "] " +
                        "with vote count [" + voteCount + "].");
            }

            BigFraction newValue = voteCount.multiply(weight);

            entry.setValue(newValue);
            total = total.add(newValue);
        }

        return total;
    }
}
