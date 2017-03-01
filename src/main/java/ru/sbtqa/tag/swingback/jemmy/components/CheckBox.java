package ru.sbtqa.tag.swingback.jemmy.components;

import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JCheckBoxOperator;

public class CheckBox extends JCheckBoxOperator {

    public CheckBox(ContainerOperator cont, ComponentChooser chooser) {
        super(cont, chooser);
    }

    /**
     * Set check box value.
     */
    public void setCheckBox(boolean value) {
        changeSelection(value);
    }

}
