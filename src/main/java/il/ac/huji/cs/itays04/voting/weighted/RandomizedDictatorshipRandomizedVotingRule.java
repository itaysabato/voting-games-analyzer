package il.ac.huji.cs.itays04.voting.weighted;

import org.apache.commons.math3.fraction.BigFraction;

public class RandomizedDictatorshipRandomizedVotingRule extends WeightedVotesRandomizedVotingRule {
    @Override
    public String getName() {
        return "Randomized Dictatorship";
    }

    @Override
    protected <C> BigFraction getWeight(C candidate, BigFraction voteCount) {
        return BigFraction.ONE;
    }

}
