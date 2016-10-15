# Voting Games Analyzer
A command line tool for analyzing randomized voting games. It defaults to using the Quadratic voting rule and the 1-euclidian preferences domain but can be customized easily.

[First Official Release v0.1](https://github.com/itaysabato/voting-games-analyzer/releases/tag/v0.1) is ready!
## Getting Started
- First make sure you have an up-to-date [Java Runtime Environment](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
- Then download and extract the [binaries](https://github.com/itaysabato/voting-games-analyzer/releases/download/v0.1/vga.zip)
- No further installation is required --- simply invoke the `vga` executable from any terminal

The quickest way to see it in action is to analyze a random game:
```
$ vga -r
************************************************************************************************
Analyzing Quadratic Voting Game 1
************************************************************************************************
with voters:
V1 = -283569466
V2 = 722491586

and candidates:
C1 = -1342501127
C2 = 1325639602

********************************
Game Analysis
********************************
Type: Voting Game
Number of voters: 2
Number of candidates: 2

Utility function: Expected utility based on the Quadratic voting rule and the following cardinal utilities:
Voter 1: -1058931661, -1609209068
Voter 2: -2064992713, -603148016

Social welfare function: Average

Number of possible game states: 4
Number of pure Nash equilibria: 1

Price of Anarchy: 2668140729 / 2212357084 (1.206)
Price of Sinking: 2668140729 / 2212357084 (1.206)
Price of Stability: 2668140729 / 2212357084 (1.206)

Number of strongly connected components in best response graph: 4
Number of components with cycles: 0
Number of sink-equilibria: 1

Social optimum: -1106178542
Socially optimal states:
1 - {V1 votes [C2 = 1325639602], V2 votes [C2 = 1325639602]}
---------------------------------
Sinks
---------------------------------
Sink #1
Component id: 0
Number of states: 1
Average welfare: -2668140729 / 2 (-1334070364.5)
Ratio to social optimum: 2668140729 / 2212357084 (1.206)

States:
S1 - {V1 votes [C1 = -1342501127], V2 votes [C2 = 1325639602]}

Sink edges:
N/A

Longest path to sink length: 2
Longest path to sink: {V1 votes [C2 = 1325639602], V2 votes [C1 = -1342501127]} => {V1 votes [C1 = -1342501127], V2 votes [C1 = -1342501127]} => {V1 votes [C1 = -1342501127], V2 votes [C2 = 1325639602]}
---------------------------------

Truthful profiles:

1 - {V1 votes [C1 = -1342501127], V2 votes [C2 = 1325639602]}
Original social welfare: -2668140729 / 2 (-1334070364.5)
Randomized Dictatorship social welfare: -2668140729 / 2 (-1334070364.5)
Randomized Dictatorship social welfare ratio to optimum: 2668140729 / 2212357084 (1.206)
Randomized Dictatorship social welfare ratio to best original NE: 1

End Analysis of Quadratic Voting Game 1.

{
    "numberOfGames" : 1,
    "avgNeCount" : 1,
    "percentageWithNe" : 100,
    "convergingPercentage" : 100,
    "avgPrices" : {
        "socialOptimum" : -1106178542,
        "priceOfSinking" : 1.206,
        "priceOfAnarchy" : 1.206,
        "priceOfStability" : 1.206
    },
    "betterPoSThanRandomizedDictatorshipPercentage" : 100
}
```
With the `-q` option, you can analyze and aggregate many games without all the extra output:
```
$ vga -r -n 100 -q
{
    "numberOfGames" : 100,
    "avgNeCount" : 1.39,
    "percentageWithNe" : 100,
    "convergingPercentage" : 63,
    "avgPrices" : {
        "socialOptimum" : -968278497.588,
        "priceOfSinking" : 1.1162,
        "priceOfAnarchy" : 1.1162,
        "priceOfStability" : 1.0872
    },
    "betterPoSThanRandomizedDictatorshipPercentage" : 98
}
```
You can use `-h` or `--help` to get a detailed specification of the various options:
```
$ vga -h
Usage: vga [options]

  Options:
  
  -h, --help
       Display help message.
       Default: false
  
  -r, --randomize
       Generate random positions. If this flag is not set, voters and candidates
       must be explicitly specified. If the -u option is set, this flag will be
       ignored.
       Default: false
  
  -q, --quiet
       Do not output individual game analyses, only final aggregation.
       Default: false
  
  -n, --number-games
       The number of games to analyze.
       Default: 1
  
  -v, --voters
       A comma-separated list of voter positions as rational numbers of the form
       '1', '-2', '3/2', etc. If the -u option is set, the first number will be read
       as the number of voters.
       Default: []
  
  -c, --candidates
       A comma-separated list of candidate positions as rational numbers of the
       form '1', '-2', '3/2', etc. If the -u option is set, the first number will be
       read as the number of candidates.
       Default: []
  
  -nc, --no-candidates
       Use the voters as the only candidates. Other options regarding candidates
       are ignored if this flag is set. If the -u option is set, this flag indicates
       that the number of candidates is equal to the number of voters.
       Default: false
  
  -rv, --random-voters
       The number of random voters to generate. It is also possible to give a
       range from which the amount will be chosen uniformly, e.g. 2,4 or 1,3.
       Default: [2, 5]
  
  -rc, --random-candidates
       The number of random candidates to generate. It is also possible to give
       a range from which the amount will be chosen uniformly, e.g. 2,4 or 1,3.
       Default: [2, 5]
  
  -u, --utilities
       A comma-separated list of cardinal utilities, starting with the utility
       the first voter gets from the first candidate, then the second candidate,
       etc. followed by a list for the second voter and so on until the last voter.
       For example, if we have 2 voters and 3 candidates then "1,2,1/3,4,23,7/5"
       would be a valid list. If this option is set, the number of voters and number
       of candidates should be given via -v and -c, respectively, instead of the
       position lists.
       Default: []
  
  -vr, --voting-rule
       The fully qualified name of a java class implementing the interface
       RandomizedVotingRule to be used instead of the Quadratic voting rule. The compiled class must
       have a no-argument constructor and be present in the lib folder.
       Default: il.ac.huji.cs.itays04.voting.weighted.QuadraticRandomizedVotingRule
```
## Partly Random Games
You can mix random and explicitly specified voters and candidates. The way it works is that once the `-r` flag is set, the generated voters/candidates are _added_ to the explicitly specified ones, for example:

- Running `$ vga -r -v 1,2,3 -rv 0` will generate random candidates but only the three specified voters will be used.
- Running `$ vga -r -v 1,2,3 -rv 0,1` will add a fourth random voter with 50% probability.

## Custom Voting Rule Example
The following is an implementation of the plurality voting rule with random tie-breaking.
```java
import java.util.*;
import org.apache.commons.math3.fraction.BigFraction;
import il.ac.huji.cs.itays04.voting.RandomizedVotingRule;
 
public class PluralityVotingRule implements RandomizedVotingRule {
    @Override
    public String getName() {
        return "Plurality";
    }
 
    @Override
    public <C> Map<C, BigFraction> getWinnerDistribution(List<C> votes, Set<C> candidatesSet) {
        final HashSet<C> topCandidates = getTopCandidates(votes, candidatesSet);
        return topLottery(candidatesSet, topCandidates);
    }
 
    public <C> Map<C, BigFraction> topLottery(Set<C> candidatesSet, HashSet<C> topCandidates) {
        final HashMap<C, BigFraction> distribution = new HashMap<>();
 
        for (C candidate : candidatesSet) {
            final BigFraction probability;
 
            if (topCandidates.contains(candidate)) {
                probability = new BigFraction(1, topCandidates.size());
            }
            else {
                probability = BigFraction.ZERO;
            }
 
            distribution.put(candidate, probability);
        }
 
        return distribution;
    }
 
    public <C> HashSet<C> getTopCandidates(List<C> votes, Set<C> candidatesSet) {
        int maxVotes = 0;
        final HashSet<C> topCandidates = new HashSet<>();
 
        for (C candidate : candidatesSet) {
            int i = 0;
            for (C vote : votes) {
                if (vote.equals(candidate)) {
                    i++;
                }
            }
 
            if (i > maxVotes) {
                maxVotes = i;
                topCandidates.clear();
            }
 
            if (i == maxVotes) {
                topCandidates.add(candidate);
            }
        }
        return topCandidates;
    }
}
```
In order to use a custom voting rule, after compiling, move the `.class` file to the `lib` folder along with any other classes you need and pass the fully qualified class name via the command line, e.g.
```
$ vga -v 1,2,3 -nc -vr PluralityVotingRule
************************************************************************************************
Analyzing Plurality Voting Game 1
************************************************************************************************
with voters:
V1 = 1
V2 = 2
V3 = 3

and candidates:
V1 = 1
V2 = 2
V3 = 3

********************************
Game Analysis
********************************
Type: Voting Game
Number of voters: 3
Number of candidates: 3

Utility function: Expected utility based on the Plurality voting rule and the following cardinal utilities:
Voter 1: 0, -1, -2
Voter 2: -1, 0, -1
Voter 3: -2, -1, 0

Social welfare function: Average

Number of possible game states: 27
Number of pure Nash equilibria: 6

Price of Anarchy: 3 / 2 (1.5)
Price of Sinking: 3 / 2 (1.5)
Price of Stability: 1

Number of strongly connected components in best response graph: 27
Number of components with cycles: 0
Number of sink-equilibria: 6

Social optimum: -2 / 3 (-0.6667)
Socially optimal states:
1 - {V1 votes [V2 = 2], V2 votes [V2 = 2], V3 votes [V1 = 1]}
2 - {V1 votes [V1 = 1], V2 votes [V2 = 2], V3 votes [V2 = 2]}
3 - {V1 votes [V2 = 2], V2 votes [V3 = 3], V3 votes [V2 = 2]}
4 - {V1 votes [V2 = 2], V2 votes [V2 = 2], V3 votes [V2 = 2]}
5 - {V1 votes [V2 = 2], V2 votes [V2 = 2], V3 votes [V3 = 3]}
6 - {V1 votes [V2 = 2], V2 votes [V1 = 1], V3 votes [V2 = 2]}
7 - {V1 votes [V3 = 3], V2 votes [V2 = 2], V3 votes [V2 = 2]}
---------------------------------
Sinks
---------------------------------
Sink #1
Component id: 16
Number of states: 1
Average welfare: -2 / 3 (-0.6667)
Ratio to social optimum: 1

States:
S1 - {V1 votes [V2 = 2], V2 votes [V2 = 2], V3 votes [V2 = 2]}

Sink edges:
N/A

Longest path to sink length: 0
Longest path to sink: {V1 votes [V2 = 2], V2 votes [V2 = 2], V3 votes [V2 = 2]}
---------------------------------
Sink #2
Component id: 0
Number of states: 1
Average welfare: -8 / 9 (-0.8889)
Ratio to social optimum: 4 / 3 (1.3333)

States:
S1 - {V1 votes [V1 = 1], V2 votes [V2 = 2], V3 votes [V3 = 3]}

Sink edges:
N/A

Longest path to sink length: 5
Longest path to sink: {V1 votes [V2 = 2], V2 votes [V1 = 1], V3 votes [V1 = 1]} => {V1 votes [V2 = 2], V2 votes [V1 = 1], V3 votes [V2 = 2]} => {V1 votes [V1 = 1], V2 votes [V1 = 1], V3 votes [V2 = 2]} => {V1 votes [V1 = 1], V2 votes [V3 = 3], V3 votes [V2 = 2]} => {V1 votes [V1 = 1], V2 votes [V3 = 3], V3 votes [V3 = 3]} => {V1 votes [V1 = 1], V2 votes [V2 = 2], V3 votes [V3 = 3]}
---------------------------------
Sink #3
Component id: 7
Number of states: 1
Average welfare: -2 / 3 (-0.6667)
Ratio to social optimum: 1

States:
S1 - {V1 votes [V2 = 2], V2 votes [V2 = 2], V3 votes [V3 = 3]}

Sink edges:
N/A

Longest path to sink length: 4
Longest path to sink: {V1 votes [V2 = 2], V2 votes [V1 = 1], V3 votes [V1 = 1]} => {V1 votes [V2 = 2], V2 votes [V3 = 3], V3 votes [V1 = 1]} => {V1 votes [V2 = 2], V2 votes [V3 = 3], V3 votes [V3 = 3]} => {V1 votes [V2 = 2], V2 votes [V1 = 1], V3 votes [V3 = 3]} => {V1 votes [V2 = 2], V2 votes [V2 = 2], V3 votes [V3 = 3]}
---------------------------------
Sink #4
Component id: 3
Number of states: 1
Average welfare: -1
Ratio to social optimum: 3 / 2 (1.5)

States:
S1 - {V1 votes [V1 = 1], V2 votes [V1 = 1], V3 votes [V1 = 1]}

Sink edges:
N/A

Longest path to sink length: 0
Longest path to sink: {V1 votes [V1 = 1], V2 votes [V1 = 1], V3 votes [V1 = 1]}
---------------------------------
Sink #5
Component id: 26
Number of states: 1
Average welfare: -1
Ratio to social optimum: 3 / 2 (1.5)

States:
S1 - {V1 votes [V3 = 3], V2 votes [V3 = 3], V3 votes [V3 = 3]}

Sink edges:
N/A

Longest path to sink length: 0
Longest path to sink: {V1 votes [V3 = 3], V2 votes [V3 = 3], V3 votes [V3 = 3]}
---------------------------------
Sink #6
Component id: 2
Number of states: 1
Average welfare: -2 / 3 (-0.6667)
Ratio to social optimum: 1

States:
S1 - {V1 votes [V1 = 1], V2 votes [V2 = 2], V3 votes [V2 = 2]}

Sink edges:
N/A

Longest path to sink length: 4
Longest path to sink: {V1 votes [V2 = 2], V2 votes [V1 = 1], V3 votes [V1 = 1]} => {V1 votes [V2 = 2], V2 votes [V1 = 1], V3 votes [V2 = 2]} => {V1 votes [V1 = 1], V2 votes [V1 = 1], V3 votes [V2 = 2]} => {V1 votes [V1 = 1], V2 votes [V3 = 3], V3 votes [V2 = 2]} => {V1 votes [V1 = 1], V2 votes [V2 = 2], V3 votes [V2 = 2]}
---------------------------------

Truthful profiles:

1 - {V1 votes [V1 = 1], V2 votes [V2 = 2], V3 votes [V3 = 3]}
Original social welfare: -8 / 9 (-0.8889)
Randomized Dictatorship social welfare: -8 / 9 (-0.8889)
Randomized Dictatorship social welfare ratio to optimum: 4 / 3 (1.3333)
Randomized Dictatorship social welfare ratio to best original NE: 3 / 4 (0.75)

End Analysis of Plurality Voting Game 1.

{
    "numberOfGames" : 1,
    "avgNeCount" : 6,
    "percentageWithNe" : 100,
    "convergingPercentage" : 100,
    "avgPrices" : {
        "socialOptimum" : -0.6667,
        "priceOfSinking" : 1.5,
        "priceOfAnarchy" : 1.5,
        "priceOfStability" : 1
    },
    "betterPoSThanRandomizedDictatorshipPercentage" : 100
}
```
