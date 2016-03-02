package il.ac.huji.cs.itays04.ne;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.math.BigDecimal;
import java.util.Objects;

public class CachedUtilityCalculator<T extends GameState<T>> implements UtilityCalculator<T> {

    private final LoadingCache<StateAndPlayer<T>, BigDecimal> utilityCache;

    public CachedUtilityCalculator(UtilityCalculator<T> utilityCalculator, int maxCacheSize) {

        utilityCache = CacheBuilder.newBuilder()
                .maximumSize(maxCacheSize)
                .build(new CacheLoader<StateAndPlayer<T>, BigDecimal>() {

                    @Override
                    public BigDecimal load(StateAndPlayer<T> key) throws Exception {
                        return utilityCalculator.calculateUtility(key.state, key.player);
                    }
                });
    }

    @Override
    public BigDecimal calculateUtility(T gameState, int playerIndex) {
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
