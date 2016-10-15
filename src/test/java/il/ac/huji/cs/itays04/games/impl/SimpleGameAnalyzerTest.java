package il.ac.huji.cs.itays04.games.impl;

import il.ac.huji.cs.itays04.cli.StaticContext;
import il.ac.huji.cs.itays04.cli.VotingGameAnalysisRunner;
import il.ac.huji.cs.itays04.games.api.GameAnalysis;
import il.ac.huji.cs.itays04.rational.NamedRationalEntity;
import il.ac.huji.cs.itays04.rational.RationalUtils;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import org.apache.commons.math3.fraction.BigFraction;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;


public class SimpleGameAnalyzerTest {
    private final RationalUtils rationalUtils = StaticContext.getInstance().rationalUtils;
    private final VotingGameAnalysisRunner votingGameAnalysisRunner = StaticContext.getInstance().votingGameAnalysisRunner;

    @Test
    public void theorem5Example() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(14,7,12);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(17,2,10);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 5 example");

        assertConvergence(analysis, true);
    }

    @Test
    public void theorem8Example() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(16, 4, 1, 7);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(5,1,19,8);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 8 example");

        assertConvergence(analysis, false);
        assertNash(analysis, true);
    }

    @Test
    public void theorem10Example() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(400, 47, 92, 92, 92, 0, 0, 0, 0);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(0,92,400);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 10 example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void theorem11Example() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(15, 55, 42, 66, 74);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(15, 55, 42, 66, 74);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 11 example");

        assertConvergence(analysis, false);
        assertNash(analysis, true);
    }

    @Test
    public void theorem12Example() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(4, 32, 34, 48, 67);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(14, 32, 42, 60, 93);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 12 example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void theorem12SmallerExample() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(4, 32, 34, 48, 67);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(14, 32, 42, 60);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Theorem 12 without candidate 93");

        assertNash(analysis, false);
    }

    @Test
    public void proposition1Example() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(0, 11, 22, 33);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(0, 11, 19, 30);

        final GameAnalysis<?, ?> analysis = analyzeAndReport(
                voterPositions, candidatePositions, "Proposition 1 example");

        assertConvergence(analysis, false);
        assertNash(analysis, false);
    }

    @Test
    public void proposition3And6Example() {
        test2kPoS1(3);
    }

    @Test
    public void proposition4Example() {
        test2kPoA(3);
    }

    @Test
    public void proposition6OtherSideExample() {
        test2kPoSNot1(3);
    }

    @Test
    public void posWorseThanRandomizedDictatorshipExample() {
        final LinkedHashSet<NamedRationalEntity> voterPositions = rationalUtils.voters(4, 4, 10);
        final LinkedHashSet<NamedRationalEntity> candidatePositions = rationalUtils.candidates(0, 10);

        analyzeAndReport(voterPositions, candidatePositions, "PoS worse than DIC SMOOTH example");
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
}
