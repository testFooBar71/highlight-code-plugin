package refactoringminer;

import actions.ActionsUtils;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import gr.uom.java.xmi.LocationInfo;
import gr.uom.java.xmi.UMLAnnotation;
import gr.uom.java.xmi.UMLClass;
import gr.uom.java.xmi.UMLOperation;
import gr.uom.java.xmi.decomposition.AbstractCodeFragment;
import gr.uom.java.xmi.decomposition.AbstractCodeMapping;
import gr.uom.java.xmi.decomposition.OperationInvocation;
import gr.uom.java.xmi.diff.*;
import kotlin.Pair;
import models.Data;
import models.DataFactory;
//import models.refactoringminer.Refactoring;
import models.JavaFileData;
import models.refactorings.PullUpAttribute;
import models.refactorings.PullUpMethod;
import models.refactorings.RefactoringData;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.refactoringminer.api.Refactoring;
import services.GlobalChangesService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class RefactoringMinerUtils {

    Project project;
    RevCommit commit;
    Document document;

    public RefactoringMinerUtils(Project project, RevCommit commit) {
        this.project = project;
        this.commit = commit;
    }

    public RefactoringMinerUtils(Project project, RevCommit commit, Document document) {
        this.project = project;
        this.commit = commit;
        this.document = document;
    }

    public void addAllRefactorings(List<Refactoring> refactorings) {
        for (Refactoring refactoring : refactorings) {
            if (refactoring instanceof ExtractOperationRefactoring) {
                addExtractOperation((ExtractOperationRefactoring) refactoring);
            } else if (refactoring instanceof RenameVariableRefactoring) {
                addRenameVariable((RenameVariableRefactoring) refactoring);
            } else if (refactoring instanceof RenameOperationRefactoring) {
                addRenameOperation((RenameOperationRefactoring) refactoring);
            } else if (refactoring instanceof RenameClassRefactoring) {
                addRenameClass((RenameClassRefactoring) refactoring);
            } else if (refactoring instanceof ChangeAttributeTypeRefactoring) {
                addChangeAttributeType((ChangeAttributeTypeRefactoring) refactoring);
            } else if (refactoring instanceof ChangeReturnTypeRefactoring) {
                addChangeReturnType((ChangeReturnTypeRefactoring) refactoring);
            } else if (refactoring instanceof ChangeVariableTypeRefactoring) {
                addChangeVariableType((ChangeVariableTypeRefactoring) refactoring);
            } else if (refactoring instanceof RemoveParameterRefactoring) {
                addRemoveParameter((RemoveParameterRefactoring) refactoring);
            } else if (refactoring instanceof AddParameterRefactoring) {
                addAddParameter((AddParameterRefactoring) refactoring);
            } else if (refactoring instanceof ReorderParameterRefactoring) {
                addReorderParameter((ReorderParameterRefactoring) refactoring);
            } else if (refactoring instanceof ExtractSuperclassRefactoring) {
                addExtractSuperclass((ExtractSuperclassRefactoring) refactoring);
            } else if (refactoring instanceof PullUpAttributeRefactoring) {
                addPullUpAttribute((PullUpAttributeRefactoring) refactoring);
            } else if (refactoring instanceof PullUpOperationRefactoring) {
                addPullUpOperation((PullUpOperationRefactoring) refactoring);
            } else if (refactoring instanceof PushDownAttributeRefactoring) {
                addPushDownAttribute((PushDownAttributeRefactoring) refactoring);
            } else if (refactoring instanceof PushDownOperationRefactoring) {
                addPushDownOperation((PushDownOperationRefactoring) refactoring);
            } else if (refactoring instanceof ExtractVariableRefactoring) {
                addExtractVariable((ExtractVariableRefactoring) refactoring);
            } else if (refactoring instanceof RenameAttributeRefactoring) {
                addRenameAttribute((RenameAttributeRefactoring) refactoring);
            }
        }
    }

    public void addRefactoringsToMap(List<Refactoring> refactorings, Map<Integer, List<Data>> actionsMap, String filePath) {
        for (Refactoring refactoring : refactorings) {
            if (refactoring instanceof ExtractOperationRefactoring) {
                handleExtractOperation(actionsMap, filePath, (ExtractOperationRefactoring) refactoring);
            } else if (refactoring instanceof RenameVariableRefactoring) {
                handleRenameVariable(actionsMap, filePath, (RenameVariableRefactoring) refactoring);
            } else if (refactoring instanceof RenameOperationRefactoring) {
                handleRenameOperation(actionsMap, filePath, (RenameOperationRefactoring) refactoring);
            } else if (refactoring instanceof RenameClassRefactoring) {
                handleRenameClass(actionsMap, filePath, (RenameClassRefactoring) refactoring);
            } else if (refactoring instanceof ChangeAttributeTypeRefactoring) {
                handleChangeAttributeType(actionsMap, filePath, (ChangeAttributeTypeRefactoring) refactoring);
            } else if (refactoring instanceof ChangeReturnTypeRefactoring) {
                handleChangeReturnType(actionsMap, filePath, (ChangeReturnTypeRefactoring) refactoring);
            } else if (refactoring instanceof ChangeVariableTypeRefactoring) {
                handleChangeVariableType(actionsMap, filePath, (ChangeVariableTypeRefactoring) refactoring);
            } else if (refactoring instanceof RemoveParameterRefactoring) {
                handleRemoveParameter(actionsMap, filePath, (RemoveParameterRefactoring) refactoring);
            } else if (refactoring instanceof AddParameterRefactoring) {
                handleAddParameter(actionsMap, filePath, (AddParameterRefactoring) refactoring);
            } else if (refactoring instanceof ReorderParameterRefactoring) {
                handleReorderParameter(actionsMap, filePath, (ReorderParameterRefactoring) refactoring);
            } else if (refactoring instanceof ExtractSuperclassRefactoring) {
                handleExtractSuperclass(actionsMap, filePath, (ExtractSuperclassRefactoring) refactoring);
            } else if (refactoring instanceof PullUpAttributeRefactoring) {
                handlePullUpAttribute(actionsMap, filePath, (PullUpAttributeRefactoring) refactoring);
            } else if (refactoring instanceof PullUpOperationRefactoring) {
                handlePullUpOperation(actionsMap, filePath, (PullUpOperationRefactoring) refactoring);
            } else if (refactoring instanceof PushDownAttributeRefactoring) {
                handlePushDownAttribute(actionsMap, filePath, (PushDownAttributeRefactoring) refactoring);
            } else if (refactoring instanceof PushDownOperationRefactoring) {
                handlePushDownOperation(actionsMap, filePath, (PushDownOperationRefactoring) refactoring);
            } else if (refactoring instanceof ExtractVariableRefactoring) {
                handleExtractVariable(actionsMap, filePath, (ExtractVariableRefactoring) refactoring);
            } else if (refactoring instanceof RenameAttributeRefactoring) {
                handleRenameAttribute(actionsMap, filePath, (RenameAttributeRefactoring) refactoring);
            }
        }
    }

    private int getLineForMethod(UMLOperation method, Document document) {
        int startLine = method.getLocationInfo().getStartLine();
        if (method.getJavadoc() != null) {
            int lines = method.getJavadoc().getLocationInfo().getEndLine() - method.getJavadoc().getLocationInfo().getStartLine() + 1;
            startLine += lines;
        }
        if (method.getAnnotations().size() > 0) {
            for (UMLAnnotation annotation : method.getAnnotations()) {
                int lines = annotation.getLocationInfo().getEndLine() - annotation.getLocationInfo().getStartLine() + 1;
                startLine += lines;
            }
        }
        while (true) {
            String lineContent = document.getText().split("\n")[startLine - 1];
            if (lineContent.contains(method.getName())) {
                break;
            } else {
                startLine++;
            }
        }
        return startLine;
    }

    private Pair<Integer, Integer> getOffsets(UMLOperation method, Document document) {
        int line = getLineForMethod(method, document);
        String methodName = method.getName();
        int startOffset = document.getLineStartOffset(line - 1);
        int relativeStartOffset = document.getText().split("\n")[line - 1].contains(methodName) ? document.getText().split("\n")[line - 1].indexOf(methodName) : 0;
        startOffset = startOffset + relativeStartOffset;
        int endOffset = relativeStartOffset != 0 ? startOffset + methodName.length() : document.getLineEndOffset(line - 1);
        return new Pair<>(startOffset, endOffset);
    }

    private void handleExtractOperation(Map<Integer, List<Data>> actionsMap, String filePath, ExtractOperationRefactoring extractOperationRefactoring) {
        List<String> codeExtractedFragments = new ArrayList<>();
        for (AbstractCodeFragment abstractCodeFragment : extractOperationRefactoring.getExtractedCodeFragmentsFromSourceOperation()) {
            codeExtractedFragments.add(abstractCodeFragment.getString());
        }
        if (extractOperationRefactoring.getExtractedOperation().getLocationInfo().getFilePath().equals(filePath)) {
            String startOffsetOfSourceOperation = String.valueOf(extractOperationRefactoring.getSourceOperationAfterExtraction().getLocationInfo().getStartOffset());
            String endOffsetOfSourceOperation = String.valueOf(extractOperationRefactoring.getSourceOperationAfterExtraction().getLocationInfo().getEndOffset());
            String startOffset = String.valueOf(extractOperationRefactoring.getExtractedOperation().getLocationInfo().getStartOffset());
            String endOffset = String.valueOf(extractOperationRefactoring.getExtractedOperation().getLocationInfo().getEndOffset());
            List<String> elements = new ArrayList<>(codeExtractedFragments);
            elements.add(0, endOffsetOfSourceOperation);
            elements.add(0, startOffsetOfSourceOperation);
            elements.add(0, endOffset);
            elements.add(0, startOffset);
            Data action = DataFactory.createRefactoringData("EXTRACTED_METHOD", elements.toArray(new String[0]));
            addActionData(action);
            ActionsUtils.addActionToLine(actionsMap, getLineForMethod(extractOperationRefactoring.getExtractedOperation(), document), action);
        }
        for (OperationInvocation call : extractOperationRefactoring.getExtractedOperationInvocations()) {
            if (call.getLocationInfo().getFilePath().equals(filePath)) {
                String startOffset = String.valueOf(call.getLocationInfo().getStartOffset());
                String endOffset = String.valueOf(call.getLocationInfo().getEndOffset());
                List<String> elements = new ArrayList<>(codeExtractedFragments);
                elements.add(0, endOffset);
                elements.add(0, startOffset);
                Data action = DataFactory.createRefactoringData("EXTRACTED_METHOD_CALL", elements.toArray(new String[0]));
                addActionData(action);
                ActionsUtils.addActionToLine(actionsMap, call.getLocationInfo().getStartLine(), action);
            }
        }
    }

    private void addExtractOperation(ExtractOperationRefactoring extractOperationRefactoring) {
        List<String> codeExtractedFragments = new ArrayList<>();
        for (AbstractCodeFragment abstractCodeFragment : extractOperationRefactoring.getExtractedCodeFragmentsFromSourceOperation()) {
            codeExtractedFragments.add(abstractCodeFragment.getString());
        }
        String startOffsetOfSourceOperation = String.valueOf(extractOperationRefactoring.getSourceOperationAfterExtraction().getLocationInfo().getStartOffset());
        String endOffsetOfSourceOperation = String.valueOf(extractOperationRefactoring.getSourceOperationAfterExtraction().getLocationInfo().getEndOffset());
        String startOffset = String.valueOf(extractOperationRefactoring.getExtractedOperation().getLocationInfo().getStartOffset());
        String endOffset = String.valueOf(extractOperationRefactoring.getExtractedOperation().getLocationInfo().getEndOffset());
        List<String> elements = new ArrayList<>(codeExtractedFragments);
        elements.add(0, endOffsetOfSourceOperation);
        elements.add(0, startOffsetOfSourceOperation);
        elements.add(0, endOffset);
        elements.add(0, startOffset);
        Data action = DataFactory.createRefactoringData("EXTRACTED_METHOD", elements.toArray(new String[0]));
        addActionData(action);
        String filePath = extractOperationRefactoring.getExtractedOperation().getLocationInfo().getFilePath();
        VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://" + project.getBasePath() + "/" + extractOperationRefactoring.getExtractedOperation().getLocationInfo().getFilePath());
        Document foundDocument = FileDocumentManager.getInstance().getDocument(virtualFile);
        int line = getLineForMethod(extractOperationRefactoring.getExtractedOperation(), foundDocument);
        project.getService(GlobalChangesService.class).addChange(filePath, line, action);
        for (OperationInvocation call : extractOperationRefactoring.getExtractedOperationInvocations()) {
            startOffset = String.valueOf(call.getLocationInfo().getStartOffset());
            endOffset = String.valueOf(call.getLocationInfo().getEndOffset());
            elements = new ArrayList<>(codeExtractedFragments);
            elements.add(0, endOffset);
            elements.add(0, startOffset);
            action = DataFactory.createRefactoringData("EXTRACTED_METHOD_CALL", elements.toArray(new String[0]));
            addActionData(action);
            filePath = call.getLocationInfo().getFilePath();
            line = call.getLocationInfo().getStartLine();
            project.getService(GlobalChangesService.class).addChange(filePath, line, action);
        }
    }

    private void handleRenameVariable(Map<Integer, List<Data>> actionsMap, String filePath, RenameVariableRefactoring renameVariableRefactoring) {
        if (renameVariableRefactoring.getRenamedVariable().getLocationInfo().getFilePath().equals(filePath)) {
            final String refactoringType = renameVariableRefactoring.getRenamedVariable().isParameter() ? "RENAME_PARAMETER" : "RENAME_VARIABLE";
            String startOffset = String.valueOf(renameVariableRefactoring.getRenamedVariable().getLocationInfo().getStartOffset());
            int variableNameLength = renameVariableRefactoring.getRenamedVariable().getVariableName().length();
            String endOffset = refactoringType.equals("RENAME_VARIABLE")
                    ? String.valueOf(renameVariableRefactoring.getRenamedVariable().getLocationInfo().getStartOffset() + variableNameLength)
                    : String.valueOf(renameVariableRefactoring.getRenamedVariable().getLocationInfo().getEndOffset());
            String[] attributes = refactoringType.equals("RENAME_PARAMETER") ?
                    new String[]{
                            renameVariableRefactoring.getOriginalVariable().getType().getClassType(),
                            renameVariableRefactoring.getOriginalVariable().getVariableName(),
                            startOffset,
                            endOffset
                    }
                    : new String[]{renameVariableRefactoring.getOriginalVariable().getVariableName(), startOffset, endOffset};
            Data action = DataFactory.createRefactoringData(refactoringType, attributes);
            addActionData(action);
            ActionsUtils.addActionToLine(actionsMap, renameVariableRefactoring.getRenamedVariable().getLocationInfo().getStartLine(), action);
        }
    }

    private void addRenameVariable(RenameVariableRefactoring renameVariableRefactoring) {
        final String refactoringType = renameVariableRefactoring.getRenamedVariable().isParameter() ? "RENAME_PARAMETER" : "RENAME_VARIABLE";
        String startOffset = String.valueOf(renameVariableRefactoring.getRenamedVariable().getLocationInfo().getStartOffset());
        int variableNameLength = renameVariableRefactoring.getRenamedVariable().getVariableName().length();
        String endOffset = refactoringType.equals("RENAME_VARIABLE")
                ? String.valueOf(renameVariableRefactoring.getRenamedVariable().getLocationInfo().getStartOffset() + variableNameLength)
                : String.valueOf(renameVariableRefactoring.getRenamedVariable().getLocationInfo().getEndOffset());
        String[] attributes = refactoringType.equals("RENAME_PARAMETER") ?
                new String[]{
                        renameVariableRefactoring.getOriginalVariable().getType().getClassType(),
                        renameVariableRefactoring.getOriginalVariable().getVariableName(),
                        startOffset,
                        endOffset
                }
                : new String[]{renameVariableRefactoring.getOriginalVariable().getVariableName(), startOffset, endOffset};
        Data action = DataFactory.createRefactoringData(refactoringType, attributes);
        addActionData(action);
        String filePath = renameVariableRefactoring.getRenamedVariable().getLocationInfo().getFilePath();
        int line = renameVariableRefactoring.getRenamedVariable().getLocationInfo().getStartLine();
        project.getService(GlobalChangesService.class).addChange(filePath, line, action);
    }

    private void handleRenameOperation(Map<Integer, List<Data>> actionsMap, String filePath, RenameOperationRefactoring renameOperationRefactoring) {
        if (renameOperationRefactoring.getRenamedOperation().getLocationInfo().getFilePath().equals(filePath)) {
//            String startOffset = String.valueOf(renameOperationRefactoring.getRenamedOperation().getLocationInfo().getStartOffset());
//            String endOffset = String.valueOf(renameOperationRefactoring.getRenamedOperation().getLocationInfo().getEndOffset());
            Pair<Integer, Integer> offsets = getOffsets(renameOperationRefactoring.getRenamedOperation(), document);
//            Data action = DataFactory.createRefactoringData("RENAME_METHOD", renameOperationRefactoring.getOriginalOperation().getName(), startOffset, endOffset);
            Data action = DataFactory.createRefactoringData("RENAME_METHOD", renameOperationRefactoring.getOriginalOperation().getName(), String.valueOf(offsets.getFirst()), String.valueOf(offsets.getSecond()));
            addActionData(action);
            ActionsUtils.addActionToLine(actionsMap, getLineForMethod(renameOperationRefactoring.getRenamedOperation(), document), action);
        }
    }

    private void addRenameOperation(RenameOperationRefactoring renameOperationRefactoring) {
//        String startOffset = String.valueOf(renameOperationRefactoring.getRenamedOperation().getLocationInfo().getStartOffset());
//        String endOffset = String.valueOf(renameOperationRefactoring.getRenamedOperation().getLocationInfo().getEndOffset());
        VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://" + project.getBasePath() + "/" + renameOperationRefactoring.getRenamedOperation().getLocationInfo().getFilePath());
        Document foundDocument = FileDocumentManager.getInstance().getDocument(virtualFile);
        Pair<Integer, Integer> offsets = getOffsets(renameOperationRefactoring.getRenamedOperation(), foundDocument);
//        Data action = DataFactory.createRefactoringData("RENAME_METHOD", renameOperationRefactoring.getOriginalOperation().getName(), startOffset, endOffset);
        Data action = DataFactory.createRefactoringData("RENAME_METHOD", renameOperationRefactoring.getOriginalOperation().getName(), String.valueOf(offsets.getFirst()), String.valueOf(offsets.getSecond()));
        addActionData(action);
        String filePath = renameOperationRefactoring.getRenamedOperation().getLocationInfo().getFilePath();
        int line = getLineForMethod(renameOperationRefactoring.getRenamedOperation(), foundDocument);
        project.getService(GlobalChangesService.class).addChange(filePath, line, action);
    }

    private void handleRenameClass(Map<Integer, List<Data>> actionsMap, String filePath, RenameClassRefactoring renameClassRefactoring) {
        if (renameClassRefactoring.getRenamedClass().getLocationInfo().getFilePath().equals(filePath)) {
            String[] classParts = renameClassRefactoring.getOriginalClassName().split("\\.");
            String startOffset = String.valueOf(renameClassRefactoring.getRenamedClass().getLocationInfo().getStartOffset());
            String endOffset = String.valueOf(renameClassRefactoring.getRenamedClass().getLocationInfo().getEndOffset());
            Data action = DataFactory.createRefactoringData("RENAME_CLASS", classParts[classParts.length - 1], startOffset, endOffset);
            addActionData(action);
            ActionsUtils.addActionToLine(actionsMap, renameClassRefactoring.getRenamedClass().getLocationInfo().getStartLine(), action);
        }
    }

    private void addRenameClass(RenameClassRefactoring renameClassRefactoring) {
        String[] classParts = renameClassRefactoring.getOriginalClassName().split("\\.");
        String startOffset = String.valueOf(renameClassRefactoring.getRenamedClass().getLocationInfo().getStartOffset());
        String endOffset = String.valueOf(renameClassRefactoring.getRenamedClass().getLocationInfo().getEndOffset());
        Data action = DataFactory.createRefactoringData("RENAME_CLASS", classParts[classParts.length - 1], startOffset, endOffset);
        addActionData(action);
        String filePath = renameClassRefactoring.getRenamedClass().getLocationInfo().getFilePath();
        int line = renameClassRefactoring.getRenamedClass().getLocationInfo().getStartLine();
        project.getService(GlobalChangesService.class).addChange(filePath, line, action);
    }

    private void handleChangeAttributeType(Map<Integer, List<Data>> actionsMap, String filePath, ChangeAttributeTypeRefactoring changeAttributeTypeRefactoring) {
        if (changeAttributeTypeRefactoring.getChangedTypeAttribute().getLocationInfo().getFilePath().equals(filePath)) {
            String startOffset = String.valueOf(changeAttributeTypeRefactoring.getChangedTypeAttribute().getType().getLocationInfo().getStartOffset());
            String endOffset = String.valueOf(changeAttributeTypeRefactoring.getChangedTypeAttribute().getType().getLocationInfo().getEndOffset());
            Data action = DataFactory.createRefactoringData("CHANGE_ATTRIBUTE_TYPE", changeAttributeTypeRefactoring.getOriginalAttribute().getType().getClassType(), startOffset, endOffset);
            addActionData(action);
            ActionsUtils.addActionToLine(actionsMap, changeAttributeTypeRefactoring.getChangedTypeAttribute().getLocationInfo().getStartLine(), action);
        }
    }

    private void addChangeAttributeType(ChangeAttributeTypeRefactoring changeAttributeTypeRefactoring) {
        String startOffset = String.valueOf(changeAttributeTypeRefactoring.getChangedTypeAttribute().getType().getLocationInfo().getStartOffset());
        String endOffset = String.valueOf(changeAttributeTypeRefactoring.getChangedTypeAttribute().getType().getLocationInfo().getEndOffset());
        Data action = DataFactory.createRefactoringData("CHANGE_ATTRIBUTE_TYPE", changeAttributeTypeRefactoring.getOriginalAttribute().getType().getClassType(), startOffset, endOffset);
        addActionData(action);
        String filePath = changeAttributeTypeRefactoring.getChangedTypeAttribute().getLocationInfo().getFilePath();
        int line = changeAttributeTypeRefactoring.getChangedTypeAttribute().getLocationInfo().getStartLine();
        project.getService(GlobalChangesService.class).addChange(filePath, line, action);
    }

    private void handleChangeReturnType(Map<Integer, List<Data>> actionsMap, String filePath, ChangeReturnTypeRefactoring changeReturnTypeRefactoring) {
        if (changeReturnTypeRefactoring.getChangedType().getLocationInfo().getFilePath().equals(filePath)) {
            Data action = DataFactory.createRefactoringData("CHANGE_RETURN_TYPE", changeReturnTypeRefactoring.getOriginalType().getClassType(), String.valueOf(changeReturnTypeRefactoring.getChangedType().getLocationInfo().getStartOffset()), String.valueOf(changeReturnTypeRefactoring.getChangedType().getLocationInfo().getEndOffset()));
            addActionData(action);
            ActionsUtils.addActionToLine(actionsMap, changeReturnTypeRefactoring.getChangedType().getLocationInfo().getStartLine(), action);
        }
    }

    private void addChangeReturnType(ChangeReturnTypeRefactoring changeReturnTypeRefactoring) {
        Data action = DataFactory.createRefactoringData("CHANGE_RETURN_TYPE", changeReturnTypeRefactoring.getOriginalType().getClassType(), String.valueOf(changeReturnTypeRefactoring.getChangedType().getLocationInfo().getStartOffset()), String.valueOf(changeReturnTypeRefactoring.getChangedType().getLocationInfo().getEndOffset()));
        addActionData(action);
        String filePath = changeReturnTypeRefactoring.getChangedType().getLocationInfo().getFilePath();
        int line = changeReturnTypeRefactoring.getChangedType().getLocationInfo().getStartLine();
        project.getService(GlobalChangesService.class).addChange(filePath, line, action);
    }

    private void handleChangeVariableType(Map<Integer, List<Data>> actionsMap, String filePath, ChangeVariableTypeRefactoring changeVariableTypeRefactoring) {
        if (changeVariableTypeRefactoring.getChangedTypeVariable().getLocationInfo().getFilePath().equals(filePath)) {
            final String refactoringType = changeVariableTypeRefactoring.getChangedTypeVariable().isParameter() ? "CHANGE_PARAMETER_TYPE" : "CHANGE_VARIABLE_TYPE";
            String startOffset = String.valueOf(changeVariableTypeRefactoring.getChangedTypeVariable().getLocationInfo().getStartOffset());
            String endOffset = String.valueOf(changeVariableTypeRefactoring.getChangedTypeVariable().getLocationInfo().getEndOffset());
            String[] attributes = refactoringType.equals("CHANGE_PARAMETER_TYPE") ?
                    new String[]{changeVariableTypeRefactoring.getChangedTypeVariable().getVariableName(), changeVariableTypeRefactoring.getOriginalVariable().getType().getClassType(), startOffset, endOffset}
                    : new String[]{changeVariableTypeRefactoring.getOriginalVariable().getType().getClassType(), startOffset, endOffset};
            Data action = DataFactory.createRefactoringData(refactoringType, attributes);
            addActionData(action);
            ActionsUtils.addActionToLine(actionsMap, changeVariableTypeRefactoring.getChangedTypeVariable().getLocationInfo().getStartLine(), action);
        }
    }

    private void addChangeVariableType(ChangeVariableTypeRefactoring changeVariableTypeRefactoring) {
        final String refactoringType = changeVariableTypeRefactoring.getChangedTypeVariable().isParameter() ? "CHANGE_PARAMETER_TYPE" : "CHANGE_VARIABLE_TYPE";
        String startOffset = String.valueOf(changeVariableTypeRefactoring.getChangedTypeVariable().getLocationInfo().getStartOffset());
        String endOffset = String.valueOf(changeVariableTypeRefactoring.getChangedTypeVariable().getLocationInfo().getEndOffset());
        String[] attributes = refactoringType.equals("CHANGE_PARAMETER_TYPE") ?
                new String[]{changeVariableTypeRefactoring.getChangedTypeVariable().getVariableName(), changeVariableTypeRefactoring.getOriginalVariable().getType().getClassType(), startOffset, endOffset}
                : new String[]{changeVariableTypeRefactoring.getOriginalVariable().getType().getClassType(), startOffset, endOffset};
        Data action = DataFactory.createRefactoringData(refactoringType, attributes);
        addActionData(action);
        String filePath = changeVariableTypeRefactoring.getChangedTypeVariable().getLocationInfo().getFilePath();
        int line = changeVariableTypeRefactoring.getChangedTypeVariable().getLocationInfo().getStartLine();
        project.getService(GlobalChangesService.class).addChange(filePath, line, action);
    }

    private void handleRemoveParameter(Map<Integer, List<Data>> actionsMap, String filePath, RemoveParameterRefactoring removeParameterRefactoring) {
        if (removeParameterRefactoring.getOperationAfter().getLocationInfo().getFilePath().equals(filePath)) {
            String startOffset = String.valueOf(removeParameterRefactoring.getParameter().getVariableDeclaration().getLocationInfo().getStartOffset());
            String endOffset = String.valueOf(removeParameterRefactoring.getParameter().getVariableDeclaration().getLocationInfo().getEndOffset());
            Data action = DataFactory.createRefactoringData("REMOVE_PARAMETER", removeParameterRefactoring.getParameter().getType().getClassType(), removeParameterRefactoring.getParameter().getName(), startOffset, endOffset);
            addActionData(action);
            ActionsUtils.addActionToLine(actionsMap, removeParameterRefactoring.getOperationAfter().getLocationInfo().getStartLine(), action);
        }
    }

    private void addRemoveParameter(RemoveParameterRefactoring removeParameterRefactoring) {
        String startOffset = String.valueOf(removeParameterRefactoring.getParameter().getVariableDeclaration().getLocationInfo().getStartOffset());
        String endOffset = String.valueOf(removeParameterRefactoring.getParameter().getVariableDeclaration().getLocationInfo().getEndOffset());
        Data action = DataFactory.createRefactoringData("REMOVE_PARAMETER", removeParameterRefactoring.getParameter().getType().getClassType(), removeParameterRefactoring.getParameter().getName(), startOffset, endOffset);
        addActionData(action);
        String filePath = removeParameterRefactoring.getOperationAfter().getLocationInfo().getFilePath();
        int line = removeParameterRefactoring.getOperationAfter().getLocationInfo().getStartLine();
        project.getService(GlobalChangesService.class).addChange(filePath, line, action);
    }

    private void handleAddParameter(Map<Integer, List<Data>> actionsMap, String filePath, AddParameterRefactoring addParameterRefactoring) {
        if (addParameterRefactoring.getOperationAfter().getLocationInfo().getFilePath().equals(filePath)) {
            String startOffset = String.valueOf(addParameterRefactoring.getParameter().getVariableDeclaration().getLocationInfo().getStartOffset());
            String endOffset = String.valueOf(addParameterRefactoring.getParameter().getVariableDeclaration().getLocationInfo().getEndOffset());
            Data action = DataFactory.createRefactoringData("ADD_PARAMETER", addParameterRefactoring.getParameter().getType().getClassType(), addParameterRefactoring.getParameter().getName(), startOffset, endOffset);
            addActionData(action);
            ActionsUtils.addActionToLine(actionsMap, addParameterRefactoring.getParameter().getVariableDeclaration().getLocationInfo().getStartLine(), action);
        }
    }

    private void addAddParameter(AddParameterRefactoring addParameterRefactoring) {
        String startOffset = String.valueOf(addParameterRefactoring.getParameter().getVariableDeclaration().getLocationInfo().getStartOffset());
        String endOffset = String.valueOf(addParameterRefactoring.getParameter().getVariableDeclaration().getLocationInfo().getEndOffset());
        Data action = DataFactory.createRefactoringData("ADD_PARAMETER", addParameterRefactoring.getParameter().getType().getClassType(), addParameterRefactoring.getParameter().getName(), startOffset, endOffset);
        addActionData(action);
        String filePath = addParameterRefactoring.getOperationAfter().getLocationInfo().getFilePath();
        int line = addParameterRefactoring.getParameter().getVariableDeclaration().getLocationInfo().getStartLine();
        project.getService(GlobalChangesService.class).addChange(filePath, line, action);
    }

    private void handleReorderParameter(Map<Integer, List<Data>> actionsMap, String filePath, ReorderParameterRefactoring reorderParameterRefactoring) {
        if (reorderParameterRefactoring.getOperationAfter().getLocationInfo().getFilePath().equals(filePath)) {
            List<String> oldParametersOrder = reorderParameterRefactoring.getParametersBefore().stream().map(parameter -> parameter.getType().getClassType() + " " + parameter.getVariableName()).collect(Collectors.toList());
            String startOffset = String.valueOf(reorderParameterRefactoring.getParametersAfter().get(0).getLocationInfo().getStartOffset());
            String endOffset = String.valueOf(reorderParameterRefactoring.getParametersAfter().get(reorderParameterRefactoring.getParametersAfter().size() - 1).getLocationInfo().getEndOffset());
            List<String> allParameters = oldParametersOrder;
            allParameters.add(0, endOffset);
            allParameters.add(0, startOffset);
            Data action = DataFactory.createRefactoringData("REORDER_PARAMETER", allParameters.toArray(new String[allParameters.size()]));
            addActionData(action);
            ActionsUtils.addActionToLine(actionsMap, reorderParameterRefactoring.getOperationAfter().getLocationInfo().getStartLine(), action);
        }
    }

    private void addReorderParameter(ReorderParameterRefactoring reorderParameterRefactoring) {
        List<String> oldParametersOrder = reorderParameterRefactoring.getParametersBefore().stream().map(parameter -> parameter.getType().getClassType() + " " + parameter.getVariableName()).collect(Collectors.toList());
        String startOffset = String.valueOf(reorderParameterRefactoring.getParametersAfter().get(0).getLocationInfo().getStartOffset());
        String endOffset = String.valueOf(reorderParameterRefactoring.getParametersAfter().get(reorderParameterRefactoring.getParametersAfter().size() - 1).getLocationInfo().getEndOffset());
        List<String> allParameters = oldParametersOrder;
        allParameters.add(0, endOffset);
        allParameters.add(0, startOffset);
        Data action = DataFactory.createRefactoringData("REORDER_PARAMETER", allParameters.toArray(new String[allParameters.size()]));
        addActionData(action);
        String filePath = reorderParameterRefactoring.getOperationAfter().getLocationInfo().getFilePath();
        int line = reorderParameterRefactoring.getOperationAfter().getLocationInfo().getStartLine();
        project.getService(GlobalChangesService.class).addChange(filePath, line, action);
    }

    private void handleExtractSuperclass(Map<Integer, List<Data>> actionsMap, String filePath, ExtractSuperclassRefactoring extractSuperclassRefactoring) {
        final String refactoringType = extractSuperclassRefactoring.getExtractedClass().isInterface() ? "EXTRACT_INTERFACE" : "EXTRACT_SUPERCLASS";
        List<String> subclasses = new ArrayList<>(extractSuperclassRefactoring.getSubclassSet());
        List<String> attributes = new ArrayList<>(subclasses);
        attributes.add(0, String.valueOf(document.getLineEndOffset(extractSuperclassRefactoring.getExtractedClass().getLocationInfo().getStartLine() - 1)));
        attributes.add(0, String.valueOf(document.getLineStartOffset(extractSuperclassRefactoring.getExtractedClass().getLocationInfo().getStartLine() - 1)));
        Data actionForExtractedClass = DataFactory.createRefactoringData(refactoringType, attributes.toArray(new String[attributes.size()]));
        addActionData(actionForExtractedClass);
        for (UMLClass umlClass : extractSuperclassRefactoring.getUMLSubclassSet()) {
//            String startOffset = String.valueOf(umlClass.getLocationInfo().getStartOffset());
            String startOffset = "-1";
//            String endOffset = String.valueOf(umlClass.getLocationInfo().getEndOffset());
            String endOffset = "-1";
            List<String> subclassAttributes = new ArrayList<>(subclasses);
            subclassAttributes.add(0, endOffset);
            subclassAttributes.add(0, startOffset);
            Data actionForChildClass = DataFactory.createRefactoringData(refactoringType, subclassAttributes.toArray(new String[subclassAttributes.size()]));
            addActionData(actionForChildClass);
            if (umlClass.getLocationInfo().getFilePath().equals(filePath)) {
                ActionsUtils.addActionToLine(actionsMap, umlClass.getLocationInfo().getStartLine(), actionForChildClass);
            }
        }
        LocationInfo extractedClassLocation = extractSuperclassRefactoring.getExtractedClass().getLocationInfo();
        if (extractedClassLocation.getFilePath().equals(filePath)) {
            ActionsUtils.addActionToLine(actionsMap, extractedClassLocation.getStartLine(), actionForExtractedClass);
        }
    }

    private void addExtractSuperclass(ExtractSuperclassRefactoring extractSuperclassRefactoring) {
        final String refactoringType = extractSuperclassRefactoring.getExtractedClass().isInterface() ? "EXTRACT_INTERFACE" : "EXTRACT_SUPERCLASS";
        List<String> subclasses = new ArrayList<>(extractSuperclassRefactoring.getSubclassSet());
        List<String> attributes = new ArrayList<>(subclasses);
        VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://" + project.getBasePath() + "/" + extractSuperclassRefactoring.getExtractedClass().getLocationInfo().getFilePath());
        Document foundDocument = FileDocumentManager.getInstance().getDocument(virtualFile);
        attributes.add(0, String.valueOf(foundDocument.getLineEndOffset(extractSuperclassRefactoring.getExtractedClass().getLocationInfo().getStartLine() - 1)));
        attributes.add(0, String.valueOf(foundDocument.getLineStartOffset(extractSuperclassRefactoring.getExtractedClass().getLocationInfo().getStartLine() - 1)));
        Data actionForExtractedClass = DataFactory.createRefactoringData(refactoringType, attributes.toArray(new String[attributes.size()]));
        addActionData(actionForExtractedClass);
        for (UMLClass umlClass : extractSuperclassRefactoring.getUMLSubclassSet()) {
//            String startOffset = String.valueOf(umlClass.getLocationInfo().getStartOffset());
            String startOffset = "-1";
//            String endOffset = String.valueOf(umlClass.getLocationInfo().getEndOffset());
            String endOffset = "-1";
            List<String> subclassAttributes = new ArrayList<>(subclasses);
            subclassAttributes.add(0, endOffset);
            subclassAttributes.add(0, startOffset);
            Data actionForChildClass = DataFactory.createRefactoringData(refactoringType, subclassAttributes.toArray(new String[subclassAttributes.size()]));
            addActionData(actionForChildClass);
            String filePath = umlClass.getLocationInfo().getFilePath();
            int line = umlClass.getLocationInfo().getStartLine();
            project.getService(GlobalChangesService.class).addChange(filePath, line, actionForChildClass);
        }
        LocationInfo extractedClassLocation = extractSuperclassRefactoring.getExtractedClass().getLocationInfo();
        String filePath = extractedClassLocation.getFilePath();
        int line = extractedClassLocation.getStartLine();
        project.getService(GlobalChangesService.class).addChange(filePath, line, actionForExtractedClass);
    }

    private void handlePullUpAttribute(Map<Integer, List<Data>> actionsMap, String filePath, PullUpAttributeRefactoring pullUpAttributeRefactoring) {
        if (pullUpAttributeRefactoring.getMovedAttribute().getLocationInfo().getFilePath().equals(filePath)) {
//            String startOffset = String.valueOf(pullUpAttributeRefactoring.getMovedAttribute().getLocationInfo().getStartOffset());
            String startOffset = String.valueOf(document.getLineStartOffset(pullUpAttributeRefactoring.getMovedAttribute().getLocationInfo().getStartLine() - 1));
//            String endOffset = String.valueOf(pullUpAttributeRefactoring.getMovedAttribute().getLocationInfo().getEndOffset());
            String endOffset = String.valueOf(document.getLineEndOffset(pullUpAttributeRefactoring.getMovedAttribute().getLocationInfo().getStartLine() - 1));
            Data action = DataFactory.createRefactoringData("PULL_UP_ATTRIBUTE", pullUpAttributeRefactoring.getMovedAttribute().getClassName(), startOffset, endOffset, pullUpAttributeRefactoring.getOriginalAttribute().getClassName());
            addActionData(action);
            ActionsUtils.addPullUpAttribute(actionsMap, pullUpAttributeRefactoring.getMovedAttribute().getLocationInfo().getStartLine(), (PullUpAttribute) action);
        }
    }

    private void addPullUpAttribute(PullUpAttributeRefactoring pullUpAttributeRefactoring) {
//            String startOffset = String.valueOf(pullUpAttributeRefactoring.getMovedAttribute().getLocationInfo().getStartOffset());
        VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://" + project.getBasePath() + "/" + pullUpAttributeRefactoring.getMovedAttribute().getLocationInfo().getFilePath());
        Document foundDocument = FileDocumentManager.getInstance().getDocument(virtualFile);
        String startOffset = String.valueOf(foundDocument.getLineStartOffset(pullUpAttributeRefactoring.getMovedAttribute().getLocationInfo().getStartLine() - 1));
//            String endOffset = String.valueOf(pullUpAttributeRefactoring.getMovedAttribute().getLocationInfo().getEndOffset());
        String endOffset = String.valueOf(foundDocument.getLineEndOffset(pullUpAttributeRefactoring.getMovedAttribute().getLocationInfo().getStartLine() - 1));
        Data action = DataFactory.createRefactoringData("PULL_UP_ATTRIBUTE", pullUpAttributeRefactoring.getMovedAttribute().getClassName(), startOffset, endOffset, pullUpAttributeRefactoring.getOriginalAttribute().getClassName());
        addActionData(action);
        String filePath = pullUpAttributeRefactoring.getMovedAttribute().getLocationInfo().getFilePath();
        int line = pullUpAttributeRefactoring.getMovedAttribute().getLocationInfo().getStartLine();
        project.getService(GlobalChangesService.class).addChange(filePath, line, action);
    }

    private void handlePullUpOperation(Map<Integer, List<Data>> actionsMap, String filePath, PullUpOperationRefactoring pullUpOperationRefactoring) {
        if (pullUpOperationRefactoring.getMovedOperation().getLocationInfo().getFilePath().equals(filePath)) {
            String startOffset = String.valueOf(pullUpOperationRefactoring.getMovedOperation().getLocationInfo().getStartOffset());
            String endOffset = String.valueOf(pullUpOperationRefactoring.getMovedOperation().getLocationInfo().getEndOffset());
            Data action = DataFactory.createRefactoringData("PULL_UP_METHOD", pullUpOperationRefactoring.getMovedOperation().getClassName(), startOffset, endOffset, pullUpOperationRefactoring.getOriginalOperation().getClassName());
            addActionData(action);
            ActionsUtils.addPullUpMethod(actionsMap, pullUpOperationRefactoring.getMovedOperation().getLocationInfo().getStartLine(), (PullUpMethod) action);
        }
    }

    private void addPullUpOperation(PullUpOperationRefactoring pullUpOperationRefactoring) {
        String startOffset = String.valueOf(pullUpOperationRefactoring.getMovedOperation().getLocationInfo().getStartOffset());
        String endOffset = String.valueOf(pullUpOperationRefactoring.getMovedOperation().getLocationInfo().getEndOffset());
        Data action = DataFactory.createRefactoringData("PULL_UP_METHOD", pullUpOperationRefactoring.getMovedOperation().getClassName(), startOffset, endOffset, pullUpOperationRefactoring.getOriginalOperation().getClassName());
        addActionData(action);
        String filePath = pullUpOperationRefactoring.getMovedOperation().getLocationInfo().getFilePath();
        int line = pullUpOperationRefactoring.getMovedOperation().getLocationInfo().getStartLine();
        project.getService(GlobalChangesService.class).addChange(filePath, line, action);
    }

    private void handlePushDownAttribute(Map<Integer, List<Data>> actionsMap, String filePath, PushDownAttributeRefactoring pushDownAttributeRefactoring) {
        if (pushDownAttributeRefactoring.getMovedAttribute().getLocationInfo().getFilePath().equals(filePath)) {
            String url = "file://" + project.getBasePath() + "/" + pushDownAttributeRefactoring.getOriginalAttribute().getLocationInfo().getFilePath();
//            String startOffset = String.valueOf(pushDownAttributeRefactoring.getMovedAttribute().getLocationInfo().getStartOffset());
            String startOffset = String.valueOf(document.getLineStartOffset(pushDownAttributeRefactoring.getMovedAttribute().getLocationInfo().getStartLine() - 1));
//            String endOffset = String.valueOf(pushDownAttributeRefactoring.getMovedAttribute().getLocationInfo().getEndOffset());
            String endOffset = String.valueOf(document.getLineEndOffset(pushDownAttributeRefactoring.getMovedAttribute().getLocationInfo().getStartLine() - 1));
            Data action = DataFactory.createRefactoringData("PUSH_DOWN_ATTRIBUTE", pushDownAttributeRefactoring.getOriginalAttribute().getClassName(), url, startOffset, endOffset);
            addActionData(action);
            ActionsUtils.addActionToLine(actionsMap, pushDownAttributeRefactoring.getMovedAttribute().getLocationInfo().getStartLine(), action);
        }
    }

    private void addPushDownAttribute(PushDownAttributeRefactoring pushDownAttributeRefactoring) {
        String url = "file://" + project.getBasePath() + "/" + pushDownAttributeRefactoring.getOriginalAttribute().getLocationInfo().getFilePath();
//            String startOffset = String.valueOf(pushDownAttributeRefactoring.getMovedAttribute().getLocationInfo().getStartOffset());
        VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://" + project.getBasePath() + "/" + pushDownAttributeRefactoring.getMovedAttribute().getLocationInfo().getFilePath());
        Document foundDocument = FileDocumentManager.getInstance().getDocument(virtualFile);
        String startOffset = String.valueOf(foundDocument.getLineStartOffset(pushDownAttributeRefactoring.getMovedAttribute().getLocationInfo().getStartLine() - 1));
//            String endOffset = String.valueOf(pushDownAttributeRefactoring.getMovedAttribute().getLocationInfo().getEndOffset());
        String endOffset = String.valueOf(foundDocument.getLineEndOffset(pushDownAttributeRefactoring.getMovedAttribute().getLocationInfo().getStartLine() - 1));
        Data action = DataFactory.createRefactoringData("PUSH_DOWN_ATTRIBUTE", pushDownAttributeRefactoring.getOriginalAttribute().getClassName(), url, startOffset, endOffset);
        addActionData(action);
        String filePath = pushDownAttributeRefactoring.getMovedAttribute().getLocationInfo().getFilePath();
        int line = pushDownAttributeRefactoring.getMovedAttribute().getLocationInfo().getStartLine();
        project.getService(GlobalChangesService.class).addChange(filePath, line, action);
    }

    private void handlePushDownOperation(Map<Integer, List<Data>> actionsMap, String filePath, PushDownOperationRefactoring pushDownOperationRefactoring) {
        if (pushDownOperationRefactoring.getMovedOperation().getLocationInfo().getFilePath().equals(filePath)) {
            String url = "file://" + project.getBasePath() + "/" + pushDownOperationRefactoring.getOriginalOperation().getLocationInfo().getFilePath();
            String startOffset = String.valueOf(pushDownOperationRefactoring.getMovedOperation().getLocationInfo().getStartOffset());
            String endOffset = String.valueOf(pushDownOperationRefactoring.getMovedOperation().getLocationInfo().getEndOffset());
            Data action = DataFactory.createRefactoringData("PUSH_DOWN_METHOD", pushDownOperationRefactoring.getOriginalOperation().getClassName(), url, startOffset, endOffset);
            addActionData(action);
            ActionsUtils.addActionToLine(actionsMap, pushDownOperationRefactoring.getMovedOperation().getLocationInfo().getStartLine(), action);
        }
    }

    private void addPushDownOperation(PushDownOperationRefactoring pushDownOperationRefactoring) {
        String url = "file://" + project.getBasePath() + "/" + pushDownOperationRefactoring.getOriginalOperation().getLocationInfo().getFilePath();
        String startOffset = String.valueOf(pushDownOperationRefactoring.getMovedOperation().getLocationInfo().getStartOffset());
        String endOffset = String.valueOf(pushDownOperationRefactoring.getMovedOperation().getLocationInfo().getEndOffset());
        Data action = DataFactory.createRefactoringData("PUSH_DOWN_METHOD", pushDownOperationRefactoring.getOriginalOperation().getClassName(), url, startOffset, endOffset);
        addActionData(action);
        String filePath = pushDownOperationRefactoring.getMovedOperation().getLocationInfo().getFilePath();
        int line = pushDownOperationRefactoring.getMovedOperation().getLocationInfo().getStartLine();
        project.getService(GlobalChangesService.class).addChange(filePath, line, action);
    }

    private void handleExtractVariable(Map<Integer, List<Data>> actionsMap, String filePath, ExtractVariableRefactoring extractVariableRefactoring) {
        if (extractVariableRefactoring.getVariableDeclaration().getLocationInfo().getFilePath().equals(filePath)) {
            String startOffset = String.valueOf(extractVariableRefactoring.getVariableDeclaration().getLocationInfo().getStartOffset());
            String endOffset = String.valueOf(extractVariableRefactoring.getVariableDeclaration().getLocationInfo().getEndOffset());
            String[] attributes = {startOffset, endOffset};
            Data action = DataFactory.createRefactoringData("EXTRACTED_VARIABLE", attributes);
            addActionData(action);
            ActionsUtils.addActionToLine(actionsMap, extractVariableRefactoring.getVariableDeclaration().getLocationInfo().getStartLine(), action);
        }
        for (AbstractCodeMapping reference : extractVariableRefactoring.getReferences()) {
            if (reference.getFragment2().getLocationInfo().getFilePath().equals(filePath)) {
                String startOffset = String.valueOf(reference.getFragment2().getLocationInfo().getStartOffset());
                String endOffset = String.valueOf(reference.getFragment2().getLocationInfo().getEndOffset());
                String extractedCode = extractVariableRefactoring.getVariableDeclaration().getInitializer().getExpression();
                String replacedBy = extractVariableRefactoring.getVariableDeclaration().getVariableName();
                String[] attributes = {startOffset, endOffset, extractedCode, replacedBy};
                Data action = DataFactory.createRefactoringData("EXTRACTED_VARIABLE_USAGE", attributes);
                addActionData(action);
                ActionsUtils.addActionToLine(actionsMap, reference.getFragment2().getLocationInfo().getStartLine(), action);
            }
        }
    }

    private void addExtractVariable(ExtractVariableRefactoring extractVariableRefactoring) {
        String startOffset = String.valueOf(extractVariableRefactoring.getVariableDeclaration().getLocationInfo().getStartOffset());
        String endOffset = String.valueOf(extractVariableRefactoring.getVariableDeclaration().getLocationInfo().getEndOffset());
        String[] attributes = {startOffset, endOffset};
        Data action = DataFactory.createRefactoringData("EXTRACTED_VARIABLE", attributes);
        addActionData(action);
        String filePath = extractVariableRefactoring.getVariableDeclaration().getLocationInfo().getFilePath();
        int line = extractVariableRefactoring.getVariableDeclaration().getLocationInfo().getStartLine();
        project.getService(GlobalChangesService.class).addChange(filePath, line, action);
        for (AbstractCodeMapping reference : extractVariableRefactoring.getReferences()) {
            startOffset = String.valueOf(reference.getFragment2().getLocationInfo().getStartOffset());
            endOffset = String.valueOf(reference.getFragment2().getLocationInfo().getEndOffset());
            String extractedCode = extractVariableRefactoring.getVariableDeclaration().getInitializer().getExpression();
            String replacedBy = extractVariableRefactoring.getVariableDeclaration().getVariableName();
            String[] referenceAttributes = {startOffset, endOffset, extractedCode, replacedBy};
            action = DataFactory.createRefactoringData("EXTRACTED_VARIABLE_USAGE", referenceAttributes);
            addActionData(action);
            filePath = reference.getFragment2().getLocationInfo().getFilePath();
            line = reference.getFragment2().getLocationInfo().getStartLine();
            project.getService(GlobalChangesService.class).addChange(filePath, line, action);
        }
    }

    private void handleRenameAttribute(Map<Integer, List<Data>> actionsMap, String filePath, RenameAttributeRefactoring renameAttributeRefactoring) {
        if (renameAttributeRefactoring.getRenamedAttribute().getLocationInfo().getFilePath().equals(filePath)) {
            String startOffset = String.valueOf(renameAttributeRefactoring.getRenamedAttribute().getLocationInfo().getStartOffset());
            String endOffset = String.valueOf(renameAttributeRefactoring.getRenamedAttribute().getLocationInfo().getEndOffset());
            Data action = DataFactory.createRefactoringData("RENAME_ATTRIBUTE", renameAttributeRefactoring.getOriginalAttribute().getName(), startOffset, endOffset);
            addActionData(action);
            ActionsUtils.addActionToLine(actionsMap, renameAttributeRefactoring.getRenamedAttribute().getLocationInfo().getStartLine(), action);
        }
    }

    private void addRenameAttribute(RenameAttributeRefactoring renameAttributeRefactoring) {
        String startOffset = String.valueOf(renameAttributeRefactoring.getRenamedAttribute().getLocationInfo().getStartOffset());
        String endOffset = String.valueOf(renameAttributeRefactoring.getRenamedAttribute().getLocationInfo().getEndOffset());
        Data action = DataFactory.createRefactoringData("RENAME_ATTRIBUTE", renameAttributeRefactoring.getOriginalAttribute().getName(), startOffset, endOffset);
        addActionData(action);
        String filePath = renameAttributeRefactoring.getRenamedAttribute().getLocationInfo().getFilePath();
        int line = renameAttributeRefactoring.getRenamedAttribute().getLocationInfo().getStartLine();
        project.getService(GlobalChangesService.class).addChange(filePath, line, action);
    }

    private void addActionData(Data action) {
        PersonIdent author = commit.getAuthorIdent();
        Date date = author.getWhen();
        LocalDateTime commitDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        action.setAuthor(author);
        action.setDateTime(commitDate);
    }

}
