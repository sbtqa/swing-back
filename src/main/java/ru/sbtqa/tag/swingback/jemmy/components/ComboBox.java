package ru.sbtqa.tag.swingback.jemmy.components;

import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JComboBoxOperator;
import ru.sbtqa.tag.swingback.exceptions.SwingBackRuntimeException;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

public class ComboBox extends JComboBoxOperator {

    public ComboBox(ContainerOperator cont, ComponentChooser chooser) {
        super(cont, chooser);
    }

    /**
     * Choose specified element in combo box.
     *
     * @param itemName   Selected item name.
     * @param comparator Comparator for searching an essential item.
     * @throws SwingBackRuntimeException when essential item is't founded.
     */
    public void chooseComboBoxItem(String itemName, StringComparator comparator) {
        int itemIndex = findItemIndex(itemName, comparator);
        if (itemIndex == -1) {
            throw new SwingBackRuntimeException("Item with name \"" + itemName + "\" is not founded in combo box.");
        }
        setSelectedIndex(itemIndex);
        pushKey(KeyEvent.VK_ENTER);
    }

    /**
     * Return list string values of the combo box, if combo box is empty - return empty list.
     *
     * @return list string values of the combo box.
     */
    public List<String> getComboBoxItems() {
        int itemCount = getItemCount();
        List<String> items = new LinkedList<>();
        for (int i = 0; i < itemCount; i++) {
            items.add(getItemAt(i).toString());
        }
        return items;
    }
}
