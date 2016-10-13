package il.ac.huji.cs.itays04.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import il.ac.huji.cs.itays04.rational.NamedRationalEntity;
import il.ac.huji.cs.itays04.rational.RandomUtils;
import il.ac.huji.cs.itays04.rational.RationalAggregator;
import il.ac.huji.cs.itays04.rational.RationalUtils;
import il.ac.huji.cs.itays04.voting.quadratic.AnalysisWithRandomDicComparison;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

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
            validateVoters(arguments);
            validateCandidates(arguments);
        }
        catch (ParameterException e) {
            System.out.println("Invalid command line arguments: " + e.getMessage());
            jCommander.usage();
            return;
        }

        if (arguments.isHelp()) {
            jCommander.usage();
        }
        else {
            run(arguments);
        }
    }

    private void validateVoters(Arguments arguments) {
        validatePositions(arguments.getVoters(), arguments.isRandomize(), arguments.getRandomVotersRange(),
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
        List<BigFraction> voterPositions = arguments.getVoters();

        final boolean noCandidates = arguments.isNoCandidates();
        List<BigFraction> candidatePositions = arguments.getCandidates();

        if (arguments.isRandomize()) {
            voterPositions = generateMorePositions(arguments.getVoters(), arguments.getRandomVotersRange());

            if (!noCandidates) {
                candidatePositions = generateMorePositions(arguments.getCandidates(), arguments.getRandomCandidatesRange());
            }
        }

        final LinkedHashSet<NamedRationalEntity> voters = rationalUtils.toVoters(voterPositions);
        final LinkedHashSet<NamedRationalEntity> candidates = noCandidates ?
                voters : rationalUtils.toCandidates(candidatePositions);

        final AnalysisWithRandomDicComparison<?, BigFraction> analysis = quadraticAnalysisRunner.analyzeAndReport(
                voters,
                candidates,
                "Quadratic Voting Game " + gameIndex,
                arguments.isQuiet());

        aggregator.add(analysis);
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
