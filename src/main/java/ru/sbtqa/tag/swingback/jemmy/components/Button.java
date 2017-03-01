package ru.sbtqa.tag.swingback.jemmy.components;

import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JButtonOperator;

public class Button extends JButtonOperator {

    public Button(ContainerOperator cont, ComponentChooser chooser) {
        super(cont, chooser);
    }

    /**
     * Push the button if it is enabled.
     */
    public void pushButtonIfIsEnabled() {
        if (isEnabled()) {
            push();
        }
    }
}
