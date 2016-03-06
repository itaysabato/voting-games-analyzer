package il.ac.huji.cs.itays04.games.api;

import java.util.Collection;
import java.util.Set;

public interface BestResponseGraph<T extends GameState<T>> {
    Set<T> getStates();
    Set<T> getChildren(T state);
    Set<T> getParents(T state);

    Collection<StronglyConnectedComponent> getStronglyConnectedComponents();

    interface StronglyConnectedComponent<T extends GameState<T>> {
        Set<T> getStates();
        Set<StronglyConnectedComponent> getChildren();
        Set<StronglyConnectedComponent> getParents();
    }
}
