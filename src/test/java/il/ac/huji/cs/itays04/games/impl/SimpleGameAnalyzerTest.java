package il.ac.huji.cs.itays04.games.impl;

import com.google.common.collect.Lists;
import il.ac.huji.cs.itays04.games.api.BigFractionAverageSocialWelfareCalculator;
import il.ac.huji.cs.itays04.games.api.GameAnalysis;
import il.ac.huji.cs.itays04.games.api.GameAnalyzer;
import il.ac.huji.cs.itays04.games.api.GamePrices;
import il.ac.huji.cs.itays04.utils.ImmutableDirectedGraphWithScc;
import il.ac.huji.cs.itays04.utils.NumberUtils;
import il.ac.huji.cs.itays04.voting.VotingGame;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import il.ac.huji.cs.itays04.voting.quadratic.QuadraticFactory;
import org.apache.commons.math3.fraction.BigFraction;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;


public class SimpleGameAnalyzerTest {
    private final Random random = new Random();

    @Test
    public void analyzeTheorem5Example() {
        final List<Integer> voterPositions = voters(14,7,12);
        final Set<Integer> candidatePositions = candidates(17,2,10);

        final GameAnalysis<?, ?> analysis = analyzeAndReportInts(
                voterPositions, candidatePositions, "Theorem 5 example");

        assertConvergence(analysis, true);
    }

    public void assertConvergence(
            GameAnalysis<?, ?> analysis,
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
        final List<Integer> voterPositions = voters(16, 4, 1, 7);
        final Set<Integer> candidatePositions = candidates(5,1,19,8);

        final GameAnalysis<?, ?> analysis = analyzeAndReportInts(
                voterPositions, candidatePositions, "Theorem 8 example");

        assertConvergence(analysis, false);
        assertNash(analysis, true);
    }

    @Test
    public void analyzeTheorem8WorseExample() {
        final List<Integer> voterPositions = voters(500, 4, 1, 7);
        final Set<Integer> candidatePositions = candidates(5,1,500,8);

        final GameAnalysis<?, ?> analysis = analyzeAndReportInts(
                voterPositions, candidatePositions, "Theorem 8 worse example");

        assertConvergence(analysis, false);
        assertNash(analysis, true);
    }

