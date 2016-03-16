package il.ac.huji.cs.itays04.voting.quadratic;

import il.ac.huji.cs.itays04.games.api.SocialWelfareCalculator;
import il.ac.huji.cs.itays04.games.api.UtilityCalculator;
import il.ac.huji.cs.itays04.voting.VotingGame;
import il.ac.huji.cs.itays04.voting.VotingGameState;

import java.util.*;
import java.util.stream.Collectors;

public class QuadraticFactory {

    public QuadraticUtilityCalculator<Integer> createDistanceBasedCalculator(List<Integer> voterPositions, Set<Integer> candidatePositions) {

        ArrayList<Map<Integer, Integer>> individualUtilities = voterPositions.stream()
                .sequential()
                .map(p -> calculateUtilities(p, candidatePositions))
                .collect(Collectors.toCollection(()->new ArrayList<>(voterPositions.size())));

        return new QuadraticUtilityCalculator<>(individualUtilities);
    }

    private Map<Integer, Integer> calculateUtilities(Integer voterPosition, Set<Integer> candidatePositions) {
        final Map<Integer, Integer> utils = new HashMap<>();

        for (Integer candidatePosition : candidatePositions) {
            utils.put(candidatePosition, Math.abs(voterPosition-candidatePosition));
        }

        return utils;
    }

    public <U extends Number & Comparable<U>, W extends Number & Comparable<W>>
    VotingGame<Integer, U, W> createDistanceBasedGame(
            List<Integer> voterPositions,
            Set<Integer> candidatePositions,
            UtilityCalculator<VotingGameState<Integer>, U> utilityCalculator,
            SocialWelfareCalculator<VotingGameState<Integer>, U, W> socialWelfareCalculator) {

        final List<Integer> votes = new ArrayList<>(candidatePositions).subList(0, voterPositions.size());

        return new VotingGame<>(candidatePositions, votes, utilityCalculator, socialWelfareCalculator);
    }
}
