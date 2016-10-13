package il.ac.huji.cs.itays04.games.impl;

import il.ac.huji.cs.itays04.games.api.*;
import il.ac.huji.cs.itays04.rational.NumberUtils;
import il.ac.huji.cs.itays04.utils.ImmutableDirectedGraphWithScc;
import il.ac.huji.cs.itays04.utils.StronglyConnectedComponent;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleGameAnalysisReporter implements GameAnalysisReporter {

    private static final String N_A = "N/A";

    @Override
    public <T extends GameState, W extends Number & Comparable<W>> void printReport(
            Game<T, ?, W> game, GameAnalysis<T, W> gameAnalysis, PrintStream printStream) {

        printStream.println("********************************");
        printStream.println("Game Analysis");
        printStream.println("********************************");
        printStream.println(game);
        printStream.println();

        final ImmutableDirectedGraphWithScc<T> bestResponseGraph = gameAnalysis.getBestResponseGraph();
        printStream.println("Number of possible states: " + bestResponseGraph.getOriginalGraph().getNodes().size());
        printStream.println("Number of strongly connected components: " + bestResponseGraph.getSccGraph().getNodes().size());
        printStream.println("Number of components with cycles: " + gameAnalysis.getNumberOfNonSingularSccs());
        printStream.println("Number of pure Nash equilibria: " + gameAnalysis.getNeCount());
        printStream.println("Number of sink-equilibria: " + gameAnalysis.getSinksWithWelfare().size());
        printStream.println();

        final GamePrices<W> prices = gameAnalysis.getPrices();
        printWelfareWithLabel(printStream, "Social optimum", prices.getSocialOptimum());

        final Optional<W> priceOfAnarchy = prices.getPriceOfAnarchy();
        printStream.print("Price of Anarchy: ");

        if (priceOfAnarchy.isPresent()) {
            printStream.println(NumberUtils.format(priceOfAnarchy.get()));
        }
        else {
            printStream.println(N_A);
        }

        printWelfareWithLabel(printStream, "Price of Sinking", prices.getPriceOfSinking());

        final Optional<W> priceOfStability = prices.getPriceOfStability();
        printStream.print("Price of Stability: ");

        if (priceOfStability.isPresent()) {
            printStream.println(NumberUtils.format(priceOfStability.get()));
        }
        else {
            printStream.println(N_A);
        }

        printStream.println();
        printStream.println("Socially optimal states:");
        int k = 1;
        for (T state : gameAnalysis.getOptimalStates()) {
            printStream.println(k++ + " - " + state);
        }

        printStream.println("---------------------------------");
        printStream.println("Sinks");
        printStream.println("---------------------------------");

        int j = 1;
        for (SinkWithWelfare<T, W> sinkWithWelfare : gameAnalysis.getSinksWithWelfare()) {
            final StronglyConnectedComponent<T> sink = sinkWithWelfare.getSink();
            final Set<T> states = sink.getNodes();

            printStream.println("Sink #" + j++);
            printStream.println("Component id: " + sink.getId());

            printStream.println("Number of states:" + states.size());

            final W welfare = sinkWithWelfare.getWelfare();
            printWelfareWithLabel(printStream, "Average welfare", welfare);

            final W ratio = game.getSocialWelfareCalculator().getRatio(prices.getSocialOptimum(), welfare);
            printWelfareWithLabel(printStream, "Ratio to social optimum", ratio);

            printStream.println();

            printStream.println("States:");
            int i = 1;
            final Map<T, Integer> stateToId = new HashMap<>(states.size());

            for (T state : states) {
                printStream.println("S" + i + " - " + state);
                stateToId.put(state, i);
                i++;
            }
            printStream.println();

            printStream.println("Sink edges:");

            final Stream<Map.Entry<T, T>> sinkEdges = bestResponseGraph
                    .getOriginalGraph()
                    .getEdges()
                    .entries()
                    .stream()
                    .filter(e -> states.contains(e.getKey()))
                    //todo: perhaps should be optional to improve efficiency:
                    .sorted((e1, e2) -> stateToId.get(e1.getKey()) - stateToId.get(e2.getKey()));

            final AtomicBoolean hasEdges = new AtomicBoolean(false);
            sinkEdges.forEachOrdered(edge -> {
                hasEdges.set(true);
                final Integer source = stateToId.get(edge.getKey());
                final Integer target = stateToId.get(edge.getValue());
                printStream.println("S" + source + " => " + "S" + target);
            });

            if (!hasEdges.get()) {
                printStream.println(N_A);
                printStream.println();
            }

            printStream.println("Longest path to sink length: " + sinkWithWelfare.getLongestPathToSinkLength());
            final String path = sinkWithWelfare.getLongestPathToSink()
                    .stream()
                    .sequential()
                    .map(scc -> {
                        if (scc.getNodes().size() == 1) {
                            return scc.getNodes().iterator().next().toString();
                        }
                        else {
                            return "SCC#" + scc.getId();
                        }
                    }).collect(Collectors.joining(" => "));

            printStream.println("Longest path to sink: " + path);

            printStream.println("---------------------------------");
        }
        printStream.println();
    }

    private void printWelfareWithLabel(PrintStream printStream, String label, Number value) {
        printStream.println(label + ": " + NumberUtils.format(value));
    }
}
