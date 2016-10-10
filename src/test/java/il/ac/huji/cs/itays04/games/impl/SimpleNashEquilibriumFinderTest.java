package il.ac.huji.cs.itays04.games.impl;

import il.ac.huji.cs.itays04.cli.StaticContext;
import il.ac.huji.cs.itays04.games.api.BigFractionAverageSocialWelfareCalculator;
import il.ac.huji.cs.itays04.utils.RationalUtils;
import il.ac.huji.cs.itays04.voting.VotingGame;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import il.ac.huji.cs.itays04.voting.quadratic.NamedRationalEntity;
import il.ac.huji.cs.itays04.voting.quadratic.QuadraticFactory;
import org.apache.commons.math3.fraction.BigFraction;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Optional;


public class SimpleNashEquilibriumFinderTest {

    @Test
    public void testTheorem12() {
        final RationalUtils rationalUtils = StaticContext.getInstance().rationalUtils;
        final QuadraticFactory quadraticFactory = StaticContext.getInstance().quadraticFactory;

        final LinkedHashSet<NamedRationalEntity> voters = rationalUtils.voters(4, 32, 34, 48, 67);
        final LinkedHashSet<NamedRationalEntity> candidates = rationalUtils.candidates(14, 32, 42, 60, 93);

        final SimpleNashEquilibriumFinder neFinder = new SimpleNashEquilibriumFinder(
                StaticContext.getInstance().simpleGameTraverser);

        final BigFractionAverageSocialWelfareCalculator socialWelfareCalculator =
                new BigFractionAverageSocialWelfareCalculator();

        final VotingGame<NamedRationalEntity, BigFraction, ?> game = quadraticFactory.createDistanceBasedGame(
                voters, candidates, socialWelfareCalculator);

        final Optional<? extends VotingGameState<NamedRationalEntity>> ne = neFinder.findNE(game);

        System.out.println("******************");

        if (ne.isPresent()) {
            final VotingGameState<NamedRationalEntity> s = ne.get();
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
