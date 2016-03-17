package il.ac.huji.cs.itays04.voting.quadratic;

import il.ac.huji.cs.itays04.games.api.UtilityCalculator;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import org.apache.commons.math3.fraction.BigFraction;

import java.math.BigInteger;
import java.util.*;

public class QuadraticUtilityCalculator<C> implements UtilityCalculator<VotingGameState<C>, BigFraction> {
    private final List<C> truthfulProfile;
    private final List<Map<C, Long>> individualUtilities;

    public QuadraticUtilityCalculator(List<Map<C, Long>> individualUtilities) {
        this.individualUtilities = Collections.unmodifiableList(individualUtilities);

        truthfulProfile = new ArrayList<>(individualUtilities.size());

        for (Map<C, Long> utilities : individualUtilities) {
            final C favorite = utilities.entrySet()
                    .stream()
                    .max(Comparator.comparingLong(Map.Entry::getValue))
                    .get()
                    .getKey();

            truthfulProfile.add(favorite);
        }
    }

    @Override
    public BigFraction calculateUtility(VotingGameState<C> gameState, int playerIndex) {
        final Map<C, Integer> histogram = calculateHistogram(gameState);

        int total = QuadrifyHistogram(histogram);

        return calculateExpectedUtility(playerIndex, histogram, total);
    }

    private Map<C, Integer> calculateHistogram(VotingGameState<C> gameState) {
        final List<C> votes = gameState.getVotes();
        final Map<C, Integer> histogram = new HashMap<>();

        for (C candidate : votes) {
            Integer count = histogram.getOrDefault(candidate, 0);
            histogram.put(candidate, count + 1);
        }

        return histogram;
    }

    private int QuadrifyHistogram(Map<C, Integer> histogram) {
        int total = 0;

        for (Map.Entry<C, Integer> entry : histogram.entrySet()) {
            int value = entry.getValue();
            int newValue = value * value;

            entry.setValue(newValue);
            total += newValue;
        }

        return total;
    }

    private BigFraction calculateExpectedUtility(int playerIndex, Map<C, Integer> weightsMap, int totalWeight) {
        final Map<C, Long> utilities = individualUtilities.get(playerIndex);

        BigInteger numerator = BigInteger.ZERO;

        for (Map.Entry<C, Integer> entry : weightsMap.entrySet()) {
            long util = utilities.get(entry.getKey());
            final BigInteger bigUtil = BigInteger.valueOf(util);

            long weight = entry.getValue();
            final BigInteger bigWeight = BigInteger.valueOf(weight);

            numerator = numerator.add(bigUtil.multiply(bigWeight));
        }

        return new BigFraction(numerator, BigInteger.valueOf(totalWeight));
    }

    public List<C> getTruthfulProfile() {
        return truthfulProfile;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("Quadratic expected utility based on individual preferences:\n");

        for (int i = 0; i < individualUtilities.size(); i++) {
            final Map<C, Long> utilities = individualUtilities.get(i);

            for (Map.Entry<C, Long> entry : utilities.entrySet()) {
                builder.append("U(")
                        .append(i)
                        .append(",")
                        .append(entry.getKey())
                        .append(") = ")
                        .append(entry.getValue())
                        .append("; ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}
