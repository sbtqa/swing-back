package ru.sbtqa.tag.swingback.jemmy.choosers;

import org.netbeans.jemmy.ComponentChooser;

import javax.swing.*;
import java.awt.*;

/**
 * Component chooser by text.
 *
 */
public class ComponentChooserByText implements ComponentChooser {
    private String text = "";

    public ComponentChooserByText(String aText) {
        text = aText;
    }

    @Override
    public boolean checkComponent(Component aComponent) {
        String componentText = ((AbstractButton) aComponent).getText();
        return componentText != null && componentText.equals(text);
    }

    @Override
    public String getDescription() {
        return text;
    }
}
