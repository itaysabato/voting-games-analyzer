package il.ac.huji.cs.itays04.voting;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import il.ac.huji.cs.itays04.games.api.Game;
import il.ac.huji.cs.itays04.games.api.SocialWelfareCalculator;
import il.ac.huji.cs.itays04.games.api.UtilityCalculator;

import java.util.*;
import java.util.stream.Collectors;

public class VotingGame<C, U extends Number & Comparable<U>, W extends Number & Comparable<W>>
        implements Game<VotingGameState<C>, U, W> {

    private final ImmutableSet<C> allCandidates;
    private final VotingGameState<C> initialState;
    private final UtilityCalculator<VotingGameState<C>, U> utilityCalculator;
    private final SocialWelfareCalculator<VotingGameState<C>, U, W> socialWelfareCalculator;

    public VotingGame(
            Set<C> allCandidates,
            List<C> initialVotes,
            UtilityCalculator<VotingGameState<C>, U> utilityCalculator,
            SocialWelfareCalculator<VotingGameState<C>, U, W> socialWelfareCalculator) {

        this.utilityCalculator = utilityCalculator;
        this.socialWelfareCalculator = socialWelfareCalculator;
        this.allCandidates = ImmutableSet.copyOf(allCandidates);
        this.initialState = new VotingGameState<>(ImmutableList.copyOf(initialVotes));
    }

    @Override
    public int getNumberOfPlayers() {
        return initialState.getVotes().size();
    }

    @Override
    public Set<? extends VotingGameState<C>> getInitialStates() {
        return Collections.singleton(initialState);
    }

    @Override
    public Set<? extends VotingGameState<C>> getPossibleMovesForPlayer(VotingGameState<C> state, int playerIndex) {
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

        return new VotingGameState<>(ImmutableList.copyOf(newVotes));
    }

    @Override
    public String toString() {
        return "Type: Voting Game"
                + ";\tNumber of players: " + initialState.getVotes().size()
                + ";\tCandidates: " + allCandidates
                + ";\tUtility function: " + utilityCalculator
                + ";\tSocial welfare function: " + socialWelfareCalculator
                + ";\tInitial State: " + initialState;
    }
}
