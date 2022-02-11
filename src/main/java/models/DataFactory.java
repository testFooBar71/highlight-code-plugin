package models;

import models.actions.*;
import models.refactorings.*;
import org.eclipse.jgit.lib.PersonIdent;

import java.time.LocalDateTime;

public class DataFactory {
//    public static Data createData(String type, PersonIdent author, LocalDateTime commitDate) {
//        switch (type) {
//            case "UPD":
//                return new Updated(author, commitDate);
//            case "INS":
//                return new Inserted(author, commitDate);
//            case "MOV":
//                return new Moved(author, commitDate);
//            case "DEL":
//                return new Deleted(author, commitDate);
//            case "UPD_MULTIPLE_TIMES":
//                return new UpdatedMultipleTimes(author, commitDate);
//            case "EXTRACTED_METHOD":
//                return new ExtractedMethod(null);
//            case "EXTRACTED_METHOD_CALL":
//                return new ExtractedMethodCall(null);
//            default:
//                return null;
//        }
//    }

    public static ModificationData createModificationData(String type, PersonIdent author, LocalDateTime commitDate, long startOffset, long endOffset, String... additionalData) {
        switch (type) {
            case "UPD":
                return new Updated(author, commitDate, startOffset, endOffset, additionalData);
            case "INS":
                return new Inserted(author, commitDate, startOffset, endOffset);
            case "MOV":
                return new Moved(author, commitDate, startOffset, endOffset, additionalData);
            case "DEL":
                return new Deleted(author, commitDate, startOffset, endOffset, additionalData);
            case "UPD_MULTIPLE_TIMES":
                return new UpdatedMultipleTimes(author, commitDate, startOffset, endOffset);
            default:
                return null;
        }
    }

    public static RefactoringData createRefactoringData(String type, String... attributes) {
        switch (type) {
            case "EXTRACTED_METHOD":
                return new ExtractedMethod(attributes);
            case "EXTRACTED_METHOD_CALL":
                return new ExtractedMethodCall(attributes);
            case "RENAME_PARAMETER":
                return new RenameParameter(attributes);
            case "RENAME_METHOD":
                return new RenameMethod(attributes);
            case "RENAME_VARIABLE":
                return new RenameVariable(attributes);
            case "RENAME_CLASS":
                return new RenameClass(attributes);
            case "CHANGE_ATTRIBUTE_TYPE":
                return new ChangeAttributeType(attributes);
            case "CHANGE_RETURN_TYPE":
                return new ChangeReturnType(attributes);
            case "CHANGE_PARAMETER_TYPE":
                return new ChangeParameterType(attributes);
            case "CHANGE_VARIABLE_TYPE":
                return new ChangeVariableType(attributes);
            case "REMOVE_PARAMETER":
                return new RemoveParameter(attributes);
            case "ADD_PARAMETER":
                return new AddParameter(attributes);
            case "REORDER_PARAMETER":
                return new ReorderParameter(attributes);
            case "EXTRACT_INTERFACE":
                return new ExtractInterface(attributes);
            case "EXTRACT_SUPERCLASS":
                return new ExtractSuperclass(attributes);
            case "PULL_UP_ATTRIBUTE":
                return new PullUpAttribute(attributes);
            case "PULL_UP_METHOD":
                return new PullUpMethod(attributes);
            case "PUSH_DOWN_ATTRIBUTE":
                return new PushDownAttribute(attributes);
            case "PUSH_DOWN_METHOD":
                return new PushDownMethod(attributes);
            case "EXTRACTED_VARIABLE":
                return new ExtractedVariable(attributes);
            case "EXTRACTED_VARIABLE_USAGE":
                return new ExtractedVariableUsage(attributes);
            case "RENAME_ATTRIBUTE":
                return new RenameAttribute(attributes);
            default:
                return null;
        }
    }

}
