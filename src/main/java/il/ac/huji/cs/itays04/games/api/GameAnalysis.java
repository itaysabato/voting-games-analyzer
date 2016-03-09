package il.ac.huji.cs.itays04.games.api;

import com.google.common.collect.ImmutableMap;
import il.ac.huji.cs.itays04.utils.ImmutableDirectedGraphWithScc;
import il.ac.huji.cs.itays04.utils.StronglyConnectedComponent;

import java.util.Map;

public class GameAnalysis<T extends GameState<T>, W extends Number & Comparable<W>> {
    private final GamePrices<W> prices;
    private final ImmutableDirectedGraphWithScc<T> bestResponseGraph;
    private final ImmutableMap<StronglyConnectedComponent<T>, W> sinksWithWelfare;

    public GameAnalysis(
            GamePrices<W> prices,
            ImmutableDirectedGraphWithScc<T> bestResponseGraph,
            Map<StronglyConnectedComponent<T>, W> sinksWithWelfare) {

        this.prices = prices;
        this.bestResponseGraph = bestResponseGraph;
        this.sinksWithWelfare = ImmutableMap.copyOf(sinksWithWelfare);
    }

    public GamePrices<W> getPrices() {
        return prices;
    }

    public ImmutableDirectedGraphWithScc<T> getBestResponseGraph() {
        return bestResponseGraph;
    }

    public Map<StronglyConnectedComponent<T>, W> getSinksWithWelfare() {
        return sinksWithWelfare;
    }

    //todo: make field
    public long getNeCount() {
        return sinksWithWelfare.keySet()
                .stream()
                .filter(sink -> sink.getNodes().size() == 1)
                .count();
    }
}
