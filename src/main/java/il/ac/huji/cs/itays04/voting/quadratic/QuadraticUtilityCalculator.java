package il.ac.huji.cs.itays04.voting.quadratic;

import il.ac.huji.cs.itays04.games.UtilityCalculator;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import org.apache.commons.math3.fraction.BigFraction;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuadraticUtilityCalculator<C> implements UtilityCalculator<VotingGameState<C>, BigFraction> {
    private final List<Map<C, Integer>> individualUtilities;

    public QuadraticUtilityCalculator(List<Map<C, Integer>> individualUtilities) {
        this.individualUtilities = individualUtilities;
    }

    @Override
    public BigFraction calculateUtility(VotingGameState<C> gameState, int playerIndex) {
        final Map<C, Integer> histogram = calculateHistogram(gameState);

        int total = QuadrifyHistogram(histogram);

        return calculateExpectedUtility(playerIndex, histogram, total);
    }

    private Map<C, Integer> calculateHistogram(VotingGameState<C> gameState) {
        final Map<C, Integer> histogram = new HashMap<>();

        for (int i = 0; i < gameState.getNumberOfPlayers(); i++) {
            C candidate = gameState.getVote(i);
            Integer count = histogram.getOrDefault(candidate, 0);

            histogram.put(candidate, count + 1);
        }

        return histogram;
    }

    public int QuadrifyHistogram(Map<C, Integer> histogram) {
        int total = 0;

        for (Map.Entry<C, Integer> entry : histogram.entrySet()) {
            int value = entry.getValue();
            int newValue = value * value;

            entry.setValue(newValue);
            total += newValue;
        }

        return total;
    }

    public BigFraction calculateExpectedUtility(int playerIndex, Map<C, Integer> weightsMap, int totalWeight) {
        final Map<C, Integer> utilities = individualUtilities.get(playerIndex);

        BigInteger numerator = BigInteger.ZERO;

        for (Map.Entry<C, Integer> entry : weightsMap.entrySet()) {
            long util = utilities.get(entry.getKey());
            long weight = entry.getValue();

            numerator = numerator.add(BigInteger.valueOf(util * weight));
        }

        final BigFraction expectedDistance = new BigFraction(numerator, BigInteger.valueOf(totalWeight));

        return expectedDistance.negate();
    }
}
