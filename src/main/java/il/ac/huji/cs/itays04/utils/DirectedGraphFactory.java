package il.ac.huji.cs.itays04.utils;

public interface DirectedGraphFactory {
    <T> ImmutableDirectedGraphWithScc<T> createImmutableDirectedGraphWithScc(ImmutableDirectedGraph<T> originalGraph);
}
