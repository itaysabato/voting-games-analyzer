package il.ac.huji.cs.itays04.games.api;

import com.google.common.collect.ImmutableSet;
import il.ac.huji.cs.itays04.utils.ImmutableDirectedGraphWithScc;
import il.ac.huji.cs.itays04.utils.StronglyConnectedComponent;

import java.util.Set;

public class GameAnalysis<T extends GameState<T>, W extends Number & Comparable<W>> {
    private final long neCount;
    private final GamePrices<W> prices;
    private final long numberOfNonSingularSccs;
    private final ImmutableDirectedGraphWithScc<T> bestResponseGraph;
    private final ImmutableSet<SinkWithWelfare<T,W>> sinksWithWelfare;
    private final ImmutableSet<T> optimalStates;

    public GameAnalysis(
            long neCount,
            GamePrices<W> prices,
            ImmutableDirectedGraphWithScc<T> bestResponseGraph,
            Set<SinkWithWelfare<T, W>> sinksWithWelfare,
            Set<T> optimalStates) {

        this.prices = prices;
        this.neCount = neCount;
        this.bestResponseGraph = bestResponseGraph;
        this.sinksWithWelfare = ImmutableSet.copyOf(sinksWithWelfare);
        this.numberOfNonSingularSccs = bestResponseGraph.getSccGraph()
                .getNodes()
                .stream()
                .map(StronglyConnectedComponent::getNodes)
                .mapToInt(Set::size)
                .filter(s -> s > 1)
                .count();

        this.optimalStates = ImmutableSet.copyOf(optimalStates);

    }

    public long getNeCount() {
        return neCount;
    }

    public GamePrices<W> getPrices() {
        return prices;
    }

    public long getNumberOfNonSingularSccs() {
        return numberOfNonSingularSccs;
    }

    public ImmutableDirectedGraphWithScc<T> getBestResponseGraph() {
        return bestResponseGraph;
    }

    public Set<SinkWithWelfare<T, W>> getSinksWithWelfare() {
        return sinksWithWelfare;
    }

    public Set<T> getOptimalStates() {
        return optimalStates;
    }
}
