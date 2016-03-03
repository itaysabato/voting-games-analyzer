package il.ac.huji.cs.itays04.voting;

import il.ac.huji.cs.itays04.ne.GameState;

import java.util.*;
import java.util.stream.Collectors;

public final class VotingGameState<C> implements GameState<VotingGameState<C>> {
    private final List<C> votes;
    private final Set<C> allCandidates;

    public VotingGameState(List<C> votes, Set<C> allCandidates) {
        this.votes = votes;
        this.allCandidates = allCandidates;
    }

    @Override
    public int getNumberOfPlayers() {
        return votes.size();
    }

    public C getVote(int playerIndex) {
        return votes.get(playerIndex);
    }

    @Override
    public Collection<? extends VotingGameState<C>> getPossibleMovesForPlayer(int index) {
        final C currentVote = votes.get(index);

        return allCandidates.stream()
                .sequential()
                .filter(candidate -> !currentVote.equals(candidate))
                .map(candidate -> makeMove(index, candidate))
                .collect(Collectors.toCollection(() -> new ArrayList<>(allCandidates.size() - 1)));
    }

    private VotingGameState<C> makeMove(int index, C candidate) {

        final ArrayList<C> newVotes = new ArrayList<>(votes);
        newVotes.set(index, candidate);

        return new VotingGameState<>(newVotes, allCandidates);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VotingGameState<?> that = (VotingGameState<?>) o;
        return Objects.equals(votes, that.votes) &&
                Objects.equals(allCandidates, that.allCandidates);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(votes);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");

        for (int i = 0; i < votes.size(); i++) {
            builder.append(i)
                    .append(" votes [")
                    .append(votes.get(i))
                    .append("], ");
    }

        return builder.substring(0, builder.length() - 2) + "}";
    }
}
