package il.ac.huji.cs.itays04.voting.quadratic;

import il.ac.huji.cs.itays04.games.api.SocialWelfareCalculator;
import il.ac.huji.cs.itays04.games.impl.CachedUtilityCalculator;
import il.ac.huji.cs.itays04.rational.NamedRationalEntity;
import il.ac.huji.cs.itays04.voting.VotingGame;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.*;
import java.util.stream.Collectors;

public class QuadraticFactory {

    public WeightedUtilityCalculator<NamedRationalEntity> createWeightedCalculator(
            LinkedHashSet<NamedRationalEntity> voters, LinkedHashSet<NamedRationalEntity> candidates, boolean quadratic) {

        List<Map<NamedRationalEntity, BigFraction>> individualUtilities = voters.stream()
                .sequential()
                .map(p -> calculateUtilities(p.getPosition(), candidates))
                .collect(Collectors.toList());

        return new WeightedUtilityCalculator<>(individualUtilities, quadratic);
    }

    private Map<NamedRationalEntity, BigFraction> calculateUtilities(BigFraction voterPosition, Set<NamedRationalEntity> candidates) {
        final Map<NamedRationalEntity, BigFraction> utils = new LinkedHashMap<>();

        for (NamedRationalEntity candidatePosition : candidates) {
            final BigFraction util = voterPosition.subtract(candidatePosition.getPosition())
                    .abs()
                    .negate();

            utils.put(candidatePosition, util);
        }

        return utils;
    }

    public <W extends Number & Comparable<W>>
    VotingGame<NamedRationalEntity, BigFraction, W> createDistanceBasedGame(
            LinkedHashSet<NamedRationalEntity> voters,
            LinkedHashSet<NamedRationalEntity> candidates,
            SocialWelfareCalculator<BigFraction, W> socialWelfareCalculator) {

        final double numberOfStates = Math.ceil(Math.pow(candidates.size(), voters.size()));

        final WeightedUtilityCalculator<NamedRationalEntity> utilityCalculator = createWeightedCalculator(
                voters, candidates, true);
        
        final Set<List<NamedRationalEntity>> truthfulProfiles = utilityCalculator.getTruthfulProfiles();

        final CachedUtilityCalculator<VotingGameState<NamedRationalEntity>, BigFraction> cachedUtilityCalculator =
                new CachedUtilityCalculator<>(utilityCalculator, (int) numberOfStates);

        return new VotingGame<>(candidates, truthfulProfiles, cachedUtilityCalculator, socialWelfareCalculator);
    }
}
