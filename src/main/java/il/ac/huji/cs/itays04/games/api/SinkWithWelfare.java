package il.ac.huji.cs.itays04.games.api;

import com.google.common.collect.ImmutableList;
import il.ac.huji.cs.itays04.utils.StronglyConnectedComponent;

import java.util.Set;

public class SinkWithWelfare<T extends GameState, W extends Number & Comparable<W>> {
    private final W welfare;
    private final long longestPathToSinkLength;
    private final StronglyConnectedComponent<T> sink;
    private final ImmutableList<StronglyConnectedComponent<T>> longestPathToSink;

    public SinkWithWelfare(W welfare, StronglyConnectedComponent<T> sink, ImmutableList<StronglyConnectedComponent<T>> longestPathToSink) {
        this.welfare = welfare;
        this.sink = sink;
        this.longestPathToSink = longestPathToSink;

        longestPathToSinkLength = longestPathToSink.subList(0, longestPathToSink.size() - 1)
                .stream()
                .map(StronglyConnectedComponent::getNodes)
                .mapToInt(Set::size)
                .sum();
    }

    public W getWelfare() {
        return welfare;
    }

    public long getLongestPathToSinkLength() {
        return longestPathToSinkLength;
    }

    public StronglyConnectedComponent<T> getSink() {
        return sink;
    }

    public ImmutableList<StronglyConnectedComponent<T>> getLongestPathToSink() {
        return longestPathToSink;
    }
}
