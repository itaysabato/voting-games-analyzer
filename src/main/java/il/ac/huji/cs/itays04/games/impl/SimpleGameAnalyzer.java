package il.ac.huji.cs.itays04.games.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import edu.princeton.cs.algs4.AcyclicLP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import il.ac.huji.cs.itays04.games.api.*;
import il.ac.huji.cs.itays04.utils.DirectedGraphFactory;
import il.ac.huji.cs.itays04.utils.ImmutableDirectedGraph;
import il.ac.huji.cs.itays04.utils.ImmutableDirectedGraphWithScc;
import il.ac.huji.cs.itays04.utils.StronglyConnectedComponent;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SimpleGameAnalyzer implements GameAnalyzer {

    private final SimpleGameTraverser simpleGameTraverser;
    private final DirectedGraphFactory directedGraphFactory;

    public SimpleGameAnalyzer(SimpleGameTraverser simpleGameTraverser, DirectedGraphFactory directedGraphFactory) {
        this.simpleGameTraverser = simpleGameTraverser;
        this.directedGraphFactory = directedGraphFactory;
    }

    @Override
    public <T extends GameState<T>, U extends Number & Comparable<U>> ImmutableDirectedGraphWithScc<T> calculateBestResponseGraph(
            Game<T, U, ?> game) {

        final ImmutableDirectedGraph<T> originalGraph = createBestResponseGraph(game);
        return directedGraphFactory.createImmutableDirectedGraphWithScc(originalGraph);
    }

    public <T extends GameState<T>, U extends Number & Comparable<U>>
    ImmutableDirectedGraph<T> createBestResponseGraph(Game<T, U, ?> game) {

        final HashSet<T> states = new HashSet<>();
        simpleGameTraverser.traverseGameUntil(game, states, s -> false);

        final HashMultimap<T, T> edges = HashMultimap.create();
        final HashMultimap<T, T> backEdges = HashMultimap.create();

        for (T state : states) {
            final Set<T> improvements = getImprovements(state, game);
            edges.putAll(state, improvements);

            for (T improvement : improvements) {
                backEdges.put(improvement, state);
            }
        }

        return new ImmutableDirectedGraph<>(states, edges, backEdges);
    }

    private <T extends GameState<T>, U extends Number & Comparable<U>>
    Set<T> getImprovements(T state, Game<T, U, ?> game) {

        final HashSet<T> improvements = new HashSet<>();

        for (int i = 0; i < game.getNumberOfPlayers(); i++) {
            game.getUtilityCalculator().streamImprovements(game, state, i)
                    .collect(Collectors.toCollection(()->improvements));
        }

        return improvements;
    }

    @Override
    public <T extends GameState<T>, W extends Number & Comparable<W>> GameAnalysis<T, W> analyze(
            Game<T, ?, W> game,
            ImmutableDirectedGraphWithScc<T> brg) {

        W socialOptimum = findSocialOptimum(game, brg);
        final Set<StronglyConnectedComponent<T>> sinks = collectSinks(brg);


        return calculatePrices(game, socialOptimum, brg, sinks);
    }

    private <T extends GameState<T>, U extends Number & Comparable<U>, W extends Number & Comparable<W>> GameAnalysis<T, W> calculatePrices(
            Game<T, U, W> game,
            W socialOptimum,
            ImmutableDirectedGraphWithScc<T> brg,
            Set<StronglyConnectedComponent<T>> sinks) {

        final SocialWelfareCalculator<T, U, W> calculator = game.getSocialWelfareCalculator();
        final HashSet<SinkWithWelfare<T, W>> sinksWithWelfare = new HashSet<>();

        Optional<W> worstRatio = Optional.empty(),
                worstNeRatio = Optional.empty(),
                bestNeRatio = Optional.empty();

        for (StronglyConnectedComponent<T> sink : sinks) {
            final Set<T> nodes = sink.getNodes();
            final W sinkWelfare = calculator.calculateAverageWelfare(game, nodes);

            final ImmutableList<StronglyConnectedComponent<T>> longestPath = calculateLongestPath(brg, sink);
            sinksWithWelfare.add(new SinkWithWelfare<>(sinkWelfare, sink, longestPath));



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

        final long neCount = sinksWithWelfare.stream()
                .filter(sink -> sink.getSink().getNodes().size() == 1)
                .count();

        return new GameAnalysis<>(neCount, gamePrices, brg, sinksWithWelfare);
    }

    private <T extends GameState<T>> ImmutableList<StronglyConnectedComponent<T>> calculateLongestPath(
            ImmutableDirectedGraphWithScc<T> brg, StronglyConnectedComponent<T> sink) {

        final ImmutableDirectedGraph<StronglyConnectedComponent<T>> sccGraph = brg.getSccGraph();
        final Set<StronglyConnectedComponent<T>> sccSet = sccGraph.getNodes();

        final BiMap<Integer, StronglyConnectedComponent<T>> idToNode = HashBiMap.create(sccSet.size());
        final Map<StronglyConnectedComponent<T>, Double> weights = new HashMap<>();

        for (StronglyConnectedComponent<T> scc : sccSet) {
            idToNode.put(scc.getId(), scc);
            weights.put(scc, (double) scc.getNodes().size());
        }

        return getLongestPathToSink(sink, weights, idToNode, sccGraph);
    }

    private <E> ImmutableList<E> getLongestPathToSink(
            E sink,
            Map<E, Double> weights,
            BiMap<Integer, E> idToNode,
            ImmutableDirectedGraph<E> sccGraph) {

        final BiMap<E, Integer> nodeToId = idToNode.inverse();

        final Set<E> sccSet = sccGraph.getNodes();
        final EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(sccSet.size());

        for (Map.Entry<E, E> edge : sccGraph.getEdges().entries()) {
            final E originalSource = edge.getKey();
            final E originalTarget = edge.getValue();

            final DirectedEdge reverseEdge = new DirectedEdge(
                    nodeToId.get(originalTarget),
                    nodeToId.get(originalSource),
                    weights.get(originalSource));

            edgeWeightedDigraph.addEdge(reverseEdge);
        }

        final AcyclicLP acyclicLP = new AcyclicLP(edgeWeightedDigraph, nodeToId.get(sink));

        final Optional<Iterable<DirectedEdge>> longestPath = sccSet.stream()
                .sequential()
                .map(nodeToId::get)
                .max(Comparator.comparingDouble(acyclicLP::distTo))
                .map(acyclicLP::pathTo);

        //todo: if the whole graph is strongly connected, this will fail:
        final List<E> path = StreamSupport.stream(longestPath.get().spliterator(), false)
                .map(DirectedEdge::to)
                .map(idToNode::get)
                .collect(Collectors.toCollection(ArrayList::new));

        Collections.reverse(path);
        path.add(sink);

        return ImmutableList.copyOf(path);
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

    private <T extends GameState<T>, U extends Number & Comparable<U>, W extends Number & Comparable<W>>
    W findSocialOptimum(Game<T, U, W> game, ImmutableDirectedGraphWithScc<T> brg) {

        return brg.getOriginalGraph()
                .getNodes()
                .stream()
                .sequential()
                .map(s -> game.getSocialWelfareCalculator().calculateWelfare(game, s))
                .max(Comparator.naturalOrder())
                .get();
    }
}
