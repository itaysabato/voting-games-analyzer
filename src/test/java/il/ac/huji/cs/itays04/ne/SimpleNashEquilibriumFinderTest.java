package il.ac.huji.cs.itays04.ne;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import il.ac.huji.cs.itays04.voting.VotingState;
import il.ac.huji.cs.itays04.voting.quadratic.QuadraticFactory;
import il.ac.huji.cs.itays04.voting.quadratic.QuadraticUtilityCalculator;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public class SimpleNashEquilibriumFinderTest {

    @Test
    public void testTheorem12() {
        final List<Integer> voterPositions = Lists.newArrayList(4, 32, 34, 48, 67);
        final Set<Integer> candidatePositions = Sets.newHashSet(14, 32, 42, 60, 93);

        final QuadraticUtilityCalculator<Integer> utilityCalculator = QuadraticFactory.getInstance()
                .createDistanceBasedCalculator(voterPositions, candidatePositions);

        final CachedUtilityCalculator<VotingState<Integer>> cachedUtilityCalculator = new CachedUtilityCalculator<>(utilityCalculator, 1000);
        final SimpleNashEquilibriumFinder<VotingState<Integer>> neFinder = new SimpleNashEquilibriumFinder<>(System.out, cachedUtilityCalculator);

        final VotingState<Integer> initialState = QuadraticFactory.getInstance()
                .createSomeDistanceBasedState(voterPositions, candidatePositions);

        final Optional<? extends VotingState<Integer>> ne = neFinder.findNE(initialState);

        System.out.println("******************");

        if (ne.isPresent()) {
            final VotingState<Integer> s = ne.get();
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

    }
}
