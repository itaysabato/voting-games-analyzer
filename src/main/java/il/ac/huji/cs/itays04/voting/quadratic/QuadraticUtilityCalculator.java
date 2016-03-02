package il.ac.huji.cs.itays04.voting.quadratic;

import il.ac.huji.cs.itays04.ne.UtilityCalculator;
import il.ac.huji.cs.itays04.voting.VotingState;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuadraticUtilityCalculator<C> implements UtilityCalculator<VotingState<C>> {
    private final List<Map<C, Integer>> individualUtilities;

    public QuadraticUtilityCalculator(List<Map<C, Integer>> individualUtilities) {
        this.individualUtilities = individualUtilities;
    }

    @Override
    public BigDecimal calculateUtility(VotingState<C> gameState, int player) {
        final Map<C, Integer> histogram = calculateHistogram(gameState);

        int total = QuadrifyHistogram(histogram);

        return calculateExpectedUtility(player, histogram, total);
    }

    private Map<C, Integer> calculateHistogram(VotingState<C> gameState) {
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

    public BigDecimal calculateExpectedUtility(int playerIndex, Map<C, Integer> weightsMap, int totalWeight) {
        final Map<C, Integer> utilities = individualUtilities.get(playerIndex);

        BigDecimal totalDecimal = new BigDecimal(totalWeight);
        BigDecimal expectedUtility = new BigDecimal(0);

        for (Map.Entry<C, Integer> entry : weightsMap.entrySet()) {
            Integer util = utilities.get(entry.getKey());
            Integer weight = entry.getValue();
            BigDecimal weightTimesUtility = new BigDecimal(util * weight);

            BigDecimal addition = weightTimesUtility.divide(totalDecimal, 20, RoundingMode.DOWN);

            expectedUtility = expectedUtility.add(addition);
        }

        return expectedUtility;
    }
}
