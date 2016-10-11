package il.ac.huji.cs.itays04.rational;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.*;
import java.util.stream.Collectors;

public class RandomUtils {
    private static final Random random = new Random();

    private final RationalUtils rationalUtils;

    public RandomUtils(RationalUtils rationalUtils) {
        this.rationalUtils = rationalUtils;
    }

    public LinkedHashSet<NamedRationalEntity> getRandomCandidates(int min, int max) {
        final int numToGenerate = randomInRange(min, max);
        return getRandomCandidates(numToGenerate);
    }

    public int randomInRange(int min, int max) {
        final OptionalInt optionalInt = random.ints(min, max).findAny();
        assert optionalInt.isPresent();
        return optionalInt.getAsInt();
    }

    private LinkedHashSet<NamedRationalEntity> getRandomCandidates(int numToGenerate) {
        final Set<Integer> candidates = random.ints()
                .distinct()
                .limit(numToGenerate)
                .mapToObj(i -> i)
                .collect(Collectors.toSet());

        return rationalUtils.candidates(candidates);
    }

    public List<BigFraction> getRandomPositions(int min, int max) {
        final int numToGenerate = randomInRange(min, max);
        return getRandomPositions(numToGenerate);
    }

    public List<BigFraction> getRandomPositions(int numberOfPositions) {
        final List<Integer> positions = random.ints(numberOfPositions)
                .mapToObj(i -> i)
                .collect(Collectors.toList());

        return rationalUtils.toFractions(positions);
    }

    public LinkedHashSet<NamedRationalEntity> getRandomVoters(int numberOfVoters) {
        return rationalUtils.toVoters(getRandomPositions(numberOfVoters));
    }
}
