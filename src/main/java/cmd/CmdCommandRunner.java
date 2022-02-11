package cmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CmdCommandRunner {
    public static String runCommand(String command, String directory) throws IOException {
        final ProcessBuilder pBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        pBuilder.directory(new File(directory));
        try {
            final Process process = pBuilder.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString();
        } catch(IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
