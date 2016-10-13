package il.ac.huji.cs.itays04.games.api;

import java.io.PrintStream;

public interface GameAnalysisReporter {
    <T extends GameState, W extends Number & Comparable<W>> void printReport(
            Game<T, ?, W> game,
            GameAnalysis<T, W> gameAnalysis,
            PrintStream printStream);
}
