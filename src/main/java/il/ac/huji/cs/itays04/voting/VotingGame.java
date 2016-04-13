package il.ac.huji.cs.itays04.voting;

import com.google.common.collect.ImmutableSet;
import il.ac.huji.cs.itays04.games.api.Game;
import il.ac.huji.cs.itays04.games.api.SocialWelfareCalculator;
import il.ac.huji.cs.itays04.games.api.UtilityCalculator;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VotingGame<C, U extends Number & Comparable<U>, W extends Number & Comparable<W>>
        implements Game<VotingGameState<C>, U, W> {

    private final int numberOfPlayers;
    private final ImmutableSet<C> allCandidates;
    private final Map<VotingGameState<C>, W> truthfulStates;
    private final UtilityCalculator<VotingGameState<C>, U> utilityCalculator;
    private final SocialWelfareCalculator<VotingGameState<C>, U, W> socialWelfareCalculator;

    public VotingGame(
            Set<C> allCandidates,
            Set<List<C>> truthfulVotes,
            UtilityCalculator<VotingGameState<C>, U> utilityCalculator,
            SocialWelfareCalculator<VotingGameState<C>, U, W> socialWelfareCalculator) {

        this.utilityCalculator = utilityCalculator;
        this.socialWelfareCalculator = socialWelfareCalculator;
        this.allCandidates = ImmutableSet.copyOf(allCandidates);
        this.numberOfPlayers = truthfulVotes.iterator()
                .next()
                .size();

        final LinkedHashMap<VotingGameState<C>, W> states = truthfulVotes.stream()
                .map(VotingGameState::new)
                .collect(Collectors.toMap(Function.identity(),
                        s -> socialWelfareCalculator.calculateWelfare(this, s),
                        (x, y) -> x,
                        LinkedHashMap::new));

        this.truthfulStates = Collections.unmodifiableMap(states);
    }

    @Override
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public Map<VotingGameState<C>, W> getTruthfulStates() {
        return truthfulStates;
    }

    @Override
    public Set<VotingGameState<C>> getPossibleMovesForPlayer(VotingGameState<C> state, int playerIndex) {
        final C currentVote = state.getVotes().get(playerIndex);

        return allCandidates.stream()
                .sequential()
                .filter(candidate -> !currentVote.equals(candidate))
                .map(candidate -> makeMove(state, playerIndex, candidate))
                .collect(Collectors.toCollection(() -> new HashSet<>(allCandidates.size() - 1)));
    }

    @Override
    public UtilityCalculator<VotingGameState<C>, U> getUtilityCalculator() {
        return utilityCalculator;
    }

    @Override
    public SocialWelfareCalculator<VotingGameState<C>, U, W> getSocialWelfareCalculator() {
        return socialWelfareCalculator;
    }

    private VotingGameState<C> makeMove(VotingGameState<C> state, int playerIndex, C candidate) {

        final ArrayList<C> newVotes = new ArrayList<>(state.getVotes());
        newVotes.set(playerIndex, candidate);

        return new VotingGameState<>(newVotes);
    }

    @Override
    public String toString() {
        return "Type: Voting Game"
                + "\nNumber of voters: " + numberOfPlayers
                + "\nCandidates: " + allCandidates
                + "\n\nUtility function: " + utilityCalculator
                + "\nSocial welfare function: " + socialWelfareCalculator;
    }
}
