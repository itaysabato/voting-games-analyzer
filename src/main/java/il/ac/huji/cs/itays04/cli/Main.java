package il.ac.huji.cs.itays04.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import il.ac.huji.cs.itays04.games.api.GameAnalysis;
import il.ac.huji.cs.itays04.rational.RandomUtils;
import il.ac.huji.cs.itays04.rational.RationalAggregator;
import il.ac.huji.cs.itays04.rational.RationalUtils;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Main {
    private static final Main main = new Main();

    private final Arguments arguments = new Arguments();
    private final JCommander jCommander = new JCommander(arguments);
    private final RandomUtils randomUtils = StaticContext.getInstance().randomUtils;
    private final RationalUtils rationalUtils = StaticContext.getInstance().rationalUtils;
    private final AnalysisRunner analysisRunner = StaticContext.getInstance().analysisRunner;

    private Main() {
        jCommander.setProgramName("vga");
    }

    public static void main(String[] args) {
        main.run(args);
    }

    private void run(String[] args) {
        try {
            jCommander.parse(args);
        }
        catch (ParameterException e) {
            System.out.println("Invalid command line format: " + e.getMessage());
            jCommander.usage();
            return;
        }

        if (arguments.isHelp()) {
            jCommander.usage();
        }
        else {
            final RationalAggregator aggregator = new RationalAggregator();
            int numberOfGames = arguments.getNumberOfGames() > 0 ? arguments.getNumberOfGames() : 1;

            for (int i = 1; i <= numberOfGames; i++) {
                nextGame(i, aggregator);
            }

            System.out.println(aggregator.getCurrentAggregation());
        }
    }

    private void nextGame(int gameIndex, RationalAggregator aggregator) {
        List<BigFraction> voters = arguments.getVoters();
        List<BigFraction> candidates = arguments.getCandidates();

        if (arguments.isRandomize()) {
            voters = generateMorePositions(arguments.getVoters(), arguments.getRandomVotersRange());
            candidates = generateMorePositions(arguments.getCandidates(), arguments.getRandomCandidatesRange());
        }

        final GameAnalysis<?, BigFraction> analysis = analysisRunner.analyzeAndReport(
                rationalUtils.toVoters(voters),
                rationalUtils.toCandidates(candidates),
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
