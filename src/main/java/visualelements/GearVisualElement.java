package visualelements;

import com.intellij.psi.PsiElement;
import com.intellij.ui.paint.PaintUtil;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GearVisualElement extends VisualElement {
    public GearVisualElement(PsiElement psiElement) {
        super(psiElement);
        this.setLayout(new FlowLayout());
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/mygear.png"));
        GraphicsConfiguration defaultConfiguration =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage image = UIUtil.createImage(defaultConfiguration, imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                BufferedImage.TYPE_INT_RGB, PaintUtil.RoundingMode.CEIL);
        Graphics graphics = image.getGraphics();
        graphics.fillRect(0, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight());
        graphics.drawImage(imageIcon.getImage(), 0, 0, null);
        ImageIcon imageIcon1 = new ImageIcon(image);
        JLabel iconImage = new JLabel(imageIcon1);
        int width = iconImage.getIcon().getIconWidth();
        int height = iconImage.getIcon().getIconHeight();
        this.add(iconImage);
        setSize(width, height);
    }
}
