package il.ac.huji.cs.itays04.games.impl;

import il.ac.huji.cs.itays04.games.api.*;
import il.ac.huji.cs.itays04.utils.StronglyConnectedComponent;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SimpleGameAnalysisReporter implements GameAnalysisReporter {

    @Override
    public <T extends GameState<T>, W extends Number & Comparable<W>> void printReport(
            Game<T, ?, W> game, GameAnalysis<T, W> gameAnalysis, PrintStream printStream) {

        printStream.println("********************************");
        printStream.println("Game Analysis");
        printStream.println("********************************");
        printStream.println(game);
        printStream.println();

        printStream.println("Number of possible states: " + gameAnalysis.getBestResponseGraph().getOriginalGraph().getNodes().size());
        printStream.println("Number of pure Nash equilibria: " + gameAnalysis.getNeCount());
        printStream.println("Number of sink-equilibria: " + gameAnalysis.getSinksWithWelfare().keySet().size());
        printStream.println();

        final GamePrices<W> prices = gameAnalysis.getPrices();
        printWelfareWithLabel(printStream, "Social optimum", prices.getSocialOptimum());

        final Optional<W> priceOfAnarchy = prices.getPriceOfAnarchy();
        printStream.print("Price of Anarchy: ");

        if (priceOfAnarchy.isPresent()) {
            printStream.println(priceOfAnarchy.get() + " (" + priceOfAnarchy.get().doubleValue() + ")");
        }
        else {
            printStream.println("N/A");
        }

        printWelfareWithLabel(printStream, "Price of Sinking", prices.getPriceOfSinking());

        final Optional<W> priceOfStability = prices.getPriceOfStability();
        printStream.print("Price of Stability: ");

        if (priceOfStability.isPresent()) {
            printStream.println(priceOfStability.get() + " (" + priceOfStability.get().doubleValue() + ")");
        }
        else {
            printStream.println("N/A");
        }

        printStream.println("---------------------------------");
        printStream.println("Sinks");
        printStream.println("---------------------------------");

        int j = 1;
        for (Map.Entry<StronglyConnectedComponent<T>, W> entry : gameAnalysis.getSinksWithWelfare().entrySet()) {
            final StronglyConnectedComponent<T> sink = entry.getKey();
            final Set<T> states = sink.getNodes();

            printStream.println("Sink #" + j++);
            printStream.println();

            printStream.println("Number of states:" + states.size());
            printWelfareWithLabel(printStream, "Average welfare", entry.getValue());

            printStream.println();

            printStream.println("States:");
            int i = 1;
            final Map<T, Integer> stateToId = new HashMap<>(states.size());

            for (T state : states) {
                printStream.println(i + " - " + state);
                stateToId.put(state, i);
                i++;
            }
            printStream.println();

            printStream.println("Sink edges:");

            gameAnalysis.getBestResponseGraph()
                    .getOriginalGraph()
                    .getEdges()
                    .entries()
                    .stream()
                    .filter(e -> states.contains(e.getKey()))
                    //todo: perhaps should be optional to improve efficiency:
                    .sorted((e1,e2) -> stateToId.get(e1.getKey()) - stateToId.get(e2.getKey()))
                    .forEachOrdered(edge -> {
                        final Integer source = stateToId.get(edge.getKey());
                        final Integer target = stateToId.get(edge.getValue());
                        printStream.println(source + " => " + target);
                    });


            printStream.println("---------------------------------");
        }
    }

    public <W extends Number & Comparable<W>> void printWelfareWithLabel(PrintStream printStream, String label, W value) {
        printStream.println(label + ": " + value + " (" + value.doubleValue() + ")" );
    }
}
