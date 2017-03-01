package ru.sbtqa.tag.swingback.jemmy.components;

import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;

public class TextField extends JTextFieldOperator {
    public TextField(ContainerOperator cont, ComponentChooser chooser) {
        super(cont, chooser);
    }

}