    @Test
    public void analyzeTheorem10Example() {
        final List<Integer> voterPositions = voters(400, 47, 92, 92, 92, 0, 0, 0, 0);
        final Set<Integer> candidatePositions = candidates(0,92,400);

        final GameAnalysis<?, ?> analysis = analyzeAndReportInts(
                voterPositions, candidatePositions, "Theorem 10 example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyzeTheorem11Example() {
        final List<Integer> voterPositions = voters(15, 55, 42, 66, 74);
        final Set<Integer> candidatePositions = candidates(15, 55, 42, 66, 74);

        final GameAnalysis<?, ?> analysis = analyzeAndReportInts(
                voterPositions, candidatePositions, "Theorem 11 example");

        assertConvergence(analysis, false);
        assertNash(analysis, true);
    }

    @Test
    public void analyzeTheorem12Example() {
        final List<Integer> voterPositions = voters(4, 32, 34, 48, 67);
        final Set<Integer> candidatePositions = candidates(14, 32, 42, 60, 93);

        final GameAnalysis<?, ?> analysis = analyzeAndReportInts(
                voterPositions, candidatePositions, "Theorem 12 example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyzeTheorem12Without93Example() {
        final List<Integer> voterPositions = voters(4, 32, 34, 48, 67);
        final Set<Integer> candidatePositions = candidates(14, 32, 42, 60);

        final GameAnalysis<?, ?> analysis = analyzeAndReportInts(
                voterPositions, candidatePositions, "Theorem 12 without candidate 93");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    @Ignore
    public void analyze4Voters5CandidatesFailedNoNeExample() {
        final List<Integer> voterPositions = voters(447892247, 1807778634, -1386396308, -800174363);
        final Set<Integer> candidatePositions = candidates(-2006359767, -534226103, -1292843788, -821544891, -463640637);

        final GameAnalysis<?, ?> analysis = analyzeAndReportInts(
                voterPositions, candidatePositions, "4 voters and 5 candidates with no NE example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    @Ignore
    public void analyze4Voters4CandidatesFailedNoNeExample() {
        final List<Integer> voterPositions = voters(447892247, 1807778634, -1386396308, -800174363);
        final Set<Integer> candidatePositions = candidates(-2006359767, -1292843788, -821544891, -463640637);

        final GameAnalysis<?, ?> analysis = analyzeAndReportInts(
                voterPositions, candidatePositions, "4 voters and 4 candidates with no NE example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyze4Voters4CandidatesNoNeExample() {
        final List<Integer> voterPositions = voters(-1421648613, 933478673, -283196042, 2113642072);
        final Set<Integer> candidatePositions = candidates(-1411103600, -10658440, 653699683, 1613807734);

        final GameAnalysis<?, ?> analysis = analyzeAndReportInts(
                voterPositions, candidatePositions, "4 voters and 4 candidates with no NE example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyze4Voters4CandidatesNoNeSmoothExample() {
        final List<Integer> voterPositions = voters(0, 66, 32, 100);
        final Set<Integer> candidatePositions = candidates(0, 39, 58, 85);

        final GameAnalysis<?, ?> analysis = analyzeAndReportInts(
                voterPositions, candidatePositions, "4 voters and 4 candidates with no NE smooth example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyze4VotersPoA3Example() {
        final List<Integer> voterPositions = voters(-76449421, 1131956366, -52576629, 1177352783);
        final Set<Integer> candidatePositions = candidates(-1205708878, 2011356870, -1659967173, 1996515385, 1115606866);

        final GameAnalysis<?, BigFraction> analysis = analyzeAndReportInts(
                voterPositions, candidatePositions, "4 voters and 5 candidates PoA=3 example");

//        assertConvergence(analysis, false);
        assertNash(analysis, true);

        Assert.assertTrue(analysis.getPrices().getPriceOfAnarchy().get().compareTo(new BigFraction(3)) >= 0);
    }

    @Test
    public void analyze4VotersPoA3SmoothExample() {
        final List<BigFraction> voterPositions = voters(new BigFraction(-76449421, 1205708878), new BigFraction(1131956366, 1205708878), new BigFraction(-52576629, 1205708878), new BigFraction(1177352783, 1205708878));
        final Set<BigFraction> candidatePositions = candidates(new BigFraction(-1), new BigFraction(2011356870, 1205708878), new BigFraction(-1659967173, 1205708878), new BigFraction(1996515385, 1205708878), new BigFraction(1115606866, 1205708878));

        final GameAnalysis<?, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 5 candidates PoA=3 example");

//        assertConvergence(analysis, false);
        assertNash(analysis, true);

        Assert.assertTrue(analysis.getPrices().getPriceOfAnarchy().get().compareTo(new BigFraction(3)) >= 0);
    }

    @Test
    public void analyze4VotersPoA3SmootherExample() {
        final List<Integer> voterPositions = voters(660, 660, 1123, 1123);
//        final Set<Integer> candidatePositions = candidates(0, 1151, 1522);
        final Set<Integer> candidatePositions = candidates(-110, 1125 , 1738);

        final GameAnalysis<?, BigFraction> analysis = analyzeAndReportInts(
                voterPositions, candidatePositions, "4 voters and 5 candidates PoA=3 example");

//        assertConvergence(analysis, false);
        assertNash(analysis, true);

        Assert.assertTrue(analysis.getPrices().getPriceOfAnarchy().get().compareTo(new BigFraction(3)) >= 0);
    }

    @Test
    @Ignore
    public void analyzeInfiniteRandomVotersExample() {
        BigFraction worstPriceOfAnarchy = BigFraction.ZERO, worstPriceOfSinking = BigFraction.ZERO;

        //noinspection InfiniteLoopStatement
        while (true) {

            final GameAnalysis<VotingGameState<BigFraction>, BigFraction> analysis = analyzeRandomExample(4, 3, 6);
            final GamePrices<BigFraction> prices = analysis.getPrices();
            final Optional<BigFraction> priceOfAnarchy = prices.getPriceOfAnarchy();

            if (priceOfAnarchy.isPresent()
                    && worstPriceOfAnarchy.compareTo(priceOfAnarchy.get()) < 0) {

                worstPriceOfAnarchy = priceOfAnarchy.get();
                System.out.println("Worst POA yet: " + worstPriceOfAnarchy + " (" + worstPriceOfAnarchy.doubleValue() + ")");
            }

            final BigFraction priceOfSinking = prices.getPriceOfSinking();
            if (worstPriceOfSinking.compareTo(priceOfSinking) < 0) {

                worstPriceOfSinking = priceOfSinking;
                System.out.println("Worst POSink yet: " + worstPriceOfSinking + " (" + worstPriceOfSinking.doubleValue() + ")");
            }
        }
    }

    @SafeVarargs
    public final <T extends Comparable<T>> List<T> voters(T... positions) {
        final ArrayList<T> voters = Lists.newArrayList(positions);
        Collections.sort(voters);
        return voters;
    }

    @SafeVarargs
    public final <T extends Comparable<T>> Set<T> candidates(T... positions) {
        final ArrayList<T> candidates = Lists.newArrayList(positions);
        Collections.sort(candidates);
        return new LinkedHashSet<>(candidates);
    }

    public GameAnalysis<VotingGameState<BigFraction>, BigFraction> analyzeRandomExample(int numberOfVoters, int minNumberOfCandidates, int maxNumberOfCandidates) {
        final List<Integer> voterPositions = getRandomVoters(numberOfVoters);
        final Set<Integer> candidatePositions = getRandomCandidates(minNumberOfCandidates, maxNumberOfCandidates);

        return analyzeAndReportInts(
                voterPositions, candidatePositions, "Random " + numberOfVoters + " voters example");
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

    private void assertNash(GameAnalysis<?, ?> analysis, boolean shouldExist) {
        final boolean exists = analysis.getNeCount() > 0;

        if (shouldExist) {
            Assert.assertTrue("Game has no NE", exists);
        }
        else {
            Assert.assertFalse("Game has a NE", exists);
        }
    }

    private GameAnalysis<VotingGameState<BigFraction>, BigFraction> analyzeAndReportInts(
            List<Integer> voterPositions,
            Set<Integer> candidatePositions,
            String gameDescription) {

        final List<BigFraction> voterFractions = voterPositions.stream()
                .map(BigFraction::new)
                .collect(Collectors.toList());

        final Set<BigFraction> candidateFractions = candidatePositions.stream()
                .sequential()
                .map(BigFraction::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return analyzeAndReport(voterFractions, candidateFractions, gameDescription);
    }

    private GameAnalysis<VotingGameState<BigFraction>, BigFraction> analyzeAndReport(
            List<BigFraction> voterPositions,
            Set<BigFraction> candidatePositions,
            String gameDescription) {

        System.out.println("Analyzing " + gameDescription + " with voters: ");
        for (int i = 0; i < voterPositions.size(); i++) {
            System.out.println("V" + i + " = " + NumberUtils.fractionToString(voterPositions.get(i)));
        }

        System.out.println();

        final String candidatesString = candidatePositions.stream()
                .sequential()
                .map(NumberUtils::fractionToString)
                .collect(Collectors.joining("\n", "\n", "\n"));

        System.out.println("and candidates: " + candidatesString);

        final QuadraticFactory quadraticFactory = StaticContext.getInstance().getQuadraticFactory();

        final BigFractionAverageSocialWelfareCalculator<VotingGameState<BigFraction>> welfareCalculator =
                new BigFractionAverageSocialWelfareCalculator<>();

        final VotingGame<BigFraction, BigFraction, BigFraction> game = quadraticFactory.createDistanceBasedGame(
                voterPositions, candidatePositions, welfareCalculator);

        final GameAnalyzer gameAnalyzer = StaticContext.getInstance().getGameAnalyzer();
        final ImmutableDirectedGraphWithScc<VotingGameState<BigFraction>> brg = gameAnalyzer.calculateBestResponseGraph(game);

        final GameAnalysis<VotingGameState<BigFraction>, BigFraction> gameAnalysis = gameAnalyzer.analyze(game, brg);

        StaticContext.getInstance().getGameAnalysisReporter().printReport(game, gameAnalysis, System.out);

//        if (gameAnalysis.getPrices().getPriceOfSinking().compareTo(new BigFraction(3)) >= 0) {
//            System.out.println("Analyzing " + gameDescription + " with voters: " + voterPositions);
//            StaticContext.getInstance().getGameAnalysisReporter().printReport(game, gameAnalysis, System.out);
//        }

        return gameAnalysis;
    }
}
