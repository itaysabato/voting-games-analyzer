package il.ac.huji.cs.itays04.voting;

import il.ac.huji.cs.itays04.games.api.GameAnalysis;
import il.ac.huji.cs.itays04.games.api.GameState;

import java.util.Optional;

public class AnalysisWithRandomDicComparison<T extends GameState, W extends Number & Comparable<W>> {
    private final Optional<W> randomDicPoSRatio;
    private final GameAnalysis<T, W> gameAnalysis;

    public AnalysisWithRandomDicComparison(Optional<W> randomDicPoSRatio, GameAnalysis<T, W> gameAnalysis) {
        this.randomDicPoSRatio = randomDicPoSRatio;
        this.gameAnalysis = gameAnalysis;
    }

    public Optional<W> getRandomDicPoSRatio() {
        return randomDicPoSRatio;
    }

    public GameAnalysis<T, W> getGameAnalysis() {
        return gameAnalysis;
    }

}
