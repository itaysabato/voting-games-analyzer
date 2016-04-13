package il.ac.huji.cs.itays04.voting.quadratic;

import il.ac.huji.cs.itays04.games.api.UtilityCalculator;
import il.ac.huji.cs.itays04.utils.NumberUtils;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import org.apache.commons.math3.fraction.BigFraction;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class WeightedUtilityCalculator<C> implements UtilityCalculator<VotingGameState<C>, BigFraction> {
    private final boolean quadratic;
    private final Set<List<C>> truthfulProfiles;
    private final List<Map<C, BigFraction>> individualUtilities;

    WeightedUtilityCalculator(List<Map<C, BigFraction>> individualUtilities) {
        this(individualUtilities, true);
    }

    WeightedUtilityCalculator(List<Map<C, BigFraction>> individualUtilities, boolean quadratic) {
        this.quadratic = quadratic;
        this.individualUtilities = Collections.unmodifiableList(individualUtilities);

        final Set<List<C>> allTruthfulProfiles = getAllTruthfulProfiles(this.individualUtilities);
        this.truthfulProfiles = Collections.unmodifiableSet(allTruthfulProfiles);
    }

    private Set<List<C>> getAllTruthfulProfiles(List<Map<C, BigFraction>> individualUtilities) {
        List<Set<C>> favorites = getFavorites(individualUtilities);
        return getProfilesFromFavorites(favorites);
    }

    private List<Set<C>> getFavorites(List<Map<C, BigFraction>> individualUtilities) {
        List<Set<C>> favorites = new ArrayList<>(individualUtilities.size());

        for (Map<C, BigFraction> utilities : individualUtilities) {
            final Optional<BigFraction> max = utilities.entrySet()
                    .stream()
                    .map(Map.Entry::getValue)
                    .max(Comparator.naturalOrder());

            assert max.isPresent();

            final Set<C> currentFavorites = utilities.entrySet()
                    .stream()
                    .filter(e -> e.getValue().equals(max.get()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());

            favorites.add(currentFavorites);
        }

        return favorites;
    }

    private Set<List<C>> getProfilesFromFavorites(List<Set<C>> favorites) {
        if (favorites.isEmpty()) {
            final HashSet<List<C>> emptyProfile = new HashSet<>();
            emptyProfile.add(Collections.emptyList());
            return emptyProfile;
        }

        final List<Set<C>> tail = favorites.subList(1, favorites.size());
        final Set<List<C>> subProfiles = getProfilesFromFavorites(tail);

        return favorites.get(0)
                .stream()
                .flatMap(c -> subProfiles.stream()
                        .map(l -> {
                            final List<C> newList = new ArrayList<>(l.size() + 1);
                            newList.add(c);
                            newList.addAll(l);
                            return newList;
                        }))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public BigFraction calculateUtility(VotingGameState<C> gameState, int playerIndex) {
        final Map<C, Integer> histogram = calculateHistogram(gameState);

        final int total;
        if (quadratic) {
            total = QuadrifyHistogram(histogram);
        }
        else {
            total = gameState.getVotes().size();
        }

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
        final Map<C, BigFraction> utilities = individualUtilities.get(playerIndex);

        BigFraction numerator = BigFraction.ZERO;

        for (Map.Entry<C, Integer> entry : weightsMap.entrySet()) {
            BigFraction util = utilities.get(entry.getKey());

            long weight = entry.getValue();
            final BigInteger bigWeight = BigInteger.valueOf(weight);

            numerator = numerator.add(util.multiply(bigWeight));
        }

        return numerator.divide(totalWeight);
    }

    public Set<List<C>> getTruthfulProfiles() {
        return truthfulProfiles;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("Quadratic expected utility based on individual preferences:\n");

        for (int i = 0; i < individualUtilities.size(); i++) {
            final Map<C, BigFraction> utilities = individualUtilities.get(i);

            for (Map.Entry<C, BigFraction> entry : utilities.entrySet()) {
                builder.append("U(V")
                        .append(i)
                        .append(",")
                        .append(entry.getKey())
                        .append(") = ")
                        .append(NumberUtils.fractionToString(entry.getValue()))
                        .append("; ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}
