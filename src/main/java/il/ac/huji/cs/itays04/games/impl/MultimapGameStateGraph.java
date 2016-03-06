package il.ac.huji.cs.itays04.games.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import il.ac.huji.cs.itays04.games.api.GameState;
import il.ac.huji.cs.itays04.games.api.GameStateGraph;

import java.util.Collection;
import java.util.Set;

public class MultimapGameStateGraph<T extends GameState<T>> implements GameStateGraph<T> {
    private final ImmutableSet<T> states;
    private final SetMultimap<T,T> edges;
    private final SetMultimap<T,T> backEdges;
    private final Collection<StronglyConnectedComponent> stronglyConnectedComponents;

    public MultimapGameStateGraph(ImmutableSet<T> states, SetMultimap<T, T> edges, SetMultimap<T, T> backEdges,
                                  Collection<StronglyConnectedComponent> stronglyConnectedComponents) {
        this.states = states;
        this.edges = edges;
        this.backEdges = backEdges;
        this.stronglyConnectedComponents = stronglyConnectedComponents;
    }

    @Override
    public Set<T> getStates() {
        return states;
    }

    @Override
    public Set<T> getChildren(T state) {
        return edges.get(state);
    }

    @Override
    public Set<T> getParents(T state) {
        return backEdges.get(state);
    }

    @Override
    public Collection<StronglyConnectedComponent> getStronglyConnectedComponents() {
        return stronglyConnectedComponents;
    }
}
