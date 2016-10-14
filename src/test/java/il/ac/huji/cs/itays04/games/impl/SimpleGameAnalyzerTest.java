package il.ac.huji.cs.itays04.games.impl;

import il.ac.huji.cs.itays04.cli.StaticContext;
import il.ac.huji.cs.itays04.cli.VotingGameAnalysisRunner;
import il.ac.huji.cs.itays04.games.api.GameAnalysis;
import il.ac.huji.cs.itays04.games.api.GamePrices;
import il.ac.huji.cs.itays04.rational.NamedRationalEntity;
import il.ac.huji.cs.itays04.rational.NumberUtils;
import il.ac.huji.cs.itays04.rational.RandomUtils;
import il.ac.huji.cs.itays04.rational.RationalUtils;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import org.apache.commons.math3.fraction.BigFraction;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;


public class SimpleGameAnalyzerTest {
//    private static final int N_THREADS = 8;
//    private final ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
    private final RandomUtils randomUtils = StaticContext.getInstance().randomUtils;
    private final RationalUtils rationalUtils = StaticContext.getInstance().rationalUtils;
    private final VotingGameAnalysisRunner votingGameAnalysisRunner = StaticContext.getInstance().votingGameAnalysisRunner;

//    @Test
//    @Ignore
//    public void testBadPosQuicklyFor8() {
//        // 0 1 2 10 2 10 2 1
//        analyzePoS(0, 1, 3, 13, 15, 25, 27, 28);
//    }
//
//    @Test
//    @Ignore
//    public void testBadPosQuicklyFor7() {
//        analyzePoS(1, 3, 13, 14, 15, 25, 27);
//    }
//
//    @Test
//    public void testBadPosQuicklyFor6() {
//        analyzePoS(1, 3, 13, 15, 25, 27);
//    }
//
//    public void analyzePoS(Integer... voters) {
//        final List<BigFraction> voterPositions = rationalUtils.voters(voters);
//        analyzePoS(voterPositions);
//    }
//
//    public void analyzePoS(List<BigFraction> voterPositions) {
//        final Set<BigFraction> candidatePositions = candidates(voterPositions);
//
//        final VotingGame<BigFraction, BigFraction, BigFraction> game;
//        synchronized (this) {
//            game = getGame(voterPositions, candidatePositions, "Bad PoS Example", false);
//        }
//
//        final WeightedVotesRandomizedVotingRule<BigFraction> randomDicCalc = getRandomDicCalculator(
//                voterPositions, candidatePositions);
//
//        final BigFraction bestRandomDicSw;
//        synchronized (this) {
//            log("Truthful states with Random-Dic SW:");
//            log("------------------------------------------------------------------");
//            final Optional<BigFraction> max = game.getTruthfulStates()
//                    .keySet()
//                    .stream()
//                    .peek(this::log)
//                    .map(state -> welfareCalculator.calculateWelfare(game, randomDicCalc, state))
//                    .peek(this::withSw)
//                    .peek(x -> log())
//                    .max(Comparator.naturalOrder());
//
//            assert max.isPresent();
//            bestRandomDicSw = max.get();
//            log("Best Random-Dic SW: " + NumberUtils.fractionToString(bestRandomDicSw));
//        }
//
//        final SimpleGameTraverser simpleGameTraverser = StaticContext.getInstance().getSimpleGameTraverser();
//        final HashSet<VotingGameState<BigFraction>> visited = new HashSet<>();
//        simpleGameTraverser.traverseGameUntil(game, visited, x -> false);
//
//        synchronized (this) {
//            log();
//            log("NE states with Quadratic SW:");
//            log("------------------------------------------------------------------");
//            visited.stream()
//                    .filter(s -> !game.getUtilityCalculator()
//                            .streamImprovements(game, s)
//                            .findAny()
//                            .isPresent())
//                    .peek(this::log)
//                    .map(state -> welfareCalculator.calculateWelfare(game, state))
//                    .peek(this::withSw)
//                    .peek(x -> log())
//                    .max(Comparator.naturalOrder())
//                    .ifPresent(sw -> {
//                        log("PoS = " + NumberUtils.fractionToString(sw));
//                        Assert.assertFalse("PoS worse than random dic!", sw.compareTo(bestRandomDicSw) < 0);
//                    });
//        }
//    }
//
//    public void withSw(BigFraction sw) {
//        log("With SW = " + NumberUtils.fractionToString(sw));
//    }

