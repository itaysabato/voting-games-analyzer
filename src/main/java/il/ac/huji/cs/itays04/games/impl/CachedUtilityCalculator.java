package il.ac.huji.cs.itays04.games.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import il.ac.huji.cs.itays04.games.api.GameState;
import il.ac.huji.cs.itays04.games.api.UtilityCalculator;

import java.util.Objects;

public class CachedUtilityCalculator<T extends GameState<T>, U extends Comparable<U>> implements UtilityCalculator<T,U> {

    private final LoadingCache<StateAndPlayer<T>, U> utilityCache;

    public CachedUtilityCalculator(UtilityCalculator<T, U> utilityCalculator, int maxCacheSize) {

        utilityCache = CacheBuilder.newBuilder()
                .maximumSize(maxCacheSize)
                .build(new CacheLoader<StateAndPlayer<T>, U>() {

                    @Override
                    public U load(StateAndPlayer<T> key) throws Exception {
                        return utilityCalculator.calculateUtility(key.state, key.player);
                    }
                });
    }

    @Override
    public U calculateUtility(T gameState, int playerIndex) {
        return utilityCache.getUnchecked(new StateAndPlayer<>(gameState, playerIndex));
    }

    private static class StateAndPlayer<T extends GameState<T>> {
        private final T state;
        private final int player;

        private StateAndPlayer(T state, int player) {
            this.state = state;
            this.player = player;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StateAndPlayer<?> that = (StateAndPlayer<?>) o;
            return player == that.player &&
                    Objects.equals(state, that.state);
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, player);
        }
    }
}
