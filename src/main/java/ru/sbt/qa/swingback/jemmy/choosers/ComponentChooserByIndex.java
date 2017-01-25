package ru.sbt.qa.swingback.jemmy.choosers;

import org.netbeans.jemmy.ComponentChooser;

import java.awt.*;

/**
 * Component chooser by index.
 */
public class ComponentChooserByIndex implements ComponentChooser {
	
	private int componentIndex;
	private int curIndex;
	
	public ComponentChooserByIndex(int componentIndex) {
		this.componentIndex = componentIndex;
		curIndex = 0;
	}
	
	@Override
	public boolean checkComponent(Component component) {
		if (curIndex > componentIndex) {
			curIndex = 0;
		}
		return componentIndex == curIndex++;
	}
	
	@Override
	public String getDescription() {
		return "Current index " + curIndex + "\n Component index " + componentIndex;
	}
}
