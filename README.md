# Voting Games Analyzer
A command line tool for analyzing voting games, in particular, ones that use randomized voting rules.

## Custom Voting Rule Example
The following is an implementation of the plurality voting rule with random tie-breaking.
`PluralityVotingRule.java`:

```
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

After compiling, move the file `PluralityVotingRule.class` to the `lib` folder along with any required dependencies and
run `vga -r -vr PluralityVotingRule`

The output may looks something like this:
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
