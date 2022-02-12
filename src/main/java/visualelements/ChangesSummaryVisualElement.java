package visualelements;

import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;

import java.awt.*;

public class ChangesSummaryVisualElement extends VisualElement {
    public ChangesSummaryVisualElement(PsiElement psiElement) {
        super(psiElement);
        this.setLayout(new FlowLayout());
        this.setText("<html><b> SUMMARY </b></html>");
        this.setSize(150, 20);
        this.setOpaque(true);
        this.setBackground(JBColor.getHSBColor(0, 0, (float)0.82));
        this.setForeground(JBColor.BLACK);
    }
}
