package il.ac.huji.cs.itays04.cli;

import il.ac.huji.cs.itays04.games.api.BigFractionAverageSocialWelfareCalculator;
import il.ac.huji.cs.itays04.games.api.GameAnalysis;
import il.ac.huji.cs.itays04.games.api.GameAnalyzer;
import il.ac.huji.cs.itays04.games.api.SocialWelfareCalculator;
import il.ac.huji.cs.itays04.utils.ImmutableDirectedGraphWithScc;
import il.ac.huji.cs.itays04.utils.NumberUtils;
import il.ac.huji.cs.itays04.voting.VotingGame;
import il.ac.huji.cs.itays04.voting.VotingGameState;
import il.ac.huji.cs.itays04.voting.quadratic.QuadraticFactory;
import il.ac.huji.cs.itays04.voting.quadratic.WeightedUtilityCalculator;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.List;
import java.util.Set;

public class AnalysisRunner {
    private final QuadraticFactory quadraticFactory;
    private final BigFractionAverageSocialWelfareCalculator<VotingGameState<BigFraction>> welfareCalculator = new BigFractionAverageSocialWelfareCalculator<>();

    public AnalysisRunner(QuadraticFactory quadraticFactory) {
        this.quadraticFactory = quadraticFactory;
    }

    public GameAnalysis<VotingGameState<BigFraction>, BigFraction> analyzeAndReport(
            List<BigFraction> voterPositions,
            Set<BigFraction> candidatePositions,
            String gameDescription) {

        return analyzeAndReport(voterPositions, candidatePositions, gameDescription, false);
    }

    public GameAnalysis<VotingGameState<BigFraction>, BigFraction> analyzeAndReport(
            List<BigFraction> voterPositions,
            Set<BigFraction> candidatePositions,
            String gameDescription,
            boolean quiet) {

        final VotingGame<BigFraction, BigFraction, BigFraction> game;
        synchronized (this) {
            game = getGame(voterPositions, candidatePositions, gameDescription, quiet);
        }

        final GameAnalyzer gameAnalyzer = StaticContext.getInstance().gameAnalyzer;
        final ImmutableDirectedGraphWithScc<VotingGameState<BigFraction>> brg = gameAnalyzer.calculateBestResponseGraph(game);

        final GameAnalysis<VotingGameState<BigFraction>, BigFraction> gameAnalysis = gameAnalyzer.analyze(game, brg);

        if (!quiet) {
            synchronized (this) {
                StaticContext.getInstance()
                        .gameAnalysisReporter
                        .printReport(game, gameAnalysis, System.out);

                log("Truthful profiles:");
                log();

                final WeightedUtilityCalculator<BigFraction> randomDicCalc = getRandomDicCalculator(voterPositions, candidatePositions);

                game.getTruthfulStates()
                        .entrySet()
                        .stream()
                        .forEachOrdered(entry -> {
                            log(entry.getKey());
                            log("SW = " + NumberUtils.fractionToString(entry.getValue()));

                            final SocialWelfareCalculator<VotingGameState<BigFraction>, BigFraction, BigFraction> socialWelfareCalculator = game.getSocialWelfareCalculator();
                            final BigFraction randomDicSW = socialWelfareCalculator.calculateWelfare(
                                    game, randomDicCalc, entry.getKey());

                            log("Randomized dictatorship SW = " + NumberUtils.fractionToString(randomDicSW));

                            final BigFraction socialOptimum = gameAnalysis.getPrices().getSocialOptimum();
                            final BigFraction ratio = socialWelfareCalculator.getRatio(socialOptimum, randomDicSW);
                            log("Randomized dictatorship ratio to optimum = " + NumberUtils.fractionToString(ratio));

                            final BigFraction priceOfStability = gameAnalysis.getPrices().getPriceOfStability().orElse(BigFraction.ZERO);
                            final BigFraction priceOfStabilityRatio = socialWelfareCalculator.getRatio(priceOfStability, ratio);
                            log("Randomized dictatorship ratio to price of stability = " + NumberUtils.fractionToString(priceOfStabilityRatio));
                            log();

                            if (priceOfStabilityRatio.compareTo(BigFraction.ONE) > 0) {
                                log("PoS worse than random dic!");
                                System.exit(0);
                            }
                        });

                log("End analysis.");
            }
        }

        return gameAnalysis;
    }

    private WeightedUtilityCalculator<BigFraction> getRandomDicCalculator(List<BigFraction> voterPositions, Set<BigFraction> candidatePositions) {
        return quadraticFactory.createWeightedCalculator(
                voterPositions, candidatePositions, false);
    }

    private VotingGame<BigFraction, BigFraction, BigFraction> getGame(
            List<BigFraction> voterPositions,
            Set<BigFraction> candidatePositions,
            String gameDescription,
            boolean quiet) {

        if (!quiet) {
            log("************************************************************************************************");
            log("Analyzing " + gameDescription);
            log("************************************************************************************************");

            log("with voters:");
            for (int i = 0; i < voterPositions.size(); i++) {
                log("V" + (i+1) + " = " + NumberUtils.fractionToString(voterPositions.get(i)));
            }

            log();
            log("and candidates:");
            candidatePositions.stream()
                    .sequential()
                    .map(NumberUtils::fractionToString)
                    .forEachOrdered(this::log);
        }

        return quadraticFactory.createDistanceBasedGame(
                voterPositions, candidatePositions, welfareCalculator);
    }

    private void log() {
        log("");
    }

    private void log(Object message) {
        final long id = Thread.currentThread().getId();

        final String prefix = id == 1 ?
                "" : id + " - ";

        System.out.println(prefix + message);
    }
}
