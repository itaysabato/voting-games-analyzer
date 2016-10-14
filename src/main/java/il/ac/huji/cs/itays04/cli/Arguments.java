package il.ac.huji.cs.itays04.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.IntegerConverter;
import com.google.common.collect.ImmutableList;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.LinkedList;
import java.util.List;

class Arguments {
    private static final String VOTERS_NAME = "-v";
    private static final String CANDIDATES_NAME = "-c";
    public static final String UTILITIES_NAME = "-u";

    private static final String POS_DESC_PRE = "A comma-separated list of ";
    private static final String POS_DESC_POST = " positions, as rational numbers of the form '1', '-2', '3/2', etc. " +
            "If the " + UTILITIES_NAME + " option is set, " +
            "only the first number will be read, and floored to an integer.";

    private static final String RAND_DESC_PRE = "The number of random ";
    private static final String RAND_DESC_POST = " to generate. It is also possible to give a range from " +
            "which the amount will be chosen uniformly, e.g. 2,4 or 1,3.";

    @Parameter(
            names = {"-h", "--help"},
            description = "Display help message.",
            help = true
    )
    private boolean help = false;

    @Parameter(
            names = {"-q", "--quiet"},
            description = "Do not output individual game analyses, only final aggregation."
    )
    private boolean quiet = false;

    @Parameter(
            names = {VOTERS_NAME, "--voters"},
            description = POS_DESC_PRE + "voter" + POS_DESC_POST,
            converter = BigFractionConverter.class
    )
    private List<BigFraction> voters = new LinkedList<>();

    @Parameter(
            names = {"-nc", "--no-candidates"},
            description = "Use the voters as the only candidates. " +
                    "Other options regarding candidates are ignored if this flag is set. If the " + UTILITIES_NAME +
                    " option is set, this flag indicates that the number of candidates is equal to the number of voters."
    )
    private boolean noCandidates = false;

    @Parameter(
            names = {CANDIDATES_NAME, "--candidates"},
            description = POS_DESC_PRE + "candidate" + POS_DESC_POST,
            converter = BigFractionConverter.class
    )
    private List<BigFraction> candidates = new LinkedList<>();

    @Parameter(
            names = {"-r", "--randomize"},
            description = "Generate random positions. If this flag is not set, " +
                    "voters and candidates must be explicitly specified. If the " + UTILITIES_NAME + " option is set, " +
                    "this flag will be ignored."
    )
    private boolean randomize = false;

    @Parameter(
            names = {"-rc", "--random-candidates"},
            description = RAND_DESC_PRE + "candidates" + RAND_DESC_POST,
            converter = IntegerConverter.class,
            validateValueWith = NonNegativeRangeValidator.class
    )
    private List<Integer> randomCandidatesRange = ImmutableList.of(2,5);

    @Parameter(
            names = {"-rv", "--random-voters"},
            description = RAND_DESC_PRE + "voters" + RAND_DESC_POST,
            converter = IntegerConverter.class,
            validateValueWith = NonNegativeRangeValidator.class
    )
    private List<Integer> randomVotersRange = ImmutableList.of(2,5);


    @Parameter(
            names = {UTILITIES_NAME, "--utilities"},
            description = "A comma-separated list of cardinal utilities, starting with the utilities the first voter " +
                    "gets from the first candidates, then the second candidates, etc. followed by a list for the " +
                    "second voter and so on until the last voter. For example, if we have 2 voters and 3 candidates " +
                    "then \"1,2,1/3,4,23,7/5\" would be a valid list. If this option is set, the number of voters " +
                    "and number of candidates should be given instead of their positions lists " +
                    "via " + VOTERS_NAME + " and " + CANDIDATES_NAME + " respectively.",
            converter = BigFractionConverter.class
    )
    private List<BigFraction> utilities = new LinkedList<>();

    @Parameter(
            names = {"-n", "--number-games"},
            description = "The number of games to analyze."
    )
    private int numberOfGames = 1;

    public boolean isHelp() {
        return help;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public List<BigFraction> getVoters() {
        return voters;
    }

    public boolean isNoCandidates() {
        return noCandidates;
    }

    public List<BigFraction> getCandidates() {
        return candidates;
    }

    public boolean isRandomize() {
        return randomize;
    }

    public List<Integer> getRandomCandidatesRange() {
        return randomCandidatesRange;
    }

    public List<Integer> getRandomVotersRange() {
        return randomVotersRange;
    }

    public List<BigFraction> getUtilities() {
        return utilities;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }
}
