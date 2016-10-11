package il.ac.huji.cs.itays04.rational;

import org.apache.commons.math3.fraction.BigFraction;

import java.util.*;
import java.util.stream.Collectors;

public class RationalUtils {

    public LinkedHashSet<NamedRationalEntity> voters(Integer... positions) {
        return voters(Arrays.asList(positions));
    }

    public LinkedHashSet<NamedRationalEntity> voters(List<Integer> voters) {
        return toVoters(toFractions(voters));
    }

    public List<BigFraction> toFractions(List<Integer> voters) {
        return voters.stream()
                .map(BigFraction::new)
                .collect(Collectors.toList());
    }

    public LinkedHashSet<NamedRationalEntity> named(List<BigFraction> positions, String namePrefix) {
        Collections.sort(positions);
        final LinkedHashSet<NamedRationalEntity> named = new LinkedHashSet<>(positions.size());

        for (int i = 0; i < positions.size(); i++) {
            final NamedRationalEntity rationalEntity = new NamedRationalEntity(
                    positions.get(i), namePrefix + (i + 1));

            named.add(rationalEntity);
        }

        return named;
    }

    public LinkedHashSet<NamedRationalEntity> voters(BigFraction... positions) {
        return toVoters(Arrays.asList(positions));
    }

    public LinkedHashSet<NamedRationalEntity> toVoters(List<BigFraction> positions) {
        return named(positions, "V");
    }

    public LinkedHashSet<NamedRationalEntity> candidates(Integer... positions) {
        return candidates(Arrays.asList(positions));
    }

    public LinkedHashSet<NamedRationalEntity> candidates(Collection<Integer> candidates) {
        return toCandidates(candidates.stream()
                .map(BigFraction::new)
                .collect(Collectors.toList()));
    }

    public LinkedHashSet<NamedRationalEntity> candidates(BigFraction... positions) {
        return toCandidates(Arrays.asList(positions));
    }

    public LinkedHashSet<NamedRationalEntity> toCandidates(List<BigFraction> positions) {
        return named(positions, "C");
    }
}
