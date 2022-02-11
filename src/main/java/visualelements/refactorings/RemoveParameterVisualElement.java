package visualelements.refactorings;

import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;
import visualelements.VisualElement;

import java.awt.*;

public class RemoveParameterVisualElement extends VisualElement {
    public RemoveParameterVisualElement(PsiElement psiElement) {
        super(psiElement);
        this.setLayout(new FlowLayout());
        this.setText("<html><b> RMP </b></html>");
        this.setSize(80, 20);
        this.setOpaque(true);
        this.setBackground(JBColor.YELLOW);
        this.setForeground(JBColor.WHITE);
    }
}
