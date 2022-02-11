package visualelements;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;
import models.Data;
import visualelements.events.VisualElementMouseEventsHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VisualElementWrapper extends VisualElement {
    public VisualElementWrapper(PsiElement psiElement, List<Data> actions, Editor editor) {
        super(psiElement);
        this.setLayout(new FlowLayout());
        int width = 0;
        int height = 0;
        for (Data action: actions) {
            JLabel visualElement = VisualElementFactory.createVisualElement(action.getType(), psiElement);
            int visualElementWidth = visualElement.getWidth();
            width += visualElementWidth;
            int visualElementHeight = visualElement.getHeight();
            height = Math.max(height, visualElementHeight);
            VisualElementMouseEventsHandler handler = new VisualElementMouseEventsHandler(editor, action);
            visualElement.addMouseListener(handler);
            this.add(visualElement);
        }
//        jl.setOpaque(true);
//        jl.setBackground(JBColor.BLUE);
//        jl.setForeground(JBColor.WHITE);
        setSize(width, height);
    }
}
