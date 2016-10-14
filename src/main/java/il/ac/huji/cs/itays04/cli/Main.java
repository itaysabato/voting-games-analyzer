package il.ac.huji.cs.itays04.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import il.ac.huji.cs.itays04.rational.NamedRationalEntity;
import il.ac.huji.cs.itays04.rational.RandomUtils;
import il.ac.huji.cs.itays04.rational.RationalAggregator;
import il.ac.huji.cs.itays04.rational.RationalUtils;
import il.ac.huji.cs.itays04.voting.quadratic.AnalysisWithRandomDicComparison;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.*;

public class Main {
    private static final Main main = new Main();

    private final RandomUtils randomUtils = StaticContext.getInstance().randomUtils;
    private final RationalUtils rationalUtils = StaticContext.getInstance().rationalUtils;
    private final QuadraticAnalysisRunner quadraticAnalysisRunner = StaticContext.getInstance().quadraticAnalysisRunner;

    public static void main(String[] args) {
        main.run(args);
    }

    private void run(String[] args) {
        final Arguments arguments = new Arguments();
        final JCommander jCommander = new JCommander(arguments);
        jCommander.setProgramName("vga");

        try {
            jCommander.parse(args);

            if (arguments.isHelp()) {
                jCommander.usage();
            }
            else {
                validateVoters(arguments);
                validateCandidates(arguments);
                run(arguments);
            }
        }
        catch (ParameterException e) {
            System.out.println("Invalid command line arguments: " + e.getMessage());
            jCommander.usage();
            System.exit(1);
        }
    }

    private void validateVoters(Arguments arguments) {
        validatePositions(arguments.getVoters(), arguments.getUtilities().isEmpty() && arguments.isRandomize(), arguments.getRandomVotersRange(),
                "Voters");
    }

    private void validatePositions(
            List<BigFraction> positions,
            boolean randomize,
            List<Integer> range,
            final String name) {

        if (positions.isEmpty() &&
                (!randomize || zeroable(range))) {

            throw new ParameterException(name + " must be supplied explicitly if not randomly generated.");
        }
    }

    private boolean zeroable(List<Integer> range) {
        return range.isEmpty() || range.contains(0);
    }

    private void validateCandidates(Arguments arguments) {
        if (!arguments.isNoCandidates()) {
            validatePositions(arguments.getCandidates(), arguments.isRandomize(), arguments.getRandomCandidatesRange(),
                    "Candidates");
        }
    }

    private void run(Arguments arguments) {
        final RationalAggregator aggregator = new RationalAggregator();
        int numberOfGames = arguments.getNumberOfGames() > 0 ? arguments.getNumberOfGames() : 1;

        for (int i = 1; i <= numberOfGames; i++) {
            nextGame(i, arguments, aggregator);
        }

        System.out.println(aggregator.getCurrentAggregation());
    }

    private void nextGame(int gameIndex, Arguments arguments, RationalAggregator aggregator) {
        final String gameDescription = "Quadratic Voting Game " + gameIndex;
        final boolean quiet = arguments.isQuiet();
        final boolean noCandidates = arguments.isNoCandidates();
        List<BigFraction> voterPositions = arguments.getVoters();
        final List<BigFraction> utilities = arguments.getUtilities();
        List<BigFraction> candidatePositions = arguments.getCandidates();

        final AnalysisWithRandomDicComparison<?, BigFraction> analysis;

        if (utilities.isEmpty()) {
            if (arguments.isRandomize()) {
                voterPositions = generateMorePositions(arguments.getVoters(), arguments.getRandomVotersRange());

                if (!noCandidates) {
                    candidatePositions = generateMorePositions(arguments.getCandidates(), arguments.getRandomCandidatesRange());
                }
            }

            final LinkedHashSet<NamedRationalEntity> voters = rationalUtils.toVoters(voterPositions);
            final LinkedHashSet<NamedRationalEntity> candidates = noCandidates ?
                    voters : rationalUtils.toCandidates(candidatePositions);

            analysis = quadraticAnalysisRunner.analyzeAndReport(
                    voters,
                    candidates,
                    gameDescription,
                    quiet);
        }
        else {
            int numVoters = floorFirst(voterPositions);
            validateNum(numVoters, "voters");

            int numCandidates = noCandidates ? numVoters : floorFirst(candidatePositions);
            validateNum(numCandidates, "candidates");

            final int expectedTotal = numVoters * numCandidates;
            if (utilities.size() != expectedTotal) {
                throw new ParameterException("Expected [" + expectedTotal +
                        "] cardinal utility values: " + utilities);
            }

            boolean isPos = false;
            boolean isNeg = false;
            List<LinkedHashMap<String, BigFraction>> allUtils = new ArrayList<>(numVoters);
            for (int i = 0; i < numVoters; i++) {
                final LinkedHashMap<String, BigFraction> iUtils = new LinkedHashMap<>();
                allUtils.add(iUtils);

                for (int j = 0; j < numCandidates; j++) {
                    final String candidateJ = "C" + (j + 1);
                    final BigFraction uij = utilities.get((i * numCandidates) + j);

                    iUtils.put(candidateJ, uij);

                    final int sign = uij.compareTo(BigFraction.ZERO);
                    if (sign > 0) {
                        isPos = true;
                    }
                    else if (sign < 0) {
                        isNeg = true;
                    }

                    if (isNeg && isPos) {
                        throw new ParameterException("Cardinal utilities must be strictly non-negative or strictly " +
                                "non-positive: " + utilities);
                    }
                }
            }

            analysis = quadraticAnalysisRunner.analyzeAndReport(
                    allUtils,
                    gameDescription,
                    quiet);
        }

        aggregator.add(analysis);
    }

    private void validateNum(int num, String name) {
        if (num <= 0) {
            throw new ParameterException("Must provide number of " + name + " if cardinal utilities are specified.");
        }
    }

    private int floorFirst(List<BigFraction> voterPositions) {
        return voterPositions.get(0).intValue();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private LinkedList<BigFraction> generateMorePositions(List<BigFraction> positions, List<Integer> amountRange) {
        final LinkedList<BigFraction> newPositions = new LinkedList<>(positions);

        if (!amountRange.isEmpty()) {
            int min = amountRange.stream().min(Comparator.naturalOrder()).get();
            int max = 1 + amountRange.stream().max(Comparator.naturalOrder()).get();

            final List<BigFraction> randomPositions = randomUtils.getRandomPositions(min, max);
            newPositions.addAll(randomPositions);
        }

        return newPositions;
    }
}
