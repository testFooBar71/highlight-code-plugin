package visualelements.refactorings;

import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;
import visualelements.VisualElement;

import java.awt.*;

public class RenameAttributeVisualElement extends VisualElement {
    public RenameAttributeVisualElement(PsiElement psiElement) {
        super(psiElement);
        this.setLayout(new FlowLayout());
        this.setText("<html><b> RNA </b></html>");
        this.setSize(95, 20);
        this.setOpaque(true);
        this.setBackground(JBColor.YELLOW);
        this.setForeground(JBColor.WHITE);
    }
}
