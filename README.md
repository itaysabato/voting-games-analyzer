# Voting Games Analyzer
A command line tool for analyzing voting games, in particular, ones that use randomized voting rules.
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
With the `-q` option, you can analyize and aggregate many games without all the noise:
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
## Usage
```
$ vga -h
Usage: vga [options]
  Options:
    -c, --candidates
       A comma-separated list of candidate positions, as rational numbers of the
       form '1', '-2', '3/2', etc. If the -u option is set, only the first number
       will be read, and floored to an integer.
       Default: []
       
    -h, --help
       Display help message.
       Default: false
       
    -nc, --no-candidates
       Use the voters as the only candidates. Other options regarding candidates
       are ignored if this flag is set. If the -u option is set, this flag indicates
       that the number of candidates is equal to the number of voters.
       Default: false
       
    -n, --number-games
       The number of games to analyze.
       Default: 1
       
    -q, --quiet
       Do not output individual game analyses, only final aggregation.
       Default: false
       
    -rc, --random-candidates
       The number of random candidates to generate. It is also possible to give
       a range from which the amount will be chosen uniformly, e.g. 2,4 or 1,3.
       Default: [2, 5]
       
    -rv, --random-voters
       The number of random voters to generate. It is also possible to give a
       range from which the amount will be chosen uniformly, e.g. 2,4 or 1,3.
       Default: [2, 5]
       
    -r, --randomize
       Generate random positions. If this flag is not set, voters and candidates
       must be explicitly specified. If the -u option is set, this flag will be
       ignored.
       Default: false
       
    -u, --utilities
       A comma-separated list of cardinal utilities, starting with the utilities
       the first voter gets from the first candidates, then the second candidates,
       etc. followed by a list for the second voter and so on until the last voter.
       For example, if we have 2 voters and 3 candidates then "1,2,1/3,4,23,7/5"
       would be a valid list. If this option is set, the number of voters and number
       of candidates should be given instead of their positions lists via -v and -c
       respectively.
       Default: []
       
    -v, --voters
       A comma-separated list of voter positions, as rational numbers of the
       form '1', '-2', '3/2', etc. If the -u option is set, only the first number
       will be read, and floored to an integer.
       Default: []
       
    -vr, --voting-rule
       The fully qualified name of a java class implementing the interface
       RandomizedVotingRule to be used instead of the Quadratic voting rule. The compiled class must
       have a no-argument constructor and be present in the lib folder.
       Default: il.ac.huji.cs.itays04.voting.weighted.QuadraticRandomizedVotingRule
```
## Custom Voting Rule Example
The following is an implementation of the plurality voting rule with random tie-breaking.
```java
PluralityVotingRule.java:

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
$ vga -r -vr PluralityVotingRule
```
The output may look something like this:
```
************************************************************************************************
Analyzing Plurality Voting Game 1
************************************************************************************************
with voters:
V1 = -1775299842
V2 = 96261999
V3 = 649111821
V4 = 1752177068

and candidates:
C1 = -1470754296
C2 = -1273974290
C3 = 1846731908

********************************
Game Analysis
********************************
Type: Voting Game
Number of voters: 4
Number of candidates: 3

Utility function: Expected utility based on the Plurality voting rule and the following cardinal utilities:
Voter 1: -304545546, -501325552, -3622031750
Voter 2: -1567016295, -1370236289, -1750469909
Voter 3: -2119866117, -1923086111, -1197620087
Voter 4: -3222931364, -3026151358, -94554840

Social welfare function: Average

Number of possible game states: 81
Number of pure Nash equilibria: 6

Price of Anarchy: 3607179661 / 3332338293 (1.0825)
Price of Sinking: 3607179661 / 3332338293 (1.0825)
Price of Stability: 1

Number of strongly connected components in best response graph: 44
Number of components with cycles: 1
Number of sink-equilibria: 6

Social optimum: -3332338293 / 2 (-1666169146.5)
Socially optimal states:
1 - {V1 votes [C3 = 1846731908], V2 votes [C3 = 1846731908], V3 votes [C3 = 1846731908], V4 votes [C2 = -1273974290]}
2 - {V1 votes [C2 = -1273974290], V2 votes [C3 = 1846731908], V3 votes [C3 = 1846731908], V4 votes [C1 = -1470754296]}
3 - {V1 votes [C3 = 1846731908], V2 votes [C2 = -1273974290], V3 votes [C1 = -1470754296], V4 votes [C3 = 1846731908]}
4 - {V1 votes [C3 = 1846731908], V2 votes [C2 = -1273974290], V3 votes [C3 = 1846731908], V4 votes [C1 = -1470754296]}
5 - {V1 votes [C2 = -1273974290], V2 votes [C1 = -1470754296], V3 votes [C3 = 1846731908], V4 votes [C3 = 1846731908]}
6 - {V1 votes [C1 = -1470754296], V2 votes [C3 = 1846731908], V3 votes [C2 = -1273974290], V4 votes [C3 = 1846731908]}
7 - {V1 votes [C3 = 1846731908], V2 votes [C3 = 1846731908], V3 votes [C3 = 1846731908], V4 votes [C3 = 1846731908]}
8 - {V1 votes [C1 = -1470754296], V2 votes [C2 = -1273974290], V3 votes [C3 = 1846731908], V4 votes [C3 = 1846731908]}
9 - {V1 votes [C3 = 1846731908], V2 votes [C3 = 1846731908], V3 votes [C3 = 1846731908], V4 votes [C1 = -1470754296]}
10 - {V1 votes [C3 = 1846731908], V2 votes [C1 = -1470754296], V3 votes [C3 = 1846731908], V4 votes [C3 = 1846731908]}
11 - {V1 votes [C3 = 1846731908], V2 votes [C3 = 1846731908], V3 votes [C1 = -1470754296], V4 votes [C2 = -1273974290]}
12 - {V1 votes [C2 = -1273974290], V2 votes [C3 = 1846731908], V3 votes [C1 = -1470754296], V4 votes [C3 = 1846731908]}
13 - {V1 votes [C3 = 1846731908], V2 votes [C3 = 1846731908], V3 votes [C1 = -1470754296], V4 votes [C3 = 1846731908]}
14 - {V1 votes [C3 = 1846731908], V2 votes [C1 = -1470754296], V3 votes [C3 = 1846731908], V4 votes [C2 = -1273974290]}
15 - {V1 votes [C3 = 1846731908], V2 votes [C2 = -1273974290], V3 votes [C3 = 1846731908], V4 votes [C3 = 1846731908]}
16 - {V1 votes [C2 = -1273974290], V2 votes [C3 = 1846731908], V3 votes [C3 = 1846731908], V4 votes [C3 = 1846731908]}
17 - {V1 votes [C1 = -1470754296], V2 votes [C3 = 1846731908], V3 votes [C3 = 1846731908], V4 votes [C2 = -1273974290]}
18 - {V1 votes [C3 = 1846731908], V2 votes [C3 = 1846731908], V3 votes [C2 = -1273974290], V4 votes [C1 = -1470754296]}
19 - {V1 votes [C3 = 1846731908], V2 votes [C1 = -1470754296], V3 votes [C2 = -1273974290], V4 votes [C3 = 1846731908]}
20 - {V1 votes [C1 = -1470754296], V2 votes [C3 = 1846731908], V3 votes [C3 = 1846731908], V4 votes [C3 = 1846731908]}
21 - {V1 votes [C3 = 1846731908], V2 votes [C3 = 1846731908], V3 votes [C2 = -1273974290], V4 votes [C3 = 1846731908]}
---------------------------------
Sinks
---------------------------------
Sink #1
Component id: 0
Number of states: 1
Average welfare: -3410399655 / 2 (-1705199827.5)
Ratio to social optimum: 1136799885 / 1110779431 (1.0234)

States:
S1 - {V1 votes [C1 = -1470754296], V2 votes [C2 = -1273974290], V3 votes [C2 = -1273974290], V4 votes [C2 = -1273974290]}

Sink edges:
N/A

Longest path to sink length: 45
Longest path to sink: {V1 votes [C3 = 1846731908], V2 votes [C3 = 1846731908], V3 votes [C1 = -1470754296], V4 votes [C1 = -1470754296]} => {V1 votes [C2 = -1273974290], V2 votes [C3 = 1846731908], V3 votes [C1 = -1470754296], V4 votes [C1 = -1470754296]} => {V1 votes [C2 = -1273974290], V2 votes [C2 = -1273974290], V3 votes [C1 = -1470754296], V4 votes [C1 = -1470754296]} => {V1 votes [C3 = 1846731908], V2 votes [C2 = -1273974290], V3 votes [C1 = -1470754296], V4 votes [C1 = -1470754296]} => SCC#23 => {V1 votes [C2 = -1273974290], V2 votes [C2 = -1273974290], V3 votes [C1 = -1470754296], V4 votes [C3 = 1846731908]} => {V1 votes [C1 = -1470754296], V2 votes [C2 = -1273974290], V3 votes [C1 = -1470754296], V4 votes [C3 = 1846731908]} => {V1 votes [C1 = -1470754296], V2 votes [C2 = -1273974290], V3 votes [C1 = -1470754296], V4 votes [C2 = -1273974290]} => {V1 votes [C1 = -1470754296], V2 votes [C2 = -1273974290], V3 votes [C2 = -1273974290], V4 votes [C2 = -1273974290]}
---------------------------------
Sink #2
Component id: 39
Number of states: 1
Average welfare: -3332338293 / 2 (-1666169146.5)
Ratio to social optimum: 1

States:
S1 - {V1 votes [C3 = 1846731908], V2 votes [C3 = 1846731908], V3 votes [C3 = 1846731908], V4 votes [C3 = 1846731908]}

Sink edges:
N/A

Longest path to sink length: 0
Longest path to sink: {V1 votes [C3 = 1846731908], V2 votes [C3 = 1846731908], V3 votes [C3 = 1846731908], V4 votes [C3 = 1846731908]}
---------------------------------
Sink #3
Component id: 1
Number of states: 1
Average welfare: -3469758977 / 2 (-1734879488.5)
Ratio to social optimum: 3469758977 / 3332338293 (1.0412)

States:
S1 - {V1 votes [C1 = -1470754296], V2 votes [C1 = -1470754296], V3 votes [C3 = 1846731908], V4 votes [C3 = 1846731908]}

Sink edges:
N/A

Longest path to sink length: 47
Longest path to sink: {V1 votes [C3 = 1846731908], V2 votes [C3 = 1846731908], V3 votes [C1 = -1470754296], V4 votes [C1 = -1470754296]} => {V1 votes [C2 = -1273974290], V2 votes [C3 = 1846731908], V3 votes [C1 = -1470754296], V4 votes [C1 = -1470754296]} => {V1 votes [C2 = -1273974290], V2 votes [C2 = -1273974290], V3 votes [C1 = -1470754296], V4 votes [C1 = -1470754296]} => {V1 votes [C3 = 1846731908], V2 votes [C2 = -1273974290], V3 votes [C1 = -1470754296], V4 votes [C1 = -1470754296]} => SCC#23 => {V1 votes [C2 = -1273974290], V2 votes [C2 = -1273974290], V3 votes [C1 = -1470754296], V4 votes [C3 = 1846731908]} => {V1 votes [C1 = -1470754296], V2 votes [C2 = -1273974290], V3 votes [C1 = -1470754296], V4 votes [C3 = 1846731908]} => {V1 votes [C1 = -1470754296], V2 votes [C2 = -1273974290], V3 votes [C1 = -1470754296], V4 votes [C2 = -1273974290]} => {V1 votes [C1 = -1470754296], V2 votes [C2 = -1273974290], V3 votes [C3 = 1846731908], V4 votes [C2 = -1273974290]} => {V1 votes [C1 = -1470754296], V2 votes [C2 = -1273974290], V3 votes [C3 = 1846731908], V4 votes [C3 = 1846731908]} => {V1 votes [C1 = -1470754296], V2 votes [C1 = -1470754296], V3 votes [C3 = 1846731908], V4 votes [C3 = 1846731908]}
---------------------------------
Sink #4
Component id: 38
Number of states: 1
Average welfare: -3607179661 / 2 (-1803589830.5)
Ratio to social optimum: 3607179661 / 3332338293 (1.0825)

States:
S1 - {V1 votes [C1 = -1470754296], V2 votes [C1 = -1470754296], V3 votes [C1 = -1470754296], V4 votes [C1 = -1470754296]}

Sink edges:
N/A

Longest path to sink length: 0
Longest path to sink: {V1 votes [C1 = -1470754296], V2 votes [C1 = -1470754296], V3 votes [C1 = -1470754296], V4 votes [C1 = -1470754296]}
---------------------------------
Sink #5
Component id: 43
Number of states: 1
Average welfare: -3410399655 / 2 (-1705199827.5)
Ratio to social optimum: 1136799885 / 1110779431 (1.0234)

States:
S1 - {V1 votes [C2 = -1273974290], V2 votes [C2 = -1273974290], V3 votes [C2 = -1273974290], V4 votes [C2 = -1273974290]}

Sink edges:
N/A

Longest path to sink length: 0
Longest path to sink: {V1 votes [C2 = -1273974290], V2 votes [C2 = -1273974290], V3 votes [C2 = -1273974290], V4 votes [C2 = -1273974290]}
---------------------------------
Sink #6
Component id: 2
Number of states: 1
Average welfare: -1685684487
Ratio to social optimum: 1123789658 / 1110779431 (1.0117)

States:
S1 - {V1 votes [C2 = -1273974290], V2 votes [C2 = -1273974290], V3 votes [C3 = 1846731908], V4 votes [C3 = 1846731908]}

Sink edges:
N/A

Longest path to sink length: 47
Longest path to sink: {V1 votes [C3 = 1846731908], V2 votes [C3 = 1846731908], V3 votes [C1 = -1470754296], V4 votes [C1 = -1470754296]} => {V1 votes [C2 = -1273974290], V2 votes [C3 = 1846731908], V3 votes [C1 = -1470754296], V4 votes [C1 = -1470754296]} => {V1 votes [C2 = -1273974290], V2 votes [C2 = -1273974290], V3 votes [C1 = -1470754296], V4 votes [C1 = -1470754296]} => {V1 votes [C3 = 1846731908], V2 votes [C2 = -1273974290], V3 votes [C1 = -1470754296], V4 votes [C1 = -1470754296]} => SCC#23 => {V1 votes [C2 = -1273974290], V2 votes [C2 = -1273974290], V3 votes [C1 = -1470754296], V4 votes [C3 = 1846731908]} => {V1 votes [C1 = -1470754296], V2 votes [C2 = -1273974290], V3 votes [C1 = -1470754296], V4 votes [C3 = 1846731908]} => {V1 votes [C1 = -1470754296], V2 votes [C2 = -1273974290], V3 votes [C1 = -1470754296], V4 votes [C2 = -1273974290]} => {V1 votes [C1 = -1470754296], V2 votes [C2 = -1273974290], V3 votes [C3 = 1846731908], V4 votes [C2 = -1273974290]} => {V1 votes [C1 = -1470754296], V2 votes [C2 = -1273974290], V3 votes [C3 = 1846731908], V4 votes [C3 = 1846731908]} => {V1 votes [C2 = -1273974290], V2 votes [C2 = -1273974290], V3 votes [C3 = 1846731908], V4 votes [C3 = 1846731908]}
---------------------------------

Truthful profiles:

1 - {V1 votes [C1 = -1470754296], V2 votes [C2 = -1273974290], V3 votes [C3 = 1846731908], V4 votes [C3 = 1846731908]}
Original social welfare: -3332338293 / 2 (-1666169146.5)
Randomized Dictatorship social welfare: -6841127951 / 4 (-1710281987.75)
Randomized Dictatorship social welfare ratio to optimum: 6841127951 / 6664676586 (1.0265)
Randomized Dictatorship social welfare ratio to best original NE: 6664676586 / 6841127951 (0.9742)

End Analysis of Plurality Voting Game 1.

{
    "numberOfGames" : 1,
    "avgNeCount" : 6,
    "percentageWithNe" : 100,
    "convergingPercentage" : 0,
    "avgPrices" : {
        "socialOptimum" : -1666169146.5,
        "priceOfSinking" : 1.0825,
        "priceOfAnarchy" : 1.0825,
        "priceOfStability" : 1
    },
    "betterPoSThanRandomizedDictatorshipPercentage" : 100
}
```
