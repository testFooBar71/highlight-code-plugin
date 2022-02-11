package visualelements;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;
import java.awt.*;

public class PopupContent extends JLabel {
    public PopupContent(String content) {
        this.setLayout(new FlowLayout());
        JBLabel jl = new JBLabel(content);
        jl.setSize(jl.getPreferredSize());
        jl.setOpaque(true);
        jl.setForeground(JBColor.BLACK);
        int width = jl.getWidth();
        int height = jl.getHeight();
        this.add(jl);
        setSize(width, height);
    }
}
