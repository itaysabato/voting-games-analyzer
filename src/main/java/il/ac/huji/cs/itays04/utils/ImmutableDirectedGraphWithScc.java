package il.ac.huji.cs.itays04.utils;

public class ImmutableDirectedGraphWithScc<T> {
    private final ImmutableDirectedGraph<T> originalGraph;
    private final ImmutableDirectedGraph<StronglyConnectedComponent<T>> sccGraph;

    public ImmutableDirectedGraphWithScc(
            ImmutableDirectedGraph<T> originalGraph,
            ImmutableDirectedGraph<StronglyConnectedComponent<T>> sccGraph) {

        this.originalGraph = originalGraph;
        this.sccGraph = sccGraph;
    }

    public ImmutableDirectedGraph<T> getOriginalGraph() {
        return originalGraph;
    }

    public ImmutableDirectedGraph<StronglyConnectedComponent<T>> getSccGraph() {
        return sccGraph;
    }
}
