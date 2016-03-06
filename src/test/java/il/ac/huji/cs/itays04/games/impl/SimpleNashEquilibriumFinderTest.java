package il.ac.huji.cs.itays04.games.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import il.ac.huji.cs.itays04.voting.VotingGame;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import il.ac.huji.cs.itays04.voting.quadratic.QuadraticFactory;
import il.ac.huji.cs.itays04.voting.quadratic.QuadraticUtilityCalculator;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public class SimpleNashEquilibriumFinderTest {

    @Test
    public void testTheorem12() {
        final List<Integer> voterPositions = Lists.newArrayList(4, 32, 34, 48, 67);
        final Set<Integer> candidatePositions = Sets.newHashSet(14, 32, 42, 60, 93);

        final QuadraticFactory quadraticFactory = StaticContext.getInstance().getQuadraticFactory();
        final QuadraticUtilityCalculator<Integer> utilityCalculator = quadraticFactory
                .createDistanceBasedCalculator(voterPositions, candidatePositions);

        final CachedUtilityCalculator<VotingGameState<Integer>, ?> cachedUtilityCalculator =
                new CachedUtilityCalculator<>(utilityCalculator, 3125);

        final SimpleNashEquilibriumFinder<VotingGameState<Integer>> neFinder = new SimpleNashEquilibriumFinder<>(
                System.out,
                StaticContext.getInstance().getSimpleGameTraverser(),
                cachedUtilityCalculator);

        final VotingGame<Integer> game = quadraticFactory.createDistanceBasedGame(voterPositions, candidatePositions);

        final Optional<? extends VotingGameState<Integer>> ne = neFinder.findNE(game);

        System.out.println("******************");

        if (ne.isPresent()) {
            final VotingGameState<Integer> s = ne.get();
            System.out.println("NE FOUND:");
            System.out.println(s);
            System.out.println("Utilities: ");
            System.out.println(cachedUtilityCalculator.calculateUtility(s, 0));
            System.out.println(cachedUtilityCalculator.calculateUtility(s, 1));
            System.out.println(cachedUtilityCalculator.calculateUtility(s, 2));
            System.out.println(cachedUtilityCalculator.calculateUtility(s, 3));
            System.out.println(cachedUtilityCalculator.calculateUtility(s, 4));
        }
        else {
            System.out.println("NE NOT FOUND!");
        }

        System.out.println("******************");

        Assert.assertFalse("should be no NE.", ne.isPresent());
    }
}
