package visualelements;

import com.intellij.psi.PsiElement;
import visualelements.actions.DeletedVisualElement;
import visualelements.actions.InsertedVisualElement;
import visualelements.actions.MovedVisualElement;
import visualelements.actions.UpdatedVisualElement;
import visualelements.refactorings.*;

import javax.swing.*;

public class VisualElementFactory {
    public static JLabel createVisualElement(String type, PsiElement psiElement) {
        switch (type) {
            case "CHANGES_SUMMARY":
                return new ChangesSummaryVisualElement(psiElement);
            case "UPD":
                return new UpdatedVisualElement(psiElement);
            case "INS":
                return new InsertedVisualElement(psiElement);
            case "MOV":
                return new MovedVisualElement(psiElement);
            case "DEL":
                return new DeletedVisualElement(psiElement);
            case "EXTRACTED_METHOD":
            case "EXTRACTED_METHOD_CALL":
                return new ExtractedMethodVisualElement(psiElement);
            case "RENAME_PARAMETER":
                return new RenameParameterVisualElement(psiElement);
            case "RENAME_METHOD":
                return new RenameMethodVisualElement(psiElement);
            case "RENAME_VARIABLE":
                return new RenameVariableVisualElement(psiElement);
            case "RENAME_CLASS":
                return new RenameClassVisualElement(psiElement);
            case "CHANGE_ATTRIBUTE_TYPE":
                return new ChangeAttributeTypeVisualElement(psiElement);
            case "CHANGE_RETURN_TYPE":
                return new ChangeReturnTypeVisualElement(psiElement);
            case "CHANGE_PARAMETER_TYPE":
                return new ChangeParameterTypeVisualElement(psiElement);
            case "CHANGE_VARIABLE_TYPE":
                return new ChangeVariableTypeVisualElement(psiElement);
            case "REMOVE_PARAMETER":
                return new RemoveParameterVisualElement(psiElement);
            case "ADD_PARAMETER":
                return new AddParameterVisualElement(psiElement);
            case "REORDER_PARAMETER":
                return new ReorderParameterVisualElement(psiElement);
            case "EXTRACT_INTERFACE":
                return new ExtractInterfaceVisualElement(psiElement);
            case "EXTRACT_SUPERCLASS":
                return new ExtractSuperclassVisualElement(psiElement);
            case "PULL_UP_ATTRIBUTE":
                return new PullUpAttributeVisualElement(psiElement);
            case "PULL_UP_METHOD":
                return new PullUpMethodVisualElement(psiElement);
            case "PUSH_DOWN_ATTRIBUTE":
                return new PushDownAttributeVisualElement(psiElement);
            case "PUSH_DOWN_METHOD":
                return new PushDownMethodVisualElement(psiElement);
            case "EXTRACTED_VARIABLE":
            case "EXTRACTED_VARIABLE_USAGE":
                return new ExtractedVariableVisualElement(psiElement);
            case "RENAME_ATTRIBUTE":
                return new RenameAttributeVisualElement(psiElement);
            case "GEAR":
                return new GearVisualElement(psiElement);
            default:
                return null;
        }
    }
}