    @Test
    public void analyzePoSWorseThanDicSmoothExample() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(4, 4, 10);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(0, 10);

        analyzeAndReport(voterPositions, candidatePositions, "Worse than DIC SMOOTH example");
    }

    @Test
    public void analyzeTheorem5Example() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(14,7,12);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(17,2,10);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 5 example");

        assertConvergence(analysis, true);
    }

    private void assertConvergence(
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
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(16, 4, 1, 7);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(5,1,19,8);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 8 example");

        assertConvergence(analysis, false);
        assertNash(analysis, true);
    }

    @Test
    public void analyzeTheorem8WorseExample() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(500, 4, 1, 7);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(5,1,500,8);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 8 worse example");

        assertConvergence(analysis, false);
        assertNash(analysis, true);
    }

    @Test
    public void analyzeTheorem10Example() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(400, 47, 92, 92, 92, 0, 0, 0, 0);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(0,92,400);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 10 example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyzeTheorem11Example() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(15, 55, 42, 66, 74);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(15, 55, 42, 66, 74);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 11 example");

        assertConvergence(analysis, false);
        assertNash(analysis, true);
    }

    @Test
    public void analyzeTheorem12Example() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(4, 32, 34, 48, 67);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(14, 32, 42, 60, 93);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 12 example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyzeTheorem12Without93Example() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(4, 32, 34, 48, 67);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(14, 32, 42, 60);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 12 without candidate 93");

        assertNash(analysis, false);
    }

//    @Test
//    public void analyzeVotersEqualCandidatesNoNeAttempt() {
////        final List<BigFraction> voterPositions = rationalUtils.voters(4, 32, 34, 48, 67);
////        final Set<BigFraction> candidatePositions = rationalUtils.candidates(14, 32, 42, 60);
//        analyzePoS(4, 14, 32, 42, 60, 67);
//
//    }

    @Test
    @Ignore
    public void analyze4Voters5CandidatesFailedNoNeExample() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(447892247, 1807778634, -1386396308, -800174363);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(-2006359767, -534226103, -1292843788, -821544891, -463640637);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 5 candidates with no NE example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    @Ignore
    public void analyze4Voters4CandidatesFailedNoNeExample() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(447892247, 1807778634, -1386396308, -800174363);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(-2006359767, -1292843788, -821544891, -463640637);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 4 candidates with no NE example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyze4Voters4CandidatesNoNeExample() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(-1421648613, 933478673, -283196042, 2113642072);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(-1411103600, -10658440, 653699683, 1613807734);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 4 candidates with no NE example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyze4VotersPosWorseThanDicAttempt() {
        analyzeVotersEqualCandidates("4 voters=candidates Pos Worse Than Dic Attempt example", 1, 2, 4, 7);
    }

    private GameAnalysis<?, ?> analyzeVotersEqualCandidates(String gameDescription, Integer... integers) {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(integers);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(integers);

        return analyzeAndReport(voterPositions, candidatePositions, gameDescription);
    }

    @Test
    public void analyze4Voters4CandidatesNoNeSmoothExample() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(0, 11, 22, 33);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(0, 11, 19, 30);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 4 candidates with no NE smooth example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyze5VotersEqualCandidatesUniformExample() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(1, 2, 3, 4, 5);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(1, 2, 3, 4, 5);

        analyzeAndReport(voterPositions, candidatePositions, "5 voters=candidates uniform example");
    }

    @Test
    public void analyze4VotersEqualCandidatesUniformExample() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(1, 2, 3, 4);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(1, 2, 3, 4);

        analyzeAndReport(voterPositions, candidatePositions, "5 voters=candidates uniform example");

    }

    @Test
    public void analyze4VotersPoA3Example() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(-76449421, 1131956366, -52576629, 1177352783);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(-1205708878, 2011356870, -1659967173, 1996515385, 1115606866);

        final GameAnalysis<?, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 5 candidates PoA=3 example");

