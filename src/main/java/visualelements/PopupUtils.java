package visualelements;

import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import models.Data;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class PopupUtils {
    public static String createContent(Data data) {
        return "<html>" + data.renderData() +
                "</html>";
    }

    @NotNull
    public static JBPopup createPopup(Data modificationData) {
        JBPopupFactory jbPopupFactory = JBPopupFactory.getInstance();
        String content = PopupUtils.createContent(modificationData);
        JComponent visualElement = new PopupContent(content);
        ComponentPopupBuilder popupBuilder = jbPopupFactory.createComponentPopupBuilder(visualElement ,null);
        JBPopup popup = popupBuilder.createPopup();
        popup.setSize(new Dimension(visualElement.getWidth() + 15, visualElement.getHeight() + 10));
        return popup;
    }
}
