package il.ac.huji.cs.itays04.games.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import il.ac.huji.cs.itays04.games.api.BigFractionAverageSocialWelfareCalculator;
import il.ac.huji.cs.itays04.voting.VotingGame;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import il.ac.huji.cs.itays04.voting.quadratic.QuadraticFactory;
import org.apache.commons.math3.fraction.BigFraction;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class SimpleNashEquilibriumFinderTest {

    @Test
    public void testTheorem12() {
        final List<Integer> voterPositions = Lists.newArrayList(4, 32, 34, 48, 67);
        final Set<Integer> candidatePositions = Sets.newHashSet(14, 32, 42, 60, 93);

        final List<BigFraction> voterFractions = voterPositions.stream()
                .map(BigFraction::new)
                .collect(Collectors.toList());

        final Set<BigFraction> candidateFractions = candidatePositions.stream()
                .map(BigFraction::new)
                .collect(Collectors.toSet());
        final QuadraticFactory quadraticFactory = StaticContext.getInstance().getQuadraticFactory();

        final SimpleNashEquilibriumFinder neFinder = new SimpleNashEquilibriumFinder(
                StaticContext.getInstance().getSimpleGameTraverser());

        final BigFractionAverageSocialWelfareCalculator<VotingGameState<BigFraction>> socialWelfareCalculator =
                new BigFractionAverageSocialWelfareCalculator<>();

        final VotingGame<BigFraction, BigFraction, ?> game = quadraticFactory.createDistanceBasedGame(
                voterFractions, candidateFractions, socialWelfareCalculator);

        final Optional<? extends VotingGameState<BigFraction>> ne = neFinder.findNE(game);

        System.out.println("******************");

        if (ne.isPresent()) {
            final VotingGameState<BigFraction> s = ne.get();
            System.out.println("NE FOUND:");
            System.out.println(s);
            System.out.println("Utilities: ");
            System.out.println(game.getUtilityCalculator().calculateUtility(s, 0));
            System.out.println(game.getUtilityCalculator().calculateUtility(s, 1));
            System.out.println(game.getUtilityCalculator().calculateUtility(s, 2));
            System.out.println(game.getUtilityCalculator().calculateUtility(s, 3));
            System.out.println(game.getUtilityCalculator().calculateUtility(s, 4));
        }
        else {
            System.out.println("NE NOT FOUND!");
        }

        System.out.println("******************");

        Assert.assertFalse("should be no NE.", ne.isPresent());
    }
}
