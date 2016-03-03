package il.ac.huji.cs.itays04.voting.quadratic;

import il.ac.huji.cs.itays04.voting.VotingGameState;

import java.util.*;
import java.util.stream.Collectors;

public class QuadraticFactory {
    private static QuadraticFactory ourInstance = new QuadraticFactory();

    private QuadraticFactory() {
    }

    public static QuadraticFactory getInstance() {
        return ourInstance;
    }

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

    public VotingGameState<Integer> createSomeDistanceBasedState(List<Integer> voterPositions, Set<Integer> candidatePositions) {

        List<Integer> votes = new ArrayList<>(candidatePositions).subList(0, voterPositions.size());
        HashSet<Integer> allCandidates = new HashSet<>(candidatePositions);

        return new VotingGameState<>(votes, allCandidates);
    }
}
