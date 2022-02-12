package visualelements.refactorings;

import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;
import visualelements.VisualElement;

import java.awt.*;

public class RenameMethodVisualElement extends VisualElement {
    public RenameMethodVisualElement(PsiElement psiElement) {
        super(psiElement);
        this.setLayout(new FlowLayout());
        this.setText("<html><b> RNM </b></html>");
        this.setSize(50, 20);
        this.setOpaque(true);
        this.setBackground(JBColor.getHSBColor((float)0.16, (float)0.8, (float)0.83));
        this.setForeground(JBColor.BLACK);
    }
}
