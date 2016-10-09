package il.ac.huji.cs.itays04.utils;

import com.google.common.collect.Lists;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.*;
import java.util.stream.Collectors;

public class PositionUtils {

    public List<BigFraction> positions(Integer... positions) {
        return positions(Arrays.asList(positions));
    }

    public List<BigFraction> positions(List<Integer> voters) {
        return voters.stream()
                .sorted()
                .map(BigFraction::new)
                .collect(Collectors.toList());
    }

    public Set<BigFraction> candidates(List<BigFraction> positions) {
        return candidates(positions.toArray(new BigFraction[positions.size()]));
    }

    public Set<BigFraction> candidates(Integer... positions) {
        return candidates(Arrays.asList(positions));
    }

    public Set<BigFraction> candidates(Collection<Integer> candidates) {
        return candidates.stream()
                .sorted()
                .map(BigFraction::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public List<BigFraction> positions(BigFraction... positions) {
        final ArrayList<BigFraction> voters = Lists.newArrayList(positions);
        Collections.sort(voters);
        return voters;
    }

    public Set<BigFraction> candidates(BigFraction... positions) {
        final ArrayList<BigFraction> candidates = Lists.newArrayList(positions);
        return candidates.stream()
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
