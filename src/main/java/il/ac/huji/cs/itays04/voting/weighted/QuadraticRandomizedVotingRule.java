package il.ac.huji.cs.itays04.voting.weighted;

import org.apache.commons.math3.fraction.BigFraction;

public class QuadraticRandomizedVotingRule extends WeightedVotesRandomizedVotingRule {

    @Override
    public String getName() {
        return "Quadratic";
    }

    @Override
    protected <C> BigFraction getWeight(C candidate, BigFraction voteCount) {
        return voteCount;
    }
}
