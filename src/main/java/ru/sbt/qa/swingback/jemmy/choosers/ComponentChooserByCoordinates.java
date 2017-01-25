package ru.sbt.qa.swingback.jemmy.choosers;

import org.netbeans.jemmy.ComponentChooser;

import java.awt.*;

/**
 * Component chooser by coordinates.
 */
public class ComponentChooserByCoordinates implements ComponentChooser {
	
	private int x;
	private int y;
	
	public ComponentChooserByCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean checkComponent(Component component) {
		return component.getX() == x && component.getY() == y;
	}
	
	@Override
	public String getDescription() {
		return "x = " + x + " y = " + y;
	}
}
