package il.ac.huji.cs.itays04.games.impl;

import com.google.common.collect.HashMultimap;
import il.ac.huji.cs.itays04.games.api.*;
import il.ac.huji.cs.itays04.utils.DirectedGraphFactory;
import il.ac.huji.cs.itays04.utils.ImmutableDirectedGraph;
import il.ac.huji.cs.itays04.utils.ImmutableDirectedGraphWithScc;
import il.ac.huji.cs.itays04.utils.StronglyConnectedComponent;

import java.util.*;
import java.util.stream.Collectors;

public class SimpleGameAnalyzer implements GameAnalyzer {

    private final SimpleGameTraverser simpleGameTraverser;
    private final DirectedGraphFactory directedGraphFactory;

    public SimpleGameAnalyzer(SimpleGameTraverser simpleGameTraverser, DirectedGraphFactory directedGraphFactory) {
        this.simpleGameTraverser = simpleGameTraverser;
        this.directedGraphFactory = directedGraphFactory;
    }

    @Override
    public <T extends GameState<T>, U extends Comparable<U>> ImmutableDirectedGraphWithScc<T> calculateBestResponseGraph(
            Game<T> game,
            UtilityCalculator<T, U> utilityCalculator) {

        final ImmutableDirectedGraph<T> originalGraph = createBestResponseGraph(game, utilityCalculator);
        return directedGraphFactory.createImmutableDirectedGraphWithScc(originalGraph);
    }

    public <T extends GameState<T>, U extends Comparable<U>> ImmutableDirectedGraph<T> createBestResponseGraph(Game<T> game, UtilityCalculator<T, U> utilityCalculator) {
        final HashSet<T> states = new HashSet<>();
        simpleGameTraverser.traverseGameUntil(game, states, s -> false);

        final HashMultimap<T, T> edges = HashMultimap.create();
        final HashMultimap<T, T> backEdges = HashMultimap.create();

        for (T state : states) {
            final Set<T> improvements = getImprovements(state, game, utilityCalculator);
            edges.putAll(state, improvements);

            for (T improvement : improvements) {
                backEdges.put(improvement, state);
            }
        }

        return new ImmutableDirectedGraph<>(states, edges, backEdges);
    }

    private <T extends GameState<T>, U extends Comparable<U>> Set<T> getImprovements(T state, Game<T> game, UtilityCalculator<T, U> utilityCalculator) {
        final HashSet<T> improvements = new HashSet<>();

        for (int i = 0; i < game.getNumberOfPlayers(); i++) {
            utilityCalculator.streamImprovements(game, state, i)
                    .collect(Collectors.toCollection(()->improvements));
        }

        return improvements;
    }

    @Override
    public <T extends GameState<T>, W extends Comparable<W>> GameAnalysis<T, W> analyze(
            Game<T> game,
            ImmutableDirectedGraphWithScc<T> brg,
            SocialWelfareCalculator<T, W> calculator) {

        W socialOptimum = findSocialOptimum(game, brg, calculator);
        final Set<StronglyConnectedComponent<T>> sinks = collectSinks(brg);


        return calculatePrices(game, socialOptimum, brg, sinks, calculator);
    }

    private <T extends GameState<T>, W extends Comparable<W>> GameAnalysis<T, W> calculatePrices(
            Game<T> game,
            W socialOptimum,
            ImmutableDirectedGraphWithScc<T> brg,
            Set<StronglyConnectedComponent<T>> sinks,
            SocialWelfareCalculator<T, W> calculator) {

        final HashMap<StronglyConnectedComponent<T>, W> sinksWithWelfare = new HashMap<>();

        Optional<W> worstRatio = Optional.empty(),
                worstNeRatio = Optional.empty(),
                bestNeRatio = Optional.empty();

        for (StronglyConnectedComponent<T> sink : sinks) {
            final Set<T> nodes = sink.getNodes();

            final W sinkWelfare = calculator.calculateAverageWelfare(game, nodes);
            sinksWithWelfare.put(sink, sinkWelfare);

            final W ratio = calculator.getRatio(socialOptimum, sinkWelfare);
            final Optional<W> optionalRatio = Optional.of(ratio);

            if (nodes.size() == 1) {
                if (bestNeRatio.isPresent()) {
                    if (ratio.compareTo(bestNeRatio.get()) > 0) {
                        bestNeRatio = optionalRatio;
                    }

                    if (ratio.compareTo(worstNeRatio.get()) < 0) {
                        worstNeRatio = optionalRatio;
                    }
                }
                else {
                    bestNeRatio = optionalRatio;
                    worstNeRatio = optionalRatio;
                }
            }

            if (worstRatio.isPresent()) {
                if (ratio.compareTo(worstRatio.get()) < 0) {
                    worstRatio = optionalRatio;
                }
            }
            else {
                worstRatio = optionalRatio;
            }
        }

        final GamePrices<W> gamePrices = new GamePrices<>(socialOptimum, worstRatio.get(), worstNeRatio, bestNeRatio);
        return new GameAnalysis<>(gamePrices, brg, sinksWithWelfare);
    }

    private <T extends GameState<T>> Set<StronglyConnectedComponent<T>> collectSinks(ImmutableDirectedGraphWithScc<T> brg) {
        final ImmutableDirectedGraph<StronglyConnectedComponent<T>> sccGraph = brg.getSccGraph();

        return sccGraph.getNodes()
                .stream()
                .sequential()
                .filter(scc -> sccGraph.getEdges()
                        .get(scc)
                        .isEmpty())
                .collect(Collectors.toCollection(HashSet::new));
    }

    private <T extends GameState<T>, W extends Comparable<W>> W findSocialOptimum(
            Game<T> game,
            ImmutableDirectedGraphWithScc<T> brg,
            SocialWelfareCalculator<T, W> calculator) {

        return brg.getOriginalGraph()
                .getNodes()
                .stream()
                .sequential()
                .map(s -> calculator.calculateWelfare(game, s))
                .max(Comparator.naturalOrder())
                .get();
    }
}
