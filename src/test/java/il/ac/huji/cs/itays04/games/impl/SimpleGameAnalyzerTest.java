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
        final List<BigFraction> voterPositions = voters(14,7,12);
        final Set<BigFraction> candidatePositions = candidates(17,2,10);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
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
        final List<BigFraction> voterPositions = voters(0, 66, 32, 100);
        final Set<BigFraction> candidatePositions = candidates(0, 39, 58, 85);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 4 candidates with no NE smooth example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void analyze4VotersPoA3Example() {
        final List<BigFraction> voterPositions = voters(-76449421, 1131956366, -52576629, 1177352783);
        final Set<BigFraction> candidatePositions = candidates(-1205708878, 2011356870, -1659967173, 1996515385, 1115606866);

        final GameAnalysis<?, BigFraction> analysis = analyzeAndReport(
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
    public void analyze4VotersPoA4Example() {
        final List<BigFraction> voterPositions = voters(7, 7, 9, 9);
        final Set<BigFraction> candidatePositions = candidates(4, 8 , 12);

        final GameAnalysis<?, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "4 voters and 3 candidates PoA=4 example");

        assertNash(analysis, true);

        Assert.assertTrue(analysis.getPrices().getPriceOfAnarchy().get().compareTo(new BigFraction(3)) >= 0);
    }

    @Test
    public void analyze10VotersPoA10Example() {
        final List<BigFraction> voterPositions = voters(19, 19, 19, 19, 19, 21, 21, 21, 21, 21);
        final Set<BigFraction> candidatePositions = candidates(10, 20 , 30);

        final GameAnalysis<?, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "10 voters and 3 candidates PoA=10 example");

        assertNash(analysis, true);

        Assert.assertTrue(analysis.getPrices().getPriceOfAnarchy().get().compareTo(new BigFraction(10)) == 0);
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

    public void test2kPoA(int k) {
        final int n = 2 * k;
        final Integer[] voters = new Integer[n];
        Arrays.fill(voters, 0, k, (4 * k) - 1);
        Arrays.fill(voters, k, n, (4 * k) + 1);

        final List<BigFraction> voterPositions = voters(voters);
        final Set<BigFraction> candidatePositions = candidates(n, 2 * n , 3 * n);

        final GameAnalysis<?, BigFraction> analysis = analyzeAndReport(
                voterPositions, candidatePositions, n + " voters and 3 candidates PoA=" + n + " example");

        assertNash(analysis, true);
        final BigFraction poa = analysis.getPrices().getPriceOfAnarchy().get();
        Assert.assertTrue(poa.compareTo(new BigFraction(n)) == 0);
    }

    public void test2kPlus1PoA(int k) {
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
        final BigFraction poa = analysis.getPrices().getPriceOfAnarchy().get();
        Assert.assertTrue("PoA too small: " + NumberUtils.fractionToString(poa), poa.compareTo(new BigFraction(n-1)) >= 0);
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

    public final List<BigFraction> voters(Integer... positions) {
        return voters(Arrays.asList(positions));
    }

    public List<BigFraction> voters(List<Integer> voters) {
        return voters.stream()
                .sorted()
                .map(BigFraction::new)
                .collect(Collectors.toList());
    }

    public final Set<BigFraction> candidates(Integer... positions) {
        return candidates(Arrays.asList(positions));
    }

    public Set<BigFraction> candidates(Collection<Integer> candidates) {
        return candidates.stream()
                .sorted()
                .map(BigFraction::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public final List<BigFraction> voters(BigFraction... positions) {
        final ArrayList<BigFraction> voters = Lists.newArrayList(positions);
        Collections.sort(voters);
        return voters;
    }

    public final Set<BigFraction> candidates(BigFraction... positions) {
        final ArrayList<BigFraction> candidates = Lists.newArrayList(positions);
        return candidates.stream()
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public GameAnalysis<VotingGameState<BigFraction>, BigFraction> analyzeRandomExample(int numberOfVoters, int minNumberOfCandidates, int maxNumberOfCandidates) {
        final List<BigFraction> voterPositions = getRandomVoters(numberOfVoters);
        final Set<BigFraction> candidatePositions = getRandomCandidates(minNumberOfCandidates, maxNumberOfCandidates);

        return analyzeAndReport(
                voterPositions, candidatePositions, "Random " + numberOfVoters + " voters example");
    }

    private Set<BigFraction> getRandomCandidates(int min, int max) {
        final OptionalInt numberOfCandidates = random.ints(min, max).findAny();

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
