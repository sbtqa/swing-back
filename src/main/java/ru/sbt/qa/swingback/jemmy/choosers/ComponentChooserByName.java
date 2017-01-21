package ru.sbt.qa.swingback.jemmy.choosers;

import org.netbeans.jemmy.ComponentChooser;

import java.awt.*;

/**
 * Component chooser by name.
 *
 * @author Olga Germanova
 */
public class ComponentChooserByName implements ComponentChooser {
    private String name = "";

    public ComponentChooserByName(String aName) {
        name = aName;
    }

    @Override
    public boolean checkComponent(Component aComponent) {
        String compName = aComponent.getName();
        return compName != null && compName.equals(name);
    }

    @Override
    public String getDescription() {
        return name;
    }
}
