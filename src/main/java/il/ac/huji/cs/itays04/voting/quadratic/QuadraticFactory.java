package il.ac.huji.cs.itays04.voting.quadratic;

import il.ac.huji.cs.itays04.games.api.SocialWelfareCalculator;
import il.ac.huji.cs.itays04.games.impl.CachedUtilityCalculator;
import il.ac.huji.cs.itays04.rational.BigFractionAverageSocialWelfareCalculator;
import il.ac.huji.cs.itays04.rational.NamedRationalEntity;
import il.ac.huji.cs.itays04.voting.VotingGame;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class QuadraticFactory {

    public WeightedUtilityCalculator<NamedRationalEntity> createWeightedCalculator(
            LinkedHashSet<NamedRationalEntity> voters, LinkedHashSet<NamedRationalEntity> candidates, boolean quadratic) {

        List<LinkedHashMap<NamedRationalEntity, BigFraction>> individualUtilities = voters.stream()
                .sequential()
                .map(p -> calculateUtilities(p.getPosition(), candidates))
                .collect(Collectors.toList());

        return new WeightedUtilityCalculator<>(individualUtilities, quadratic);
    }

    private LinkedHashMap<NamedRationalEntity, BigFraction> calculateUtilities(BigFraction voterPosition, Set<NamedRationalEntity> candidates) {
        final LinkedHashMap<NamedRationalEntity, BigFraction> utils = new LinkedHashMap<>();

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

        final WeightedUtilityCalculator<NamedRationalEntity> utilityCalculator = createWeightedCalculator(
                voters, candidates, true);

        return getGame(voters.size(), candidates, utilityCalculator, socialWelfareCalculator);
    }

    public <C, W extends Number & Comparable<W>> VotingGame<C, BigFraction, W> getGame(
            int numVoters,
            LinkedHashSet<C> candidates,
            WeightedUtilityCalculator<C> utilityCalculator,
            SocialWelfareCalculator<BigFraction, W> socialWelfareCalculator) {

        final double numberOfStates = Math.ceil(Math.pow(candidates.size(), numVoters));

        final CachedUtilityCalculator<VotingGameState<C>, BigFraction> cachedUtilityCalculator =
                new CachedUtilityCalculator<>(utilityCalculator, (int) numberOfStates);

        final Set<List<C>> truthfulProfiles = utilityCalculator.getTruthfulProfiles();
        return new VotingGame<>(candidates, truthfulProfiles, cachedUtilityCalculator, socialWelfareCalculator);
    }

    public VotingGame<String, BigFraction, BigFraction> createGame(List<LinkedHashMap<String, BigFraction>> allUtils) {
        return getGame(
                allUtils.size(),
                new LinkedHashSet<>(allUtils.get(0).keySet()),
                new WeightedUtilityCalculator<>(allUtils, true),
                new BigFractionAverageSocialWelfareCalculator());
    }
}
