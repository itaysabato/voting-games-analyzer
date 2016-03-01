package il.ac.huji.cs.itays04.voting.quadratic;

import il.ac.huji.cs.itays04.ne.UtilityCalculator;
import il.ac.huji.cs.itays04.voting.VotingState;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class QuadraticUtilityCalculator<C> implements UtilityCalculator<VotingState<C>> {
    private final List<Map<C, Integer>> individualUtilities;

    public QuadraticUtilityCalculator(List<Map<C, Integer>> individualUtilities) {
        this.individualUtilities = individualUtilities;
    }

    @Override
    public BigDecimal calculateUtility(VotingState<C> gameState, int player) {
        //todo: calculate probabilities for each candidate and then compute expectancy.
        return null;
    }
}
