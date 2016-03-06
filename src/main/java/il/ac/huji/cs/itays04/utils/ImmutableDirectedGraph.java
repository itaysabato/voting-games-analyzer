package il.ac.huji.cs.itays04.utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;

import java.util.Collection;
import java.util.Set;

public class ImmutableDirectedGraph<T> {
    private final ImmutableSet<T> nodes;
    private final ImmutableSetMultimap<T,T> edges;
    private final ImmutableSetMultimap<T,T> backEdges;

    public ImmutableDirectedGraph(
            Collection<T> nodes,
            Multimap<T, T> edges,
            Multimap<T, T> backEdges) {

        this.nodes = ImmutableSet.copyOf(nodes);
        this.edges = ImmutableSetMultimap.copyOf(edges);
        this.backEdges = ImmutableSetMultimap.copyOf(backEdges);
    }

    public Set<T> getNodes() {
        return nodes;
    }

    public SetMultimap<T, T> getEdges() {
        return edges;
    }

    public SetMultimap<T, T> getBackEdges() {
        return backEdges;
    }
}
