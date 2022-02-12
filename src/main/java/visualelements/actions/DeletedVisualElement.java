package visualelements.actions;

import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;
import visualelements.VisualElement;

import javax.swing.*;
import java.awt.*;

public class DeletedVisualElement extends VisualElement {
    public DeletedVisualElement(PsiElement psiElement) {
        super(psiElement);
        this.setLayout(new FlowLayout());
        this.setText("<html><b> DEL </b></html>");
        this.setSize(30, 20);
        this.setOpaque(true);
        this.setBackground(JBColor.getHSBColor(0, (float)0.65, 1));
        this.setForeground(JBColor.BLACK);
    }
}
