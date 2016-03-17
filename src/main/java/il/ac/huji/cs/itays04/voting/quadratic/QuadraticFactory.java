package il.ac.huji.cs.itays04.voting.quadratic;

import il.ac.huji.cs.itays04.games.api.SocialWelfareCalculator;
import il.ac.huji.cs.itays04.games.impl.CachedUtilityCalculator;
import il.ac.huji.cs.itays04.voting.VotingGame;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class QuadraticFactory {

    public QuadraticUtilityCalculator<Integer> createDistanceBasedCalculator(List<Integer> voterPositions, Set<Integer> candidatePositions) {

        List<Map<Integer, Long>> individualUtilities = voterPositions.stream()
                .sequential()
                .map(p -> calculateUtilities(p, candidatePositions))
                .collect(Collectors.toList());

        return new QuadraticUtilityCalculator<>(individualUtilities);
    }

    private Map<Integer, Long> calculateUtilities(Integer voterPosition, Set<Integer> candidatePositions) {
        final Map<Integer, Long> utils = new HashMap<>();

        for (Integer candidatePosition : candidatePositions) {
            utils.put(candidatePosition, -Math.abs(((long) voterPosition) - ((long) candidatePosition)));
        }

        return utils;
    }

    public <W extends Number & Comparable<W>>
    VotingGame<Integer, BigFraction, W> createDistanceBasedGame(
            List<Integer> voterPositions,
            Set<Integer> candidatePositions,
            SocialWelfareCalculator<VotingGameState<Integer>, BigFraction, W> socialWelfareCalculator) {

        final double numberOfStates = Math.ceil(Math.pow(candidatePositions.size(), voterPositions.size()));

        final QuadraticUtilityCalculator<Integer> utilityCalculator = createDistanceBasedCalculator(voterPositions, candidatePositions);
        final List<Integer> truthfulProfile = utilityCalculator.getTruthfulProfile();

        final CachedUtilityCalculator<VotingGameState<Integer>, BigFraction> cachedUtilityCalculator =
                new CachedUtilityCalculator<>(utilityCalculator, (int) numberOfStates);

        return new VotingGame<>(candidatePositions, truthfulProfile, cachedUtilityCalculator, socialWelfareCalculator);
    }
}
