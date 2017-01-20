package ru.sbt.qa.swingback.choosers;

import org.netbeans.jemmy.ComponentChooser;

import javax.swing.*;
import java.awt.*;

/**
 * Component chooser by tool type.
 *
 * @author Olga Germanova
 */
public class ComponentChooserByToolTip implements ComponentChooser {
    private String toolTip = "";

    public ComponentChooserByToolTip(String aToolTip) {
        toolTip = aToolTip;
    }

    @Override
    public boolean checkComponent(Component aComponent) {
        String hint = ((JComponent) aComponent).getToolTipText();
        return hint != null && hint.equals(toolTip);
    }

    @Override
    public String getDescription() {
        return toolTip;
    }
}