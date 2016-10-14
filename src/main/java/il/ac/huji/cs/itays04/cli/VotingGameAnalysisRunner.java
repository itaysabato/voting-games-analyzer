package il.ac.huji.cs.itays04.cli;

import il.ac.huji.cs.itays04.games.api.GameAnalysis;
import il.ac.huji.cs.itays04.games.api.GameAnalyzer;
import il.ac.huji.cs.itays04.games.api.SocialWelfareCalculator;
import il.ac.huji.cs.itays04.rational.BigFractionAverageSocialWelfareCalculator;
import il.ac.huji.cs.itays04.rational.NamedRationalEntity;
import il.ac.huji.cs.itays04.rational.NumberUtils;
import il.ac.huji.cs.itays04.utils.ImmutableDirectedGraphWithScc;
import il.ac.huji.cs.itays04.voting.*;
import il.ac.huji.cs.itays04.voting.weighted.QuadraticRandomizedVotingRule;
import il.ac.huji.cs.itays04.voting.weighted.RandomizedDictatorshipRandomizedVotingRule;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.*;
import java.util.stream.Collectors;

public class VotingGameAnalysisRunner {
    private final VotingGamesFactory votingGamesFactory;
    private final BigFractionAverageSocialWelfareCalculator welfareCalculator = new BigFractionAverageSocialWelfareCalculator();

    public VotingGameAnalysisRunner(VotingGamesFactory votingGamesFactory) {
        this.votingGamesFactory = votingGamesFactory;
    }

    public AnalysisWithRandomDicComparison<?, BigFraction> analyzeAndReport(
            List<LinkedHashMap<String, BigFraction>> allUtils,
            RandomizedVotingRule votingRule,
            String gameDescription,
            boolean quiet) {

        if (!quiet) {
            logHeader(gameDescription);
            log();
            log("Using explicit cardinal utilities: " + allUtils);
            log();
        }

        final VotingGame<String, BigFraction, BigFraction> game = votingGamesFactory.createGame(allUtils, votingRule);
        final ExpectedUtilityCalculator<String> randomDicCalc = votingGamesFactory.getCalculator(
                allUtils,
                new RandomizedDictatorshipRandomizedVotingRule());

        return analyzeAndReport(game, randomDicCalc, gameDescription, quiet);
    }

    public AnalysisWithRandomDicComparison<VotingGameState<NamedRationalEntity>, BigFraction> analyzeAndReport(
            LinkedHashSet<NamedRationalEntity> voterPositions,
            LinkedHashSet<NamedRationalEntity> candidatePositions,
            String gameDescription,
            boolean quiet) {

        return analyzeAndReport(
                voterPositions,
                candidatePositions,
                new QuadraticRandomizedVotingRule(),
                gameDescription,
                quiet);
    }

    public AnalysisWithRandomDicComparison<VotingGameState<NamedRationalEntity>, BigFraction> analyzeAndReport(
            LinkedHashSet<NamedRationalEntity> voters,
            LinkedHashSet<NamedRationalEntity> candidates,
            RandomizedVotingRule votingRule,
            String gameDescription,
            boolean quiet) {

        final VotingGame<NamedRationalEntity, BigFraction, BigFraction> game;
        synchronized (this) {
            game = getGame(voters, candidates, votingRule, gameDescription, quiet);
        }

        final ExpectedUtilityCalculator<NamedRationalEntity> randomDicCalc = getRandomDicCalculator(voters, candidates);
        return analyzeAndReport(game, randomDicCalc, gameDescription, quiet);
    }

