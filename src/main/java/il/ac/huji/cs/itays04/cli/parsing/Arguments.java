package il.ac.huji.cs.itays04.cli.parsing;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.IntegerConverter;
import com.google.common.collect.ImmutableList;
import il.ac.huji.cs.itays04.voting.RandomizedVotingRule;
import il.ac.huji.cs.itays04.voting.weighted.QuadraticRandomizedVotingRule;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.LinkedList;
import java.util.List;

public class Arguments {
    private static final String VOTERS_NAME = "-v";
    private static final String CANDIDATES_NAME = "-c";
    public static final String UTILITIES_NAME = "-u";

    private static final String POS_DESC_PRE = "A comma-separated list of ";
    private static final String POS_DESC_POST = " positions as rational numbers of the form '1', '-2', '3/2', etc. " +
            "If the " + UTILITIES_NAME + " option is set, " +
            "the first number will be read as the number of ";

    private static final String RAND_DESC_PRE = "The number of random ";
    private static final String RAND_DESC_POST = " to generate. It is also possible to give a range from " +
            "which the amount will be chosen uniformly, e.g. 2,4 or 1,3.";

    @Parameter(
            names = {"-h", "--help"},
            description = "Display help message.",
            help = true
    )
    @ExtendedParameter()
    private boolean help = false;

    @Parameter(
            names = {"-r", "--randomize"},
            description = "Generate random positions. If this flag is not set, " +
                    "voters and candidates must be explicitly specified. If the " + UTILITIES_NAME + " option is set, " +
                    "this flag will be ignored."
    )
    @ExtendedParameter(priority = 10)
    private boolean randomize = false;

    @Parameter(
            names = {"-q", "--quiet"},
            description = "Do not output individual game analyses, only final aggregation."
    )
    @ExtendedParameter(priority = 20)
    private boolean quiet = false;

    @Parameter(
            names = {"-n", "--number-games"},
            description = "The number of games to analyze."
    )
    @ExtendedParameter(priority = 30)
    private int numberOfGames = 1;

    @Parameter(
            names = {VOTERS_NAME, "--voters"},
            description = POS_DESC_PRE + "voter" + POS_DESC_POST + "voters.",
            converter = BigFractionConverter.class
    )
    @ExtendedParameter(priority = 40)
    private List<BigFraction> voters = new LinkedList<>();

    @Parameter(
            names = {CANDIDATES_NAME, "--candidates"},
            description = POS_DESC_PRE + "candidate" + POS_DESC_POST  + "candidates.",
            converter = BigFractionConverter.class
    )
    @ExtendedParameter(priority = 50)
    private List<BigFraction> candidates = new LinkedList<>();

    @Parameter(
            names = {"-nc", "--no-candidates"},
            description = "Use the voters as the only candidates. " +
                    "Other options regarding candidates are ignored if this flag is set. If the " + UTILITIES_NAME +
                    " option is set, this flag indicates that the number of candidates is equal to the number of voters."
    )
    @ExtendedParameter(priority = 60)
    private boolean noCandidates = false;

    @Parameter(
            names = {"-rv", "--random-voters"},
            description = RAND_DESC_PRE + "voters" + RAND_DESC_POST,
            converter = IntegerConverter.class,
            validateValueWith = NonNegativeRangeValidator.class
    )
    @ExtendedParameter(priority = 70)
    private List<Integer> randomVotersRange = ImmutableList.of(2,5);

    @Parameter(
            names = {"-rc", "--random-candidates"},
            description = RAND_DESC_PRE + "candidates" + RAND_DESC_POST,
            converter = IntegerConverter.class,
            validateValueWith = NonNegativeRangeValidator.class
    )
    @ExtendedParameter(priority = 80)
    private List<Integer> randomCandidatesRange = ImmutableList.of(2,5);

    @Parameter(
            names = {UTILITIES_NAME, "--utilities"},
            description = "A comma-separated list of cardinal utilities, starting with the utility the first voter " +
                    "gets from the first candidate, then the second candidate, etc. followed by a list for the " +
                    "second voter and so on until the last voter. For example, if we have 2 voters and 3 candidates " +
                    "then \"1,2,1/3,4,23,7/5\" would be a valid list. If this option is set, the number of voters " +
                    "and number of candidates should be given via " + VOTERS_NAME + " and " + CANDIDATES_NAME +
                    ", respectively, instead of the position lists.",
            converter = BigFractionConverter.class
    )
    @ExtendedParameter(priority = 90)
    private List<BigFraction> utilities = new LinkedList<>();


    @Parameter(
            names = {"-vr", "--voting-rule"},
            description = "The fully qualified name of a java class implementing the interface " +
                    "RandomizedVotingRule to be used instead of the Quadratic voting rule. The compiled class must " +
                    "have a no-argument constructor and be present in the lib folder.",
            converter = VotingRuleConverter.class
    )
    @ExtendedParameter(priority = 100)
    private RandomizedVotingRule votingRule = new QuadraticRandomizedVotingRule();

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

    public RandomizedVotingRule getVotingRule() {
        return votingRule;
    }
}
