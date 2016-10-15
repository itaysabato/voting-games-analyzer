package il.ac.huji.cs.itays04.cli.parsing;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import il.ac.huji.cs.itays04.voting.RandomizedVotingRule;

public class VotingRuleConverter implements IStringConverter<RandomizedVotingRule> {
    @Override
    public RandomizedVotingRule convert(String className) {
        final Class<?> rvrClass;
        try {
            rvrClass = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            throw new ParameterException("Voting rule class not found: " + className, e);
        }

        if (!RandomizedVotingRule.class.isAssignableFrom(rvrClass)) {
            throw new ParameterException("The voting rule class does not implement the RandomizedVotingRule interface: " + rvrClass);
        }

        try {
            return (RandomizedVotingRule) rvrClass.newInstance();
        }
        catch (Exception e) {
            throw new ParameterException("Could not instantiate voting rule: " + className, e);
        }
    }
}
