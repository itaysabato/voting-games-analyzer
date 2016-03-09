package il.ac.huji.cs.itays04.games.impl;

import il.ac.huji.cs.itays04.games.api.GameAnalysisReporter;
import il.ac.huji.cs.itays04.games.api.GameAnalyzer;
import il.ac.huji.cs.itays04.utils.DirectedGraphFactory;
import il.ac.huji.cs.itays04.utils.PrincetonDirectedGraphFactory;
import il.ac.huji.cs.itays04.voting.quadratic.QuadraticFactory;

public class StaticContext {
    private static final StaticContext ourInstance = new StaticContext();

    private final QuadraticFactory quadraticFactory = new QuadraticFactory();
    private final SimpleGameTraverser simpleGameTraverser = new SimpleGameTraverser();
    private final GameAnalysisReporter gameAnalysisReporter = new SimpleGameAnalysisReporter();
    private final DirectedGraphFactory directedGraphFactory = new PrincetonDirectedGraphFactory();
    private final GameAnalyzer gameAnalyzer = new SimpleGameAnalyzer(simpleGameTraverser, directedGraphFactory);

    private StaticContext() {
    }

    public static StaticContext getInstance() {
        return ourInstance;
    }

    public QuadraticFactory getQuadraticFactory() {
        return quadraticFactory;
    }

    public SimpleGameTraverser getSimpleGameTraverser() {
        return simpleGameTraverser;
    }

    public DirectedGraphFactory getDirectedGraphFactory() {
        return directedGraphFactory;
    }

    public GameAnalyzer getGameAnalyzer() {
        return gameAnalyzer;
    }

    public GameAnalysisReporter getGameAnalysisReporter() {
        return gameAnalysisReporter;
    }
}
