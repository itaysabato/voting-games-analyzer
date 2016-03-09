package il.ac.huji.cs.itays04.games.impl;

import il.ac.huji.cs.itays04.games.api.*;
import il.ac.huji.cs.itays04.utils.StronglyConnectedComponent;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SimpleGameAnalysisReporter implements GameAnalysisReporter {
    @Override
    public <T extends GameState<T>, W extends Comparable<W>> void printReport(
            Game<T> game, GameAnalysis<T, W> gameAnalysis, PrintStream printStream) {

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
        printStream.println("Optimal social welfare: " + prices.getSocialOptimum());
        printStream.println("Price of Anarchy: " + prices.getPriceOfAnarchy());
        printStream.println("Price of Sinking: " + prices.getPriceOfSinking());
        printStream.println("Price of Stability: " + prices.getPriceOfStability());
        printStream.println();

        printStream.println("---------------------------------");
        printStream.println("Sinks:");
        printStream.println("---------------------------------");

        for (Map.Entry<StronglyConnectedComponent<T>, W> entry : gameAnalysis.getSinksWithWelfare().entrySet()) {
            final StronglyConnectedComponent<T> sink = entry.getKey();
            final Set<T> states = sink.getNodes();

            printStream.println("Number of states:" + states.size());
            printStream.println("Average welfare: " + entry.getValue());
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
                    .forEach(edge -> {
                        final Integer source = stateToId.get(edge.getKey());
                        final Integer target = stateToId.get(edge.getValue());
                        printStream.println(source + " => " + target);
                    });


            printStream.println("---------------------------------");
        }
    }
}
