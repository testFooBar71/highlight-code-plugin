package visualelements.actions;

import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;
import visualelements.VisualElement;

import javax.swing.*;
import java.awt.*;

public class UpdatedVisualElement extends VisualElement {
    public UpdatedVisualElement(PsiElement psiElement) {
        super(psiElement);
        this.setLayout(new FlowLayout());
        this.setText("<html><b> UPD </b></html>");
        this.setSize(35, 20);
        this.setOpaque(true);
        this.setBackground(JBColor.GREEN);
        this.setForeground(JBColor.WHITE);
    }
}