//        assertConvergence(analysis, false);
        assertNash(analysis, true);

        final Optional<BigFraction> priceOfAnarchy = analysis.getPrices().getPriceOfAnarchy();
        assert priceOfAnarchy.isPresent();
        Assert.assertTrue(priceOfAnarchy.get().compareTo(new BigFraction(3)) >= 0);
    }

    @Test
    public void analyze4VotersPoA3SmoothExample() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(new BigFraction(-76449421, 1205708878), new BigFraction(1131956366, 1205708878), new BigFraction(-52576629, 1205708878), new BigFraction(1177352783, 1205708878));
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(new BigFraction(-1), new BigFraction(2011356870, 1205708878), new BigFraction(-1659967173, 1205708878), new BigFraction(1996515385, 1205708878), new BigFraction(1115606866, 1205708878));

        final GameAnalysis<?, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 5 candidates PoA=3 example");

//        assertConvergence(analysis, false);
        assertNash(analysis, true);

        final Optional<BigFraction> priceOfAnarchy = analysis.getPrices().getPriceOfAnarchy();
        assert priceOfAnarchy.isPresent();
        Assert.assertTrue(priceOfAnarchy.get().compareTo(new BigFraction(3)) >= 0);
    }

    @Test
    public void analyze4VotersPoA4Example() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(7, 7, 9, 9);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(4, 8 , 12);

        final GameAnalysis<?, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 3 candidates PoA=4 example");

        assertNash(analysis, true);

        final Optional<BigFraction> priceOfAnarchy = analysis.getPrices().getPriceOfAnarchy();
        assert priceOfAnarchy.isPresent();
        Assert.assertTrue(priceOfAnarchy.get().compareTo(new BigFraction(3)) >= 0);
    }

    @Test
    @Ignore
    public void analyze10VotersPoA10Example() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(19, 19, 19, 19, 19, 21, 21, 21, 21, 21);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(10, 20 , 30);

        final GameAnalysis<?, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "10 voters and 3 candidates PoA=10 example");

        assertNash(analysis, true);

        final Optional<BigFraction> priceOfAnarchy = analysis.getPrices().getPriceOfAnarchy();
        assert priceOfAnarchy.isPresent();
        Assert.assertTrue(priceOfAnarchy.get().compareTo(new BigFraction(10)) == 0);
    }

    @Test
    public void analyze6VotersPoA6Example() {
        test2kPoA(3);
    }

    @Test
    @Ignore
    public void analyze7VotersPoAOver6Example() {
        test2kPlus1PoA(3);
    }

    @Test
    public void analyze8VotersPoA8Example() {
        test2kPoA(4);
    }

    @Test
    public void analyze2VotersPoA2Example() {
        test2kPoA(1);
    }

    @Test
    public void analyze4VotersPoS1Example() {
        test2kPoS1(2);
    }

    @Test
    public void analyze6VotersPoS1Example() {
        test2kPoS1(3);
    }

    @Test
    public void analyze6VotersPoSNot1Example() {
        test2kPoSNot1(3);
    }

    private void test2kPoS1(int k) {
        final int n = 2 * k;
        GameAnalysis<?, BigFraction> analysis = test2kDelta(k, new BigFraction(n, n - 1), true);

        final Optional<BigFraction> priceOfAnarchy = analysis.getPrices().getPriceOfAnarchy();
        assert priceOfAnarchy.isPresent();
        final BigFraction poa = priceOfAnarchy.get();
        Assert.assertTrue(poa.compareTo(new BigFraction(2, 5*n)) >= 0);
    }

    private void test2kPoSNot1(int k) {
        BigFraction delta = new BigFraction(2 * k, 2 * k - 1)
                .subtract(new BigFraction(1, 100));
        test2kDelta(k, delta, false);
    }

    private GameAnalysis<?, BigFraction> test2kDelta(int k, BigFraction delta, boolean pos1) {
        assert k > 1;
        final int n = 2 * k;
        final BigFraction a = new BigFraction(-n), b = delta.negate(), d = new BigFraction(n);

        final BigFraction[] voters = new BigFraction[n];
        voters[0] = a;
        Arrays.fill(voters, 1, k, b);
        Arrays.fill(voters, k, n-1, delta);
        voters[n-1] = d;

        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(voters);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(a, b, delta, d);

        final GameAnalysis<?, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, n + " voters and 4 candidates PoS=1 example");

        assertNash(analysis, true);
        final Optional<BigFraction> priceOfStability = analysis.getPrices().getPriceOfStability();
        assert priceOfStability.isPresent();
        final BigFraction pos = priceOfStability.get();
        Assert.assertTrue(pos1 == (pos.compareTo(new BigFraction(1)) == 0));

        return analysis;
    }

    private void test2kPoA(int k) {
        final int n = 2 * k;
        final Integer[] voters = new Integer[n];
        Arrays.fill(voters, 0, k, (4 * k) - 1);
        Arrays.fill(voters, k, n, (4 * k) + 1);

        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(voters);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(n, 2 * n , 3 * n);

        final GameAnalysis<?, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, n + " voters and 3 candidates PoA=" + n + " example");

        assertNash(analysis, true);
        final Optional<BigFraction> priceOfAnarchy = analysis.getPrices().getPriceOfAnarchy();
        assert priceOfAnarchy.isPresent();
        final BigFraction poa = priceOfAnarchy.get();

        Assert.assertTrue(poa.compareTo(new BigFraction(n)) == 0);
