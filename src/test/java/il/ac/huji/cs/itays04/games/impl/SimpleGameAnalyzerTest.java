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
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;


public class SimpleGameAnalyzerTest {

    //todo: add asserts

    @Test
    public void analyzeTheorem5Example() {
        final List<Integer> voterPositions = Lists.newArrayList(14,7,12);
        final Set<Integer> candidatePositions = Sets.newHashSet(17,2,10);

        final GameAnalysis<VotingGameState<Integer>, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 5 example");

        assertConvergence(analysis, true);
    }

    public void assertConvergence(
            GameAnalysis<VotingGameState<Integer>, BigFraction> analysis,
            boolean shouldConverge) {

        final boolean doesConverge = analysis.getNumberOfNonSingularSccs() == 0;

        if (shouldConverge) {
            Assert.assertTrue("Game has cycles and thus may not converge", doesConverge);
        }
        else {
            Assert.assertFalse("Game has no cycles and thus is guaranteed to converge", doesConverge);
        }
    }

    @Test
    public void analyzeTheorem8Example() {
        final List<Integer> voterPositions = Lists.newArrayList(16,4,1,7);
        final Set<Integer> candidatePositions = Sets.newHashSet(5,1,19,8);

        final GameAnalysis<VotingGameState<Integer>, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 8 example");

        assertConvergence(analysis, false);
        assertNash(analysis, true);
    }

    @Test
    public void analyzeTheorem10Example() {
        final List<Integer> voterPositions = Lists.newArrayList(400, 47, 92, 92, 92, 0, 0, 0, 0);
        final Set<Integer> candidatePositions = Sets.newHashSet(0,92,400);

        final GameAnalysis<VotingGameState<Integer>, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 10 example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyzeTheorem11Example() {
        final List<Integer> voterPositions = Lists.newArrayList(15, 55, 42, 66, 74);
        final Set<Integer> candidatePositions = Sets.newHashSet(15, 55, 42, 66, 74);

        final GameAnalysis<VotingGameState<Integer>, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 11 example");

        assertConvergence(analysis, false);
        assertNash(analysis, true);
    }

    @Test
    public void analyzeTheorem12Example() {
        final List<Integer> voterPositions = Lists.newArrayList(4, 32, 34, 48, 67);
        final Set<Integer> candidatePositions = Sets.newHashSet(14, 32, 42, 60, 93);

        final GameAnalysis<VotingGameState<Integer>, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 12 example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    private void assertNash(GameAnalysis<VotingGameState<Integer>, BigFraction> analysis, boolean shouldExist) {
        final boolean exists = analysis.getNeCount() > 0;

        if (shouldExist) {
            Assert.assertTrue("Game has no NE", exists);
        }
        else {
            Assert.assertFalse("Game has a NE", exists);
        }
    }

    public GameAnalysis<VotingGameState<Integer>, BigFraction> analyzeAndReport(List<Integer> voterPositions, Set<Integer> candidatePositions, String gameDescription) {
        System.out.println("Analyzing " + gameDescription + " with voters: " + voterPositions);
        final QuadraticFactory quadraticFactory = StaticContext.getInstance().getQuadraticFactory();

        final BigFractionAverageSocialWelfareCalculator<VotingGameState<Integer>> welfareCalculator =
                new BigFractionAverageSocialWelfareCalculator<>();

        final VotingGame<Integer, BigFraction, BigFraction> game = quadraticFactory.createDistanceBasedGame(
                voterPositions, candidatePositions, welfareCalculator);

        final GameAnalyzer gameAnalyzer = StaticContext.getInstance().getGameAnalyzer();
        final ImmutableDirectedGraphWithScc<VotingGameState<Integer>> brg = gameAnalyzer.calculateBestResponseGraph(game);

        final GameAnalysis<VotingGameState<Integer>, BigFraction> gameAnalysis = gameAnalyzer.analyze(game, brg);

        StaticContext.getInstance().getGameAnalysisReporter().printReport(game, gameAnalysis, System.out);

        return gameAnalysis;
    }
}
