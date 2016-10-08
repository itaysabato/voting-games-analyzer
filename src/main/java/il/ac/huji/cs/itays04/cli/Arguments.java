package il.ac.huji.cs.itays04.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.common.base.MoreObjects;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.Collections;
import java.util.List;

@Parameters(separators = "=")
class Arguments {
    private static final String POS_DESC_PRE = "A comma-separated list of ";
    private static final String POS_DESC_POST = " positions, as rational numbers of the form '1', '-2', '3/2', etc.";

    @Parameter(
            names = {"-h", "--help"},
            description = "Display help message",
            help = true
    )
    private boolean help = false;

    @Parameter(
            names = {"-v", "--voters"},
            description = POS_DESC_PRE + "voter" + POS_DESC_POST,
            converter = BigFractionConverter.class
    )
    private List<BigFraction> voters = Collections.emptyList();

    @Parameter(
            names = {"-c", "--candidates"},
            description = POS_DESC_PRE + "candidate" + POS_DESC_POST,
            converter = BigFractionConverter.class
    )
    private List<BigFraction> candidates = Collections.emptyList();

    public boolean isHelp() {
        return help;
    }

    public List<BigFraction> getVoters() {
        return voters;
    }

    public List<BigFraction> getCandidates() {
        return candidates;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("help", help)
                .add("voters", voters)
                .add("candidates", candidates)
                .toString();
    }
}
