package il.ac.huji.cs.itays04.cli;

import com.beust.jcommander.JCommander;

public class Main {
    public static void main(String[] args) {
        final Arguments arguments = new Arguments();
        final JCommander jCommander = new JCommander(arguments);
        jCommander.parse(args);

        if (arguments.isHelp()) {
            jCommander.setProgramName("vga");
            jCommander.usage();
        }
        else {
            System.out.println(arguments);
        }
    }
}
