package il.ac.huji.cs.itays04.voting.quadratic;

import il.ac.huji.cs.itays04.games.api.SocialWelfareCalculator;
import il.ac.huji.cs.itays04.games.impl.CachedUtilityCalculator;
import il.ac.huji.cs.itays04.voting.VotingGame;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class QuadraticFactory {

    public WeightedUtilityCalculator<BigFraction> createWeightedCalculator(
            List<BigFraction> voterPositions, Set<BigFraction> candidatePositions, boolean quadratic) {

        List<Map<BigFraction, BigFraction>> individualUtilities = voterPositions.stream()
                .sequential()
                .map(p -> calculateUtilities(p, candidatePositions))
                .collect(Collectors.toList());

        return new WeightedUtilityCalculator<>(individualUtilities, quadratic);
    }

    private Map<BigFraction, BigFraction> calculateUtilities(BigFraction voterPosition, Set<BigFraction> candidatePositions) {
        final Map<BigFraction, BigFraction> utils = new LinkedHashMap<>();

        for (BigFraction candidatePosition : candidatePositions) {
            final BigFraction util = voterPosition.subtract(candidatePosition)
                    .abs()
                    .negate();

            utils.put(candidatePosition, util);
        }

        return utils;
    }

    public <W extends Number & Comparable<W>>
    VotingGame<BigFraction, BigFraction, W> createDistanceBasedGame(
            List<BigFraction> voterPositions,
            Set<BigFraction> candidatePositions,
            SocialWelfareCalculator<VotingGameState<BigFraction>, BigFraction, W> socialWelfareCalculator) {

        final double numberOfStates = Math.ceil(Math.pow(candidatePositions.size(), voterPositions.size()));

        final WeightedUtilityCalculator<BigFraction> utilityCalculator = createWeightedCalculator(
                voterPositions, candidatePositions, true);
        
        final Set<List<BigFraction>> truthfulProfiles = utilityCalculator.getTruthfulProfiles();

        final CachedUtilityCalculator<VotingGameState<BigFraction>, BigFraction> cachedUtilityCalculator =
                new CachedUtilityCalculator<>(utilityCalculator, (int) numberOfStates);

        return new VotingGame<>(candidatePositions, truthfulProfiles, cachedUtilityCalculator, socialWelfareCalculator);
    }
}
