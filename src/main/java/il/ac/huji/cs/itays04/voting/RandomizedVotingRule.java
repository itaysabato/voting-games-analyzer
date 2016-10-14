package il.ac.huji.cs.itays04.voting;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A Class that implements this interface represents a randomized voting rule.
 */
public interface RandomizedVotingRule {

    /**
     *
     * @return The user friendly name of this randomized voting rule.
     */
    String getName();

    /**
     * Calculates the winner distribution based on the given ballot. Expected utilities will
     * be calculated based on the returned distribution.
     * <br/>
     * Note that the returned map values must all be between 0 to 1 and sum up to 1.
     *
     * @param votes A ballot containing the votes of each voter, in order.
     * @param candidatesSet The set of candidates.
     * @return A distribution over the set of candidates.
     */
    <C> Map<C, BigFraction> getWinnerDistribution(List<C> votes, Set<C> candidatesSet);
}
