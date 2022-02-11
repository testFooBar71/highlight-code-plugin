package difflogic;

import cmd.CmdCommandRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DiffGeneratorCmd implements IDiffGenerator {
    public boolean generateDiff() {
        String command = "java -jar \"C:\\Users\\Dell\\Documents\\UCB\\10mo Semestre\\Seminario de Grado\\pluginPoC\\libs\\at.aau.softwaredynamics.runner-1.0-SNAPSHOT-jar-with-dependencies.jar\" -src C:\\Users\\Dell\\IdeaProjects\\highlightTest\\src\\com\\company\\test_old.simple -dst C:\\Users\\Dell\\IdeaProjects\\highlightTest\\src\\com\\company\\test.simple -c None -m IJM -w FS -g OTG";
        String directory = "C:\\Users\\Dell\\Documents\\outputs";
        try {
            CmdCommandRunner.runCommand(command, directory);
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
