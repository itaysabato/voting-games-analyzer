package il.ac.huji.cs.itays04.games;

public interface GameAnalyzer<T extends GameState<T>> {
    BestResponseGraph<T> calculateBestResponseGraph();
}
