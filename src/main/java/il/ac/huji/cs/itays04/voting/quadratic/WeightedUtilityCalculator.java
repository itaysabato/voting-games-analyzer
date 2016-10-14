package il.ac.huji.cs.itays04.voting.quadratic;

import il.ac.huji.cs.itays04.games.api.UtilityCalculator;
import il.ac.huji.cs.itays04.rational.NumberUtils;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.*;
import java.util.stream.Collectors;

public class WeightedUtilityCalculator<C> implements UtilityCalculator<VotingGameState<C>, BigFraction> {
    private final boolean quadratic;
    private final Set<List<C>> truthfulProfiles;
    private final List<LinkedHashMap<C, BigFraction>> individualUtilities;

    public WeightedUtilityCalculator(List<LinkedHashMap<C, BigFraction>> individualUtilities, boolean quadratic) {
        this.quadratic = quadratic;
        this.individualUtilities = Collections.unmodifiableList(individualUtilities);

        final Set<List<C>> allTruthfulProfiles = getAllTruthfulProfiles(this.individualUtilities);
        this.truthfulProfiles = Collections.unmodifiableSet(allTruthfulProfiles);
    }

    private Set<List<C>> getAllTruthfulProfiles(List<? extends Map<C, BigFraction>> individualUtilities) {
        List<Set<C>> favorites = getFavorites(individualUtilities);
        return getProfilesFromFavorites(favorites);
    }

    private List<Set<C>> getFavorites(List<? extends Map<C, BigFraction>> individualUtilities) {
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
        final Map<C, BigFraction> distribution = getWinnerDistribution(gameState.getVotes());
        return calculateExpectedUtility(distribution, playerIndex);
    }

    public Map<C, BigFraction> getWinnerDistribution(List<C> votes) {
        final Map<C, BigFraction> histogram = calculateHistogram(votes);

        final BigFraction total;
        if (quadratic) {
            total = quadrifyHistogram(histogram);
        }
        else {
            total = new BigFraction(votes.size());
        }

        for (Map.Entry<C, BigFraction> entry : histogram.entrySet()) {
            entry.setValue(entry.getValue().divide(total));
        }
        return histogram;
    }

    private Map<C, BigFraction> calculateHistogram(List<C> votes) {
        final Map<C, BigFraction> histogram = new HashMap<>();

        for (C candidate : votes) {
            BigFraction count = histogram.getOrDefault(candidate, BigFraction.ZERO);
            histogram.put(candidate, count.add(1));
        }

        return histogram;
    }

    private BigFraction quadrifyHistogram(Map<C, BigFraction> histogram) {
        BigFraction total = BigFraction.ZERO;

        for (Map.Entry<C, BigFraction> entry : histogram.entrySet()) {
            BigFraction value = entry.getValue();
            BigFraction newValue = value.pow(2);

            entry.setValue(newValue);
            total = total.add(newValue);
        }

        return total;
    }

    private BigFraction calculateExpectedUtility(Map<C, BigFraction> distribution, int playerIndex) {
        final Map<C, BigFraction> utilities = individualUtilities.get(playerIndex);

        BigFraction expectancy = BigFraction.ZERO;

        for (Map.Entry<C, BigFraction> entry : distribution.entrySet()) {
            BigFraction util = utilities.get(entry.getKey());
            final BigFraction probability = entry.getValue();

            expectancy = expectancy.add(util.multiply(probability));
        }

        return expectancy;
    }

    public Set<List<C>> getTruthfulProfiles() {
        return truthfulProfiles;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(quadratic ? "Quadratic" : "Randomized Dictatorship")
                .append(" expected utility based on the following cardinal utilities:\n");

        for (int i = 0; i < individualUtilities.size(); i++) {
            final Map<C, BigFraction> utilities = individualUtilities.get(i);

            builder.append("Voter ")
                    .append(i + 1)
                    .append(": ")
                    .append(utilities.values()
                            .stream()
                            .map(NumberUtils::format)
                            .collect(Collectors.joining(", ")))
                    .append("\n");
        }

        return builder.toString();
    }
}
