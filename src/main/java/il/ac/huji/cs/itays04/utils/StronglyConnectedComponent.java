package il.ac.huji.cs.itays04.utils;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class StronglyConnectedComponent<T> {

    private final int id;
    private final ImmutableSet<T> nodes;

    public StronglyConnectedComponent(int id, ImmutableSet<T> nodes) {
        this.id = id;
        this.nodes = nodes;
    }

    public Set<T> getNodes() {
        return nodes;
    }

    public int getId() {
        return id;
    }
}
