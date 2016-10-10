package il.ac.huji.cs.itays04.cli;

import il.ac.huji.cs.itays04.games.api.GameAnalysisReporter;
import il.ac.huji.cs.itays04.games.api.GameAnalyzer;
import il.ac.huji.cs.itays04.games.impl.SimpleGameAnalysisReporter;
import il.ac.huji.cs.itays04.games.impl.SimpleGameAnalyzer;
import il.ac.huji.cs.itays04.games.impl.SimpleGameTraverser;
import il.ac.huji.cs.itays04.utils.DirectedGraphFactory;
import il.ac.huji.cs.itays04.utils.PrincetonDirectedGraphFactory;
import il.ac.huji.cs.itays04.utils.RandomUtils;
import il.ac.huji.cs.itays04.utils.RationalUtils;
import il.ac.huji.cs.itays04.voting.quadratic.QuadraticFactory;

public class StaticContext {
    private static final StaticContext ourInstance = new StaticContext();

    public final RationalUtils rationalUtils = new RationalUtils();
    public final RandomUtils randomUtils = new RandomUtils(rationalUtils);
    public final QuadraticFactory quadraticFactory = new QuadraticFactory();
    public final AnalysisRunner analysisRunner = new AnalysisRunner(quadraticFactory);
    public final SimpleGameTraverser simpleGameTraverser = new SimpleGameTraverser();
    public final GameAnalysisReporter gameAnalysisReporter = new SimpleGameAnalysisReporter();
    public final DirectedGraphFactory directedGraphFactory = new PrincetonDirectedGraphFactory();
    public final GameAnalyzer gameAnalyzer = new SimpleGameAnalyzer(simpleGameTraverser, directedGraphFactory);

    private StaticContext() {
    }

    public static StaticContext getInstance() {
        return ourInstance;
    }
}
