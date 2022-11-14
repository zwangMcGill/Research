package ca.mcgill.cs.tokenIdentification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PerlExecutor {
    private final List<String> commands = new ArrayList<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        PerlExecutor pe = new PerlExecutor();
        pe.execute();
    }

    public List<String> constructCommand(String inputPath) {
        commands.add("perl");
        commands.add("D:\\Research\\POSSE\\Scripts\\mainParser.pl");
        commands.add(inputPath);
        commands.add("'M'");
        return commands;
    }

    public void execute() throws IOException, InterruptedException {
        String command1 = "perl D:\\Research\\POSSE\\Scripts\\mainParser.pl D:\\Research\\POSSE\\Input\\test.input \"M\"";
        String command2 = "perl mainParser.pl ..\\Input\\test.input \"M\"";

        ProcessBuilder pb = new ProcessBuilder(
                "cmd.exe", "/c", "cd \"D:\\Research\\POSSE\\Scripts\" && " + command1);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();

        if (process.exitValue() == 0) {
            System.out.println("Command Successful");
            System.out.println(process);
        } else {
            System.out.println("Command Fails");
        }
    }
}
