package il.ac.huji.cs.itays04.games.api;

import java.io.PrintStream;

public interface GameAnalysisReporter {
    <T extends GameState<T>,W extends Comparable<W>> void printReport(
            Game<T> game,
            GameAnalysis<T, W> gameAnalysis,
            PrintStream printStream);
}
