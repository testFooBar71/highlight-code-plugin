package refactoringminer;

import cmd.CmdCommandRunner;
import com.google.gson.Gson;
import models.GitCommit;
import models.refactoringminer.RefactoringMinerOutput;

import java.io.IOException;

public class RefactoringMinerCmd {

    public RefactoringMinerOutput runRefactoringMiner(String projectPath, String commitSha) {
        String command = ".\\RefactoringMiner -c " + projectPath + " " + commitSha;
        String refactoringMinerPath = "C:\\refactoringMiner\\bin";
        RefactoringMinerOutput output = new RefactoringMinerOutput();
        try {
            String outputString = CmdCommandRunner.runCommand(command, refactoringMinerPath);
            Gson gson = new Gson();
            output = gson.fromJson(outputString, RefactoringMinerOutput.class);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return output;
    }

}
