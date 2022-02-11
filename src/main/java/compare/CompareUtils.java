package compare;

import at.aau.softwaredynamics.classifier.AbstractJavaChangeClassifier;
import at.aau.softwaredynamics.classifier.NonClassifyingClassifier;
import at.aau.softwaredynamics.classifier.entities.SourceCodeChange;
import at.aau.softwaredynamics.gen.NodeType;
import at.aau.softwaredynamics.gen.OptimizedJdtTreeGenerator;
import at.aau.softwaredynamics.matchers.JavaMatchers;
import at.aau.softwaredynamics.runner.util.ClassifierFactory;
import com.github.gumtreediff.gen.TreeGenerator;
import com.github.gumtreediff.matchers.Matcher;
import models.DiffRow;

import java.util.ArrayList;
import java.util.List;

public class CompareUtils {

    public static List<DiffRow> getDiffChanges(String previousVersion, String currentVersion) {
        List<SourceCodeChange> changes = getChangesBetweenVersions(previousVersion, currentVersion);
        return getDiff(changes);
    }

    public static List<SourceCodeChange> getSourceCodeChanges(String previousVersion, String currentVersion) {
        return getChangesBetweenVersions(previousVersion, currentVersion);
    }

    public static List<SourceCodeChange> getChangesBetweenVersions(String previousVersion, String latestVersion) {
        AbstractJavaChangeClassifier classifier = createClassifier();
        try {
            classifier.classify(previousVersion, latestVersion);
            return classifier.getCodeChanges();
        } catch (Exception e) {
            return null;
        }
    }

    private static List<DiffRow> getDiff(List<SourceCodeChange> changes) {
        List<DiffRow> diffs = new ArrayList<>();
        for (SourceCodeChange change : changes) {
            diffs.add(createDiffRow(change));
        }
        return diffs;
    }

    private static AbstractJavaChangeClassifier createClassifier() {
        Class<? extends AbstractJavaChangeClassifier> classifierType = NonClassifyingClassifier.class;
        Class<? extends Matcher> matcher = JavaMatchers.IterativeJavaMatcher_V2.class;
        TreeGenerator generator = new OptimizedJdtTreeGenerator();
        ClassifierFactory classifierFactory = new ClassifierFactory(classifierType, matcher, generator);
        return classifierFactory.createClassifier();
    }

    private static DiffRow createDiffRow(SourceCodeChange change) {
        return new DiffRow("NO_COMMIT",
                change.getNode().getLabel(),
                change.getAction().getName(),
                String.valueOf(NodeType.getEnum(change.getNodeType())),
                change.getSrcInfo().getStartLineNumber(),
                change.getSrcInfo().getEndLineNumber(),
                change.getDstInfo().getStartLineNumber(),
                change.getDstInfo().getEndLineNumber(),
                "dst");
    }
}
