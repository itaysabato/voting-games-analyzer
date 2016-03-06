package il.ac.huji.cs.itays04.voting;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import il.ac.huji.cs.itays04.games.api.Game;

import java.util.*;
import java.util.stream.Collectors;

public class VotingGame<C> implements Game<VotingGameState<C>> {
    private final ImmutableSet<C> allCandidates;
    private final VotingGameState<C> initialState;

    public VotingGame(Set<C> allCandidates, List<C> initialVotes) {
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

    private VotingGameState<C> makeMove(VotingGameState<C> state, int playerIndex, C candidate) {

        final ArrayList<C> newVotes = new ArrayList<>(state.getVotes());
        newVotes.set(playerIndex, candidate);

        return new VotingGameState<>(ImmutableList.copyOf(newVotes));
    }
}
