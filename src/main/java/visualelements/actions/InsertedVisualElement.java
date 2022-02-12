package visualelements.actions;

import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;
import visualelements.VisualElement;

import javax.swing.*;
import java.awt.*;

public class InsertedVisualElement extends VisualElement {
    public InsertedVisualElement(PsiElement psiElement) {
        super(psiElement);
        this.setLayout(new FlowLayout());
        this.setText("<html><b> INS </b></html>");
        this.setSize(30, 20);
        this.setOpaque(true);
        this.setBackground(JBColor.getHSBColor((float)0.6,(float)0.6,(float)0.95));
        this.setForeground(JBColor.BLACK);
    }
}
