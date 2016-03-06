package il.ac.huji.cs.itays04.utils;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class StronglyConnectedComponent<T> {
    private final ImmutableSet<T> nodes;

    public StronglyConnectedComponent(ImmutableSet<T> nodes) {
        this.nodes = nodes;
    }

    public Set<T> getNodes() {
        return nodes;
    }
}