    public <C> AnalysisWithRandomDicComparison<VotingGameState<C>, BigFraction> analyzeAndReport(
            VotingGame<C, BigFraction, BigFraction> game,
            ExpectedUtilityCalculator<C> randomDicCalc,
            String gameDescription,
            boolean quiet) {

        final GameAnalyzer gameAnalyzer = StaticContext.getInstance().gameAnalyzer;
        final ImmutableDirectedGraphWithScc<VotingGameState<C>> brg = gameAnalyzer.calculateBestResponseGraph(game);

        final GameAnalysis<VotingGameState<C>, BigFraction> gameAnalysis = gameAnalyzer.analyze(game, brg);
        Optional<BigFraction> randomDicPoSRatio = Optional.empty();

        synchronized (this) {
            if (!quiet) {
                StaticContext.getInstance()
                        .gameAnalysisReporter
                        .printReport(game, gameAnalysis, System.out);

                log("Truthful profiles:");
            }

            int i = 1;
            for (Map.Entry<VotingGameState<C>, BigFraction> entry : game.getTruthfulStates()
                    .entrySet()) {

                if (!quiet) {
                    log();
                    log((i++) + " - " + entry.getKey());
                    log("Original social welfare: " + NumberUtils.fractionToString(entry.getValue()));
                }

                final SocialWelfareCalculator<BigFraction, BigFraction> socialWelfareCalculator = game.getSocialWelfareCalculator();
                final BigFraction randomDicSW = socialWelfareCalculator.calculateWelfare(
                        game, randomDicCalc, entry.getKey());

                if (!quiet) {
                    log("Randomized Dictatorship social welfare: " + NumberUtils.fractionToString(randomDicSW));
                }
                final BigFraction socialOptimum = gameAnalysis.getPrices().getSocialOptimum();
                final BigFraction ratio = socialWelfareCalculator.getRatio(socialOptimum, randomDicSW);

                if (!quiet) {
                    log("Randomized Dictatorship social welfare ratio to optimum: " + NumberUtils.fractionToString(ratio));
                }
                final BigFraction priceOfStability = gameAnalysis.getPrices().getPriceOfStability().orElse(BigFraction.ZERO);
                final BigFraction priceOfStabilityRatio = socialWelfareCalculator.getRatio(priceOfStability, ratio);

                if (!quiet) {
                    log("Randomized Dictatorship social welfare ratio to best original NE: " + NumberUtils.fractionToString(priceOfStabilityRatio));
                }

                if (!quiet && priceOfStabilityRatio.compareTo(BigFraction.ONE) > 0) {
                    log("Randomized Dictatorship social welfare is better than best original NE!");
                }

                if (!randomDicPoSRatio.isPresent()
                        || randomDicPoSRatio.get().compareTo(priceOfStabilityRatio) < 0) {

                    randomDicPoSRatio = Optional.of(priceOfStabilityRatio);
                }
            }

            if (!quiet) {
                log();
                log("End Analysis of " + gameDescription + ".");
                log();
            }
        }

        return new AnalysisWithRandomDicComparison<>(randomDicPoSRatio, gameAnalysis);
    }

    private ExpectedUtilityCalculator<NamedRationalEntity> getRandomDicCalculator(
            LinkedHashSet<NamedRationalEntity> voterPositions,
            LinkedHashSet<NamedRationalEntity> candidatePositions) {

        return votingGamesFactory.createDistanceBasedCalculator(
                voterPositions,
                candidatePositions,
                new RandomizedDictatorshipRandomizedVotingRule());
    }

    private VotingGame<NamedRationalEntity, BigFraction, BigFraction> getGame(
            LinkedHashSet<NamedRationalEntity> voters,
            LinkedHashSet<NamedRationalEntity> candidates,
            RandomizedVotingRule votingRule,
            String gameDescription,
            boolean quiet) {

        if (!quiet) {
            logHeader(gameDescription);

            log("with voters:");
            logEntities(voters);
            log();

            log("and candidates:");
            logEntities(candidates);
            log();
        }

        return votingGamesFactory.createDistanceBasedGame(
                voters, candidates, votingRule, welfareCalculator);
    }

    private void logHeader(String gameDescription) {
        log("************************************************************************************************");
        log("Analyzing " + gameDescription);
        log("************************************************************************************************");
    }

    private void logEntities(Collection<NamedRationalEntity> entities) {
        log(entities.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n")));
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
