package il.ac.huji.cs.itays04.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.IntegerConverter;
import com.google.common.base.MoreObjects;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class Arguments {
    private static final String POS_DESC_PRE = "A comma-separated list of ";
    private static final String POS_DESC_POST = " positions, as rational numbers of the form '1', '-2', '3/2', etc.";
    private static final String RAND_DESC_PRE = "The number of random ";
    private static final String RAND_DESC_POST = " to generate. It is also possible to give a range from " +
            "which the amount will be chosen uniformly, e.g. 2,4 or 1,3.";

    @Parameter(
            names = {"-h", "--help"},
            description = "Display help message",
            help = true
    )
    private boolean help = false;

    @Parameter(
            names = {"-v", "--voters"},
            description = POS_DESC_PRE + "voter" + POS_DESC_POST,
            converter = BigFractionConverter.class
    )
    private List<BigFraction> voters = new LinkedList<>();

    @Parameter(
            names = {"-c", "--candidates"},
            description = POS_DESC_PRE + "candidate" + POS_DESC_POST,
            converter = BigFractionConverter.class
    )
    private List<BigFraction> candidates = new LinkedList<>();

    @Parameter(
            names = {"-rc", "--random-candidates"},
            description = RAND_DESC_PRE + "candidates" + RAND_DESC_POST,
            converter = IntegerConverter.class,
            validateValueWith = NonNegativeRangeValidator.class
    )
    private List<Integer> randomCandidatesRange = Collections.emptyList();

    @Parameter(
            names = {"-rv", "--random-voters"},
            description = RAND_DESC_PRE + "voters" + RAND_DESC_POST,
            converter = IntegerConverter.class,
            validateValueWith = NonNegativeRangeValidator.class
    )
    private List<Integer> randomVotersRange = Collections.emptyList();

    public boolean isHelp() {
        return help;
    }

    public List<BigFraction> getVoters() {
        return voters;
    }

    public List<BigFraction> getCandidates() {
        return candidates;
    }

    public List<Integer> getRandomCandidatesRange() {
        return randomCandidatesRange;
    }

    public List<Integer> getRandomVotersRange() {
        return randomVotersRange;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("help", help)
                .add("voters", voters)
                .add("candidates", candidates)
                .toString();
    }
}
