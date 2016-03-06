package il.ac.huji.cs.itays04.utils;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.TarjanSCC;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PrincetonDirectedGraphFactory implements DirectedGraphFactory {

    @Override
    public <T> ImmutableDirectedGraphWithScc<T> createImmutableDirectedGraphWithScc(ImmutableDirectedGraph<T> originalGraph) {

        final HashBiMap<T, Integer> nodeToId = generateNodeIds(originalGraph);

        final TarjanSCC tarjanSCC = computeTarjanSCC(originalGraph, nodeToId);

        final HashMap<Integer, StronglyConnectedComponent<T>> sccMap =
                createSccMap(originalGraph, nodeToId, tarjanSCC);

        final ImmutableDirectedGraph<StronglyConnectedComponent<T>> sccGraph =
                addSccEdges(originalGraph, nodeToId, tarjanSCC, sccMap);

        return new ImmutableDirectedGraphWithScc<>(originalGraph, sccGraph);
    }

    private <T> ImmutableDirectedGraph<StronglyConnectedComponent<T>> addSccEdges(ImmutableDirectedGraph<T> originalGraph, HashBiMap<T, Integer> nodeToId, TarjanSCC tarjanSCC, HashMap<Integer, StronglyConnectedComponent<T>> sccMap) {
        final HashMultimap<StronglyConnectedComponent<T>, StronglyConnectedComponent<T>> sccEdges = HashMultimap.create();
        final HashMultimap<StronglyConnectedComponent<T>, StronglyConnectedComponent<T>> sccBackEdges = HashMultimap.create();

        for (Map.Entry<Integer, StronglyConnectedComponent<T>> entry : sccMap.entrySet()) {
            final int sccId = entry.getKey();
            final StronglyConnectedComponent<T> scc = entry.getValue();

            for (T node : scc.getNodes()) {
                for (T child : originalGraph.getEdges().get(node)) {
                    final int childSccId = getSccId(nodeToId, tarjanSCC, child);

                    if (sccId != childSccId) {
                        final StronglyConnectedComponent<T> childScc = sccMap.get(childSccId);

                        sccEdges.put(scc, childScc);
                        sccBackEdges.put(childScc, scc);
                    }
                }
            }
        }

        return new ImmutableDirectedGraph<>(sccMap.values(), sccEdges, sccBackEdges);
    }

    private <T> HashMap<Integer, StronglyConnectedComponent<T>> createSccMap(ImmutableDirectedGraph<T> originalGraph, HashBiMap<T, Integer> nodeToId, TarjanSCC tarjanSCC) {
        final HashMultimap<Integer, T> sccMultimap = HashMultimap.create();

        for (T node : originalGraph.getNodes()) {
            final int sccId = getSccId(nodeToId, tarjanSCC, node);
            sccMultimap.put(sccId, node);
        }

        final HashMap<Integer,StronglyConnectedComponent<T>> sccMap = new HashMap<>(sccMultimap.size());

        for (Map.Entry<Integer, Collection<T>> entry : sccMultimap.asMap().entrySet()) {

            final ImmutableSet<T> copy = ImmutableSet.copyOf(entry.getValue());
            final StronglyConnectedComponent<T> scc = new StronglyConnectedComponent<>(copy);
            sccMap.put(entry.getKey(), scc);
        }
        return sccMap;
    }

    private <T> TarjanSCC computeTarjanSCC(ImmutableDirectedGraph<T> originalGraph, HashBiMap<T, Integer> nodeToId) {
        final Digraph digraph = new Digraph(originalGraph.getNodes().size());

        for (Map.Entry<T, T> entry : originalGraph.getEdges().entries()) {
            final int source = get(nodeToId, entry.getKey());
            final int target = get(nodeToId, entry.getValue());
            digraph.addEdge(source, target);
        }

        return new TarjanSCC(digraph);
    }

    private <T> HashBiMap<T, Integer> generateNodeIds(ImmutableDirectedGraph<T> originalGraph) {
        final HashBiMap<T, Integer> nodeToId = HashBiMap.create(originalGraph.getNodes().size());

        int i = 0;
        for (T node : originalGraph.getNodes()) {
            nodeToId.put(node, i++);
        }
        return nodeToId;
    }

    private <T> int getSccId(HashBiMap<T, Integer> nodeToId, TarjanSCC tarjanSCC, T node) {
        final Integer nodeId = get(nodeToId, node);
        return tarjanSCC.id(nodeId);
    }

    private <T> Integer get(HashBiMap<T, Integer> nodeToId, T node) {
        return nodeToId.getOrDefault(node, -1);
    }

}
