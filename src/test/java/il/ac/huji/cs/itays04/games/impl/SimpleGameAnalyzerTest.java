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

    //todo: add asserts

    @Test
    public void analyzeProposition5Example() {
        final List<Integer> voterPositions = Lists.newArrayList(14,7,12);
        final Set<Integer> candidatePositions = Sets.newHashSet(17,2,10);

        analyzeAndReport(voterPositions, candidatePositions, "Proposition 5 example");
    }

    @Test
    public void analyzeTheorem8Example() {
        final List<Integer> voterPositions = Lists.newArrayList(16,4,1,7);
        final Set<Integer> candidatePositions = Sets.newHashSet(5,1,19,8);

        analyzeAndReport(voterPositions, candidatePositions, "Theorem 8 example");
    }

    @Test
    public void analyzeTheorem10Example() {
        final List<Integer> voterPositions = Lists.newArrayList(400, 47, 92, 92, 92, 0, 0, 0, 0);
        final Set<Integer> candidatePositions = Sets.newHashSet(0,92,400);

        analyzeAndReport(voterPositions, candidatePositions, "Theorem 10 example");
    }

    @Test
    public void analyzeTheorem11Example() {
        final List<Integer> voterPositions = Lists.newArrayList(15, 55, 42, 66, 74);
        final Set<Integer> candidatePositions = Sets.newHashSet(15, 55, 42, 66, 74);

        analyzeAndReport(voterPositions, candidatePositions, "Theorem 11 example");
    }

    @Test
    public void analyzeTheorem12Example() {
        final List<Integer> voterPositions = Lists.newArrayList(4, 32, 34, 48, 67);
        final Set<Integer> candidatePositions = Sets.newHashSet(14, 32, 42, 60, 93);

        analyzeAndReport(voterPositions, candidatePositions, "Theorem 12 example");
    }

    public void analyzeAndReport(List<Integer> voterPositions, Set<Integer> candidatePositions, String gameDescription) {
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
    }
}
