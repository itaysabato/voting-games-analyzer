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
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;


public class SimpleGameAnalyzerTest {
    private final Random random = new Random();

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

    @Test
    public void analyzeTheorem12Without93Example() {
        final List<Integer> voterPositions = Lists.newArrayList(4, 32, 34, 48, 67);
        final Set<Integer> candidatePositions = Sets.newHashSet(14, 32, 42, 60);

        final GameAnalysis<VotingGameState<Integer>, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 12 without candidate 93");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    @Ignore
    public void analyze4Voters5CandidatesFailedNoNeExample() {
        final List<Integer> voterPositions = Lists.newArrayList(447892247, 1807778634, -1386396308, -800174363);
        final Set<Integer> candidatePositions = Sets.newHashSet(-2006359767, -534226103, -1292843788, -821544891, -463640637);

        final GameAnalysis<VotingGameState<Integer>, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 5 candidates with no NE example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    @Ignore
    public void analyze4Voters4CandidatesFailedNoNeExample() {
        final List<Integer> voterPositions = Lists.newArrayList(447892247, 1807778634, -1386396308, -800174363);
        final Set<Integer> candidatePositions = Sets.newHashSet(-2006359767, -1292843788, -821544891, -463640637);

        final GameAnalysis<VotingGameState<Integer>, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 4 candidates with no NE example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyze4Voters4CandidatesNoNeExample() {
        final List<Integer> voterPositions = Lists.newArrayList(-1421648613, 933478673, -283196042, 2113642072);
        final Set<Integer> candidatePositions = Sets.newHashSet(-1411103600, -10658440, 653699683, 1613807734);

        final GameAnalysis<VotingGameState<Integer>, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 4 candidates with no NE example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    @Ignore
    public void analyzeInfiniteRandom4VotersExample() {
        //noinspection InfiniteLoopStatement
        while (true) {
            analyzeRandomExample(4, 3, 6);
        }
    }

    public void analyzeRandomExample(int numberOfVoters, int minNumberOfCandidates, int maxNumberOfCandidates) {
        final List<Integer> voterPositions = getRandomVoters(numberOfVoters);
        final Set<Integer> candidatePositions = getRandomCandidates(minNumberOfCandidates, maxNumberOfCandidates);

        final GameAnalysis<VotingGameState<Integer>, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Random " + numberOfVoters + " voters example");

        assertNash(analysis, true);
    }

    private Set<Integer> getRandomCandidates(int min, int max) {
        final OptionalInt numberOfCandidates = random.ints(min, max).findAny();

        return random.ints()
                .distinct()
                .limit(numberOfCandidates.getAsInt())
                .mapToObj(i -> i)
                .collect(Collectors.toSet());
    }

    private List<Integer> getRandomVoters(int numberOfVoters) {
        return random.ints(numberOfVoters)
                .mapToObj(i -> i)
                .collect(Collectors.toList());
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

    private GameAnalysis<VotingGameState<Integer>, BigFraction> analyzeAndReport(
            List<Integer> voterPositions,
            Set<Integer> candidatePositions,
            String gameDescription) {

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