//
//        final ArrayList<BigFraction> votes = new ArrayList<>(n);
//
//        for (int i = 0; i < k; i++) {
//            votes.add(i, new BigFraction(2*n));
//        }
//
//        for (int i = k; i < n; i++) {
//            votes.add(i, new BigFraction(3*n));
//        }
//        final VotingGameState<BigFraction> state = new VotingGameState<>(votes);
//        final ImmutableDirectedGraphWithScc<?> bestResponseGraph = analysis.getBestResponseGraph();
//
//        final Set<VotingGameState<BigFraction>> improvingStates = bestResponseGraph.getOriginalGraph().getEdges().get(state);
//
//        log("The following are improving moves from state: " + state);
//        improvingStates.forEach(this::log);
    }

    private void test2kPlus1PoA(int k) {
        final int n = 2 * k + 1;
        final Integer[] voters = new Integer[n];

        Arrays.fill(voters, 0, k, (4 * k) - 1);
        voters[k] = 4 * k;
        Arrays.fill(voters, k + 1, n, (4 * k) + 1);

        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(voters);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(2 * k, 4 * k , 6 * k);

        final GameAnalysis<?, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, n + " voters and 3 candidates PoA>=" + (n-1) + " example");

        assertNash(analysis, true);
        final Optional<BigFraction> priceOfAnarchy = analysis.getPrices().getPriceOfAnarchy();
        assert priceOfAnarchy.isPresent();
        final BigFraction poa = priceOfAnarchy.get();
        Assert.assertTrue("PoA too small: " + NumberUtils.fractionToString(poa), poa.compareTo(new BigFraction(n-1)) >= 0);
    }

    @Test
    public void testPoS() {
        test2kPoS(3);
    }

    private void test2kPoS(int k) {
        final int n = 2 * k;
        final Integer[] voters = new Integer[n + 1];

        Arrays.fill(voters, 0, k, (4 * k) - 1);
        Arrays.fill(voters, k, n, (4 * k) + 1);
        voters[n] = 5 * k + 1;

        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(voters);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(n, 2 * n , 3 * n);

        analyzeAndReport(voterPositions, candidatePositions, (n + 1) + " voters and 3 candidates PoS=? example");

//        assertNash(analysis, true);
//        final Optional<BigFraction> priceOfAnarchy = analysis.getPrices().getPriceOfAnarchy();
//        assert priceOfAnarchy.isPresent();
//        final BigFraction poa = priceOfAnarchy.get();
//
//        Assert.assertTrue(poa.compareTo(new BigFraction(n)) == 0);
    }

    @Test
    @Ignore
    public void analyzeInfiniteRandomVotersExample() {
        BigFraction worstPriceOfAnarchy = BigFraction.ZERO,
                worstPriceOfStability = BigFraction.ZERO,
                worstPriceOfSinking = BigFraction.ZERO;

        //noinspection InfiniteLoopStatement
        while (true) {

            final GameAnalysis<?, BigFraction> analysis = analyzeRandomExample(5, 3, 6);
            final GamePrices<BigFraction> prices = analysis.getPrices();
            final Optional<BigFraction> priceOfAnarchy = prices.getPriceOfAnarchy();

            if (priceOfAnarchy.isPresent()
                    && worstPriceOfAnarchy.compareTo(priceOfAnarchy.get()) < 0) {

                worstPriceOfAnarchy = priceOfAnarchy.get();
                log("Worst POA yet: " + worstPriceOfAnarchy + " (" + worstPriceOfAnarchy.doubleValue() + ")");
            }

            final Optional<BigFraction> priceOfStability = prices.getPriceOfStability();
            if (priceOfStability.isPresent()
                    && worstPriceOfStability.compareTo(priceOfStability.get()) < 0) {

                worstPriceOfStability = priceOfStability.get();
                log("Worst PoS yet: " + worstPriceOfStability + " (" + worstPriceOfStability.doubleValue() + ")");
            }

            final BigFraction priceOfSinking = prices.getPriceOfSinking();
            if (worstPriceOfSinking.compareTo(priceOfSinking) < 0) {

                worstPriceOfSinking = priceOfSinking;
                log("Worst PoSink yet: " + worstPriceOfSinking + " (" + worstPriceOfSinking.doubleValue() + ")");
            }
        }
    }

