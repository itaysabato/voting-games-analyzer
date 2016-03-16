package il.ac.huji.cs.itays04.games.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import il.ac.huji.cs.itays04.games.api.BigFractionAverageSocialWelfareCalculator;
import il.ac.huji.cs.itays04.games.api.GameAnalysis;
import il.ac.huji.cs.itays04.games.api.GameAnalyzer;
import il.ac.huji.cs.itays04.utils.ImmutableDirectedGraphWithScc;
import il.ac.huji.cs.itays04.voting.VotingGame;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import il.ac.huji.cs.itays04.voting.quadratic.QuadraticFactory;
import org.apache.commons.math3.fraction.BigFraction;
import org.junit.Test;

import java.util.List;
import java.util.Set;


public class SimpleGameAnalyzerTest {

    @Test
    public void analyzeTheorem12Example() {
        final List<Integer> voterPositions = Lists.newArrayList(4, 32, 34, 48, 67);
        final Set<Integer> candidatePositions = Sets.newHashSet(14, 32, 42, 60, 93);

        analyzeAndReport(voterPositions, candidatePositions);
    }

    public void analyzeAndReport(List<Integer> voterPositions, Set<Integer> candidatePositions) {
        final QuadraticFactory quadraticFactory = StaticContext.getInstance().getQuadraticFactory();

        final BigFractionAverageSocialWelfareCalculator<VotingGameState<Integer>> welfareCalculator =
                new BigFractionAverageSocialWelfareCalculator<>();

        final VotingGame<Integer, BigFraction, BigFraction> game = quadraticFactory.createDistanceBasedGame(
                voterPositions, candidatePositions, welfareCalculator);

        final GameAnalyzer gameAnalyzer = StaticContext.getInstance().getGameAnalyzer();
        final ImmutableDirectedGraphWithScc<VotingGameState<Integer>> brg = gameAnalyzer.calculateBestResponseGraph(game);

        final GameAnalysis<VotingGameState<Integer>, BigFraction> gameAnalysis = gameAnalyzer.analyze(game, brg);

        StaticContext.getInstance().getGameAnalysisReporter().printReport(game, gameAnalysis, System.out);
    }
}
