package il.ac.huji.cs.itays04.voting;

import il.ac.huji.cs.itays04.games.api.UtilityCalculator;
import il.ac.huji.cs.itays04.rational.NumberUtils;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.*;
import java.util.stream.Collectors;

public class ExpectedUtilityCalculator<C> implements UtilityCalculator<VotingGameState<C>, BigFraction> {
    private final Set<C> allCandidates;
    private final Set<List<C>> truthfulProfiles;
    private final RandomizedVotingRule votingRule;
    private final List<LinkedHashMap<C, BigFraction>> individualUtilities;

    public ExpectedUtilityCalculator(
            List<LinkedHashMap<C, BigFraction>> individualUtilities,
            RandomizedVotingRule votingRule) {

        this.votingRule = votingRule;
        this.individualUtilities = Collections.unmodifiableList(individualUtilities);
        allCandidates = Collections.unmodifiableSet(individualUtilities.get(0).keySet());

        final Set<List<C>> allTruthfulProfiles = getAllTruthfulProfiles(individualUtilities);
        this.truthfulProfiles = Collections.unmodifiableSet(allTruthfulProfiles);
    }

    /**
     * Calculates the expected utility for the playerIndex-th voter, given the game state.
     * @param gameState The game state.
     * @param playerIndex The player/voter index (zero-based).
     * @return The expected utility for the playerIndex-th voter.
     * @throws IllegalStateException If child class does not provide a well-defined distribution over the set of candidates.
     */
    @Override
    public BigFraction calculateUtility(VotingGameState<C> gameState, int playerIndex) {
        final Map<C, BigFraction> distribution = votingRule.getWinnerDistribution(gameState.getVotes(), allCandidates);
        return calculateExpectedUtility(distribution, playerIndex);
    }

    /**
     *
     * @return The set of ballots in which every voter votes for one of her favorite candidates.
     */
    public Set<List<C>> getTruthfulProfiles() {
        return truthfulProfiles;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("Expected utility based on the ");
        builder.append(votingRule.getName())
                .append(" voting rule and the following cardinal utilities:\n");

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
}
