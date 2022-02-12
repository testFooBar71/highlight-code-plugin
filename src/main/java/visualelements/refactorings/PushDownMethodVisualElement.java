package visualelements.refactorings;

import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;
import visualelements.VisualElement;

import java.awt.*;

public class PushDownMethodVisualElement extends VisualElement {
    public PushDownMethodVisualElement(PsiElement psiElement) {
        super(psiElement);
        this.setLayout(new FlowLayout());
        this.setText("<html><b> PDM </b></html>");
        this.setSize(125, 20);
        this.setOpaque(true);
        this.setBackground(JBColor.getHSBColor((float)0.16, (float)0.8, (float)0.83));
        this.setForeground(JBColor.BLACK);
    }
}
