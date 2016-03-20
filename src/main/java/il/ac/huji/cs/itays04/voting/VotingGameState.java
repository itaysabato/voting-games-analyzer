package il.ac.huji.cs.itays04.voting;

import com.google.common.collect.ImmutableList;
import il.ac.huji.cs.itays04.games.api.GameState;

import java.util.List;
import java.util.Objects;

public final class VotingGameState<C> implements GameState<VotingGameState<C>> {
    private final ImmutableList<C> votes;

    VotingGameState(ImmutableList<C> votes) {
        this.votes = votes;
    }

    public List<C> getVotes() {
        return votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VotingGameState<?> that = (VotingGameState<?>) o;
        return Objects.equals(votes, that.votes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(votes);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");

        for (int i = 0; i < votes.size(); i++) {
            builder.append("V")
                    .append(i)
                    .append(" votes [")
                    .append(votes.get(i))
                    .append("], ");
    }

        return builder.substring(0, builder.length() - 2) + "}";
    }
}