//    @Test
//    @Ignore
//    public void analyzeInfiniteRandomVotersEqualCandidatesExample() throws InterruptedException {
//
//        final Runnable runnable = () -> {
//            try {
//                analyzeRandomPosExample(7);
//            }
//            catch (Exception e) {
//                log("Exception caught: " + e);
//                System.exit(0);
//            }
//        };
//
//        //noinspection InfiniteLoopStatement
//        while (true) {
//            for (int i = 0; i < N_THREADS; i++) {
//                executorService.execute(runnable);
//            }
//
//            Thread.sleep(120000);
//        }
//
//    }

    private GameAnalysis<VotingGameState<NamedRationalEntity>, BigFraction> analyzeRandomExample(int numberOfVoters, int minNumberOfCandidates, int maxNumberOfCandidates) {
        final LinkedHashSet<NamedRationalEntity> voterPositions = randomUtils.getRandomVoters(numberOfVoters);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = randomUtils.getRandomCandidates(minNumberOfCandidates, maxNumberOfCandidates);

        return analyzeAndReport(
                voterPositions,
                candidatePositions,
                "Random " + numberOfVoters + " voters example",
                true);
    }

//    private void analyzeRandomPosExample(int numberOfVoters) {
//        final List<BigFraction> voterPositions = randomUtils.getRandomPositions(numberOfVoters);
//        analyzePoS(voterPositions);
//    }

//    private GameAnalysis<VotingGameState<BigFraction>, BigFraction> analyzeRandomExample(int numberOfVoters, boolean quiet) {
//        final List<NamedRationalEntity> voterPositions = randomUtils.getRandomPositions(numberOfVoters);
//        final Set<BigFraction> candidatePositions = rationalUtils.candidates(voterPositions);
//
//        return analyzeAndReport(
//                voterPositions,
//                candidatePositions,
//                "Random " + numberOfVoters + " voters=candidates example",
//                quiet);
//    }

    private void assertNash(GameAnalysis<?, ?> analysis, boolean shouldExist) {
        final boolean exists = analysis.getNeCount() > 0;

        if (shouldExist) {
            Assert.assertTrue("Game has no NE", exists);
        }
        else {
            Assert.assertFalse("Game has a NE", exists);
        }
    }

    private GameAnalysis<VotingGameState<NamedRationalEntity>, BigFraction> analyzeAndReport(
            LinkedHashSet<NamedRationalEntity> voterPositions,
            LinkedHashSet<NamedRationalEntity> candidatePositions,
            String gameDescription) {

        return analyzeAndReport(voterPositions, candidatePositions, gameDescription, false);
    }

    private GameAnalysis<VotingGameState<NamedRationalEntity>, BigFraction> analyzeAndReport(
            LinkedHashSet<NamedRationalEntity> voterPositions,
            LinkedHashSet<NamedRationalEntity> candidatePositions,
            String gameDescription,
            boolean quiet) {

        return votingGameAnalysisRunner.analyzeAndReport(voterPositions, candidatePositions, gameDescription, quiet)
                .getGameAnalysis();
    }

//    private void log() {
//        log("");
//    }

    private void log(Object message) {
        final long id = Thread.currentThread().getId();

        final String prefix = id == 1 ?
                "" : id + " - ";

        System.out.println(prefix + message);
    }
}
