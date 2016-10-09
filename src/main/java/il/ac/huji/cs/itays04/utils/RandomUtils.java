package il.ac.huji.cs.itays04.utils;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomUtils {
    private static final Random random = new Random();

    private final PositionUtils positionUtils;

    public RandomUtils(PositionUtils positionUtils) {
        this.positionUtils = positionUtils;
    }

    public Set<BigFraction> getRandomCandidates(int min, int max) {
        final int numToGenerate = randomInRange(min, max);
        return getRandomCandidates(numToGenerate);
    }

    public int randomInRange(int min, int max) {
        final OptionalInt optionalInt = random.ints(min, max).findAny();
        assert optionalInt.isPresent();
        return optionalInt.getAsInt();
    }

    private Set<BigFraction> getRandomCandidates(int numToGenerate) {
        final Set<Integer> candidates = random.ints()
                .distinct()
                .limit(numToGenerate)
                .mapToObj(i -> i)
                .collect(Collectors.toSet());

        return positionUtils.candidates(candidates);
    }

    public List<BigFraction> getRandomPositions(int min, int max) {
        final int numToGenerate = randomInRange(min, max);
        return getRandomPositions(numToGenerate);
    }

    public List<BigFraction> getRandomPositions(int numberOfPositions) {
        final List<Integer> positions = random.ints(numberOfPositions)
                .mapToObj(i -> i)
                .collect(Collectors.toList());

        return positionUtils.positions(positions);
    }
}
