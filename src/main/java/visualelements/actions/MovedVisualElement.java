package visualelements.actions;

import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;
import visualelements.VisualElement;

import javax.swing.*;
import java.awt.*;

public class MovedVisualElement extends VisualElement {
    public MovedVisualElement(PsiElement psiElement) {
        super(psiElement);
        this.setLayout(new FlowLayout());
        this.setText("<html><b> MOV </b></html>");
        this.setSize(35, 20);
        this.setOpaque(true);
        this.setBackground(JBColor.getHSBColor((float)0.09, (float)0.66, 1));
        this.setForeground(JBColor.BLACK);
    }
}
