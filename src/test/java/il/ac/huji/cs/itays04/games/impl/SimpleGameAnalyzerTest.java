package il.ac.huji.cs.itays04.games.impl;

import com.google.common.collect.Lists;
import il.ac.huji.cs.itays04.games.api.*;
import il.ac.huji.cs.itays04.utils.ImmutableDirectedGraphWithScc;
import il.ac.huji.cs.itays04.utils.NumberUtils;
import il.ac.huji.cs.itays04.voting.VotingGame;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import il.ac.huji.cs.itays04.voting.quadratic.QuadraticFactory;
import il.ac.huji.cs.itays04.voting.quadratic.WeightedUtilityCalculator;
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
        final List<BigFraction> voterPositions = voters(14,7,12);
        final Set<BigFraction> candidatePositions = candidates(17,2,10);

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
        final List<BigFraction> voterPositions = voters(16, 4, 1, 7);
        final Set<BigFraction> candidatePositions = candidates(5,1,19,8);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 8 example");

        assertConvergence(analysis, false);
        assertNash(analysis, true);
    }

    @Test
    public void analyzeTheorem8WorseExample() {
        final List<BigFraction> voterPositions = voters(500, 4, 1, 7);
        final Set<BigFraction> candidatePositions = candidates(5,1,500,8);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 8 worse example");

        assertConvergence(analysis, false);
        assertNash(analysis, true);
    }

    @Test
    public void analyzeTheorem10Example() {
        final List<BigFraction> voterPositions = voters(400, 47, 92, 92, 92, 0, 0, 0, 0);
        final Set<BigFraction> candidatePositions = candidates(0,92,400);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 10 example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyzeTheorem11Example() {
        final List<BigFraction> voterPositions = voters(15, 55, 42, 66, 74);
        final Set<BigFraction> candidatePositions = candidates(15, 55, 42, 66, 74);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 11 example");

        assertConvergence(analysis, false);
        assertNash(analysis, true);
    }

    @Test
    public void analyzeTheorem12Example() {
        final List<BigFraction> voterPositions = voters(4, 32, 34, 48, 67);
        final Set<BigFraction> candidatePositions = candidates(14, 32, 42, 60, 93);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 12 example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyzeTheorem12Without93Example() {
        final List<BigFraction> voterPositions = voters(4, 32, 34, 48, 67);
        final Set<BigFraction> candidatePositions = candidates(14, 32, 42, 60);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 12 without candidate 93");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    @Ignore
    public void analyze4Voters5CandidatesFailedNoNeExample() {
        final List<BigFraction> voterPositions = voters(447892247, 1807778634, -1386396308, -800174363);
        final Set<BigFraction> candidatePositions = candidates(-2006359767, -534226103, -1292843788, -821544891, -463640637);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 5 candidates with no NE example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    @Ignore
    public void analyze4Voters4CandidatesFailedNoNeExample() {
        final List<BigFraction> voterPositions = voters(447892247, 1807778634, -1386396308, -800174363);
        final Set<BigFraction> candidatePositions = candidates(-2006359767, -1292843788, -821544891, -463640637);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 4 candidates with no NE example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyze4Voters4CandidatesNoNeExample() {
        final List<BigFraction> voterPositions = voters(-1421648613, 933478673, -283196042, 2113642072);
        final Set<BigFraction> candidatePositions = candidates(-1411103600, -10658440, 653699683, 1613807734);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 4 candidates with no NE example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyze4Voters4CandidatesNoNeSmoothExample() {
        final List<BigFraction> voterPositions = voters(0, 11, 22, 33);
        final Set<BigFraction> candidatePositions = candidates(0, 11, 19, 30);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 4 candidates with no NE smooth example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyze5VotersEqualCandidatesUniformExample() {
        final List<BigFraction> voterPositions = voters(1, 2, 3, 4, 5);
        final Set<BigFraction> candidatePositions = candidates(1, 2, 3, 4, 5);

        analyzeAndReport(voterPositions, candidatePositions, "5 voters=candidates uniform example");
    }

    @Test
    public void analyze4VotersEqualCandidatesUniformExample() {
        final List<BigFraction> voterPositions = voters(1, 2, 3, 4);
        final Set<BigFraction> candidatePositions = candidates(1, 2, 3, 4);

        analyzeAndReport(voterPositions, candidatePositions, "5 voters=candidates uniform example");

    }

    @Test
    public void analyze4VotersPoA3Example() {
        final List<BigFraction> voterPositions = voters(-76449421, 1131956366, -52576629, 1177352783);
        final Set<BigFraction> candidatePositions = candidates(-1205708878, 2011356870, -1659967173, 1996515385, 1115606866);

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
        final List<BigFraction> voterPositions = voters(new BigFraction(-76449421, 1205708878), new BigFraction(1131956366, 1205708878), new BigFraction(-52576629, 1205708878), new BigFraction(1177352783, 1205708878));
        final Set<BigFraction> candidatePositions = candidates(new BigFraction(-1), new BigFraction(2011356870, 1205708878), new BigFraction(-1659967173, 1205708878), new BigFraction(1996515385, 1205708878), new BigFraction(1115606866, 1205708878));

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
        final List<BigFraction> voterPositions = voters(7, 7, 9, 9);
        final Set<BigFraction> candidatePositions = candidates(4, 8 , 12);

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
        final List<BigFraction> voterPositions = voters(19, 19, 19, 19, 19, 21, 21, 21, 21, 21);
        final Set<BigFraction> candidatePositions = candidates(10, 20 , 30);

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

        final List<BigFraction> voterPositions = voters(voters);
        final Set<BigFraction> candidatePositions = candidates(a, b, delta, d);

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

        final List<BigFraction> voterPositions = voters(voters);
        final Set<BigFraction> candidatePositions = candidates(n, 2 * n , 3 * n);

        final GameAnalysis<VotingGameState<BigFraction>, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, n + " voters and 3 candidates PoA=" + n + " example");

        assertNash(analysis, true);
        final Optional<BigFraction> priceOfAnarchy = analysis.getPrices().getPriceOfAnarchy();
        assert priceOfAnarchy.isPresent();
        final BigFraction poa = priceOfAnarchy.get();

        Assert.assertTrue(poa.compareTo(new BigFraction(n)) == 0);


        final ArrayList<BigFraction> votes = new ArrayList<>(n);

        for (int i = 0; i < k; i++) {
            votes.add(i, new BigFraction(2*n));
        }

        for (int i = k; i < n; i++) {
            votes.add(i, new BigFraction(3*n));
        }
        final VotingGameState<BigFraction> state = new VotingGameState<>(votes);
        final ImmutableDirectedGraphWithScc<VotingGameState<BigFraction>> bestResponseGraph = analysis.getBestResponseGraph();

        final Set<VotingGameState<BigFraction>> improvingStates = bestResponseGraph.getOriginalGraph().getEdges().get(state);

        System.out.println("The following are improving moves from state: " + state);
        improvingStates.forEach(System.out::println);

    }

    private void test2kPlus1PoA(int k) {
        final int n = 2 * k + 1;
        final Integer[] voters = new Integer[n];

        Arrays.fill(voters, 0, k, (4 * k) - 1);
        voters[k] = 4 * k;
        Arrays.fill(voters, k + 1, n, (4 * k) + 1);

        final List<BigFraction> voterPositions = voters(voters);
        final Set<BigFraction> candidatePositions = candidates(2 * k, 4 * k , 6 * k);

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

        final List<BigFraction> voterPositions = voters(voters);
        final Set<BigFraction> candidatePositions = candidates(n, 2 * n , 3 * n);

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

            final GameAnalysis<VotingGameState<BigFraction>, BigFraction> analysis = analyzeRandomExample(5, 3, 6);
            final GamePrices<BigFraction> prices = analysis.getPrices();
            final Optional<BigFraction> priceOfAnarchy = prices.getPriceOfAnarchy();

            if (priceOfAnarchy.isPresent()
                    && worstPriceOfAnarchy.compareTo(priceOfAnarchy.get()) < 0) {

                worstPriceOfAnarchy = priceOfAnarchy.get();
                System.out.println("Worst POA yet: " + worstPriceOfAnarchy + " (" + worstPriceOfAnarchy.doubleValue() + ")");
            }

            final Optional<BigFraction> priceOfStability = prices.getPriceOfStability();
            if (priceOfStability.isPresent()
                    && worstPriceOfStability.compareTo(priceOfStability.get()) < 0) {

                worstPriceOfStability = priceOfStability.get();
                System.out.println("Worst PoS yet: " + worstPriceOfStability + " (" + worstPriceOfStability.doubleValue() + ")");
            }

            final BigFraction priceOfSinking = prices.getPriceOfSinking();
            if (worstPriceOfSinking.compareTo(priceOfSinking) < 0) {

                worstPriceOfSinking = priceOfSinking;
                System.out.println("Worst PoSink yet: " + worstPriceOfSinking + " (" + worstPriceOfSinking.doubleValue() + ")");
            }
        }
    }

    private List<BigFraction> voters(Integer... positions) {
        return voters(Arrays.asList(positions));
    }

    private List<BigFraction> voters(List<Integer> voters) {
        return voters.stream()
                .sorted()
                .map(BigFraction::new)
                .collect(Collectors.toList());
    }

    private Set<BigFraction> candidates(Integer... positions) {
        return candidates(Arrays.asList(positions));
    }

    private Set<BigFraction> candidates(Collection<Integer> candidates) {
        return candidates.stream()
                .sorted()
                .map(BigFraction::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private List<BigFraction> voters(BigFraction... positions) {
        final ArrayList<BigFraction> voters = Lists.newArrayList(positions);
        Collections.sort(voters);
        return voters;
    }

    private Set<BigFraction> candidates(BigFraction... positions) {
        final ArrayList<BigFraction> candidates = Lists.newArrayList(positions);
        return candidates.stream()
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private GameAnalysis<VotingGameState<BigFraction>, BigFraction> analyzeRandomExample(int numberOfVoters, int minNumberOfCandidates, int maxNumberOfCandidates) {
        final List<BigFraction> voterPositions = getRandomVoters(numberOfVoters);
        final Set<BigFraction> candidatePositions = getRandomCandidates(minNumberOfCandidates, maxNumberOfCandidates);

        return analyzeAndReport(
                voterPositions,
                candidatePositions,
                "Random " + numberOfVoters + " voters example",
                true);
    }

    private Set<BigFraction> getRandomCandidates(int min, int max) {
        final OptionalInt numberOfCandidates = random.ints(min, max).findAny();
        assert numberOfCandidates.isPresent();

        final Set<Integer> candidates = random.ints()
                .distinct()
                .limit(numberOfCandidates.getAsInt())
                .mapToObj(i -> i)
                .collect(Collectors.toSet());

        return candidates(candidates);
    }

    private List<BigFraction> getRandomVoters(int numberOfVoters) {
        final List<Integer> voters = random.ints(numberOfVoters)
                .mapToObj(i -> i)
                .collect(Collectors.toList());

        return voters(voters);
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

    private GameAnalysis<VotingGameState<BigFraction>, BigFraction> analyzeAndReport(
            List<BigFraction> voterPositions,
            Set<BigFraction> candidatePositions,
            String gameDescription) {

        return analyzeAndReport(voterPositions, candidatePositions, gameDescription, false);
    }

    private GameAnalysis<VotingGameState<BigFraction>, BigFraction> analyzeAndReport(
            List<BigFraction> voterPositions,
            Set<BigFraction> candidatePositions,
            String gameDescription,
            boolean quiet) {

        if (!quiet) {
            System.out.println("************************************************************************************************");
            System.out.println("Analyzing " + gameDescription + " with voters: ");
            System.out.println("************************************************************************************************");

            for (int i = 0; i < voterPositions.size(); i++) {
                System.out.println("V" + (i+1) + " = " + NumberUtils.fractionToString(voterPositions.get(i)));
            }

            System.out.println();

            final String candidatesString = candidatePositions.stream()
                    .sequential()
                    .map(NumberUtils::fractionToString)
                    .collect(Collectors.joining("\n", "\n", "\n"));

            System.out.println("and candidates: " + candidatesString);
        }

        final QuadraticFactory quadraticFactory = StaticContext.getInstance().getQuadraticFactory();

        final BigFractionAverageSocialWelfareCalculator<VotingGameState<BigFraction>> welfareCalculator =
                new BigFractionAverageSocialWelfareCalculator<>();

        final VotingGame<BigFraction, BigFraction, BigFraction> game = quadraticFactory.createDistanceBasedGame(
                voterPositions, candidatePositions, welfareCalculator);

        final GameAnalyzer gameAnalyzer = StaticContext.getInstance().getGameAnalyzer();
        final ImmutableDirectedGraphWithScc<VotingGameState<BigFraction>> brg = gameAnalyzer.calculateBestResponseGraph(game);

        final GameAnalysis<VotingGameState<BigFraction>, BigFraction> gameAnalysis = gameAnalyzer.analyze(game, brg);

        if (!quiet) {
            final WeightedUtilityCalculator<BigFraction> randomDicCalc = quadraticFactory.createWeightedCalculator(
                    voterPositions, candidatePositions, false);

            System.out.println("Truthful profiles:");
            System.out.println();

            game.getTruthfulStates()
                    .entrySet()
                    .stream()
                    .forEachOrdered(entry -> {
                        System.out.println(entry.getKey());
                        System.out.println("SW = " + NumberUtils.fractionToString(entry.getValue()));

                        final SocialWelfareCalculator<VotingGameState<BigFraction>, BigFraction, BigFraction> socialWelfareCalculator = game.getSocialWelfareCalculator();
                        final BigFraction randomDicSW = socialWelfareCalculator.calculateWelfare(
                                game, randomDicCalc, entry.getKey());

                        System.out.println("Randomized dictatorship SW = " + NumberUtils.fractionToString(randomDicSW));

                        final BigFraction socialOptimum = gameAnalysis.getPrices().getSocialOptimum();
                        final BigFraction ratio = socialWelfareCalculator.getRatio(socialOptimum, randomDicSW);
                        System.out.println("Randomized dictatorship ratio to optimum = " + NumberUtils.fractionToString(ratio));

                        final BigFraction priceOfStability = gameAnalysis.getPrices().getPriceOfStability().orElse(BigFraction.ZERO);
                        final BigFraction priceOfStabilityRatio = socialWelfareCalculator.getRatio(priceOfStability, ratio);
                        System.out.println("Randomized dictatorship ratio to price of stability = " + NumberUtils.fractionToString(priceOfStabilityRatio));
                        System.out.println();
                    });

            StaticContext.getInstance().getGameAnalysisReporter().printReport(game, gameAnalysis, System.out);
        }

        return gameAnalysis;
    }
}
