package il.ac.huji.cs.itays04.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import il.ac.huji.cs.itays04.utils.RandomUtils;
import il.ac.huji.cs.itays04.utils.RationalUtils;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.Comparator;
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
            generateMorePositions(arguments.getVoters(), arguments.getRandomVotersRange());
            generateMorePositions(arguments.getCandidates(), arguments.getRandomCandidatesRange());

            analysisRunner.analyzeAndReport(
                    rationalUtils.toVoters(arguments.getVoters()),
                    rationalUtils.toCandidates(arguments.getCandidates()),
                    "Quadratic Voting Game");
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private void generateMorePositions(List<BigFraction> positions, List<Integer> amountRange) {
        if (!amountRange.isEmpty()) {
            int min = amountRange.stream().min(Comparator.naturalOrder()).get();
            int max = 1 + amountRange.stream().max(Comparator.naturalOrder()).get();

            final List<BigFraction> randomPositions = randomUtils.getRandomPositions(min, max);
            positions.addAll(randomPositions);
        }
    }
}
