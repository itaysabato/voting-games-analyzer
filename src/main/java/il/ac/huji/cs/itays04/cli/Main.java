package il.ac.huji.cs.itays04.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.google.common.collect.ImmutableSet;

public class Main {
    private static final Main main = new Main();

    private final Arguments arguments = new Arguments();
    private final JCommander jCommander = new JCommander(arguments);
    private final AnalysisRunner analysisRunner = new AnalysisRunner();

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
            analysisRunner.analyzeAndReport(
                    arguments.getVoters(),
                    //todo: support candidate duplicates:
                    ImmutableSet.copyOf(arguments.getCandidates()),
                    "Quadratic Voting Game");
        }
    }
}
