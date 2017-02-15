package ru.sbtqa.tag.swingback.jemmy;

import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.operators.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.swingback.Form;
import ru.sbtqa.tag.swingback.SwingBackRuntimeException;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static org.netbeans.jemmy.operators.Operator.*;

/**
 * Common action for interaction with jemmy components.
 */
public class CommonActions {
    
    private CommonActions() {
    }
    
    private static final Logger LOG = LoggerFactory.getLogger(CommonActions.class);
    
    //********************  TABBED PANE *****************************************************************
    
    //******************** BUTTON *****************************************************************
    
    /**
     * Push the button if it is enabled.
     */
    public static void pushButtonIfIsEnabled(JButtonOperator buttonOperator) {
        if (buttonOperator.isEnabled()) {
            buttonOperator.push();
        }
    }
    
    //******************** TEXT *****************************************************************
    
    /**
     * Return text component value.
     * @param chooser   TextComponent's chooser in which search will be performed.
     * @param container Container with essential component.
     */
    public static String getTextComponentValue(ContainerOperator container, ComponentChooser chooser) {
        return new JTextComponentOperator(container, chooser).getText();
    }
    
    //******************** TABLE *****************************************************************
    
    /**
     * Click on the table column title.
     * @throws IllegalArgumentException If index isn't founded.
     */
    public static void clickOnTableColumnTitle(JTableOperator table, String colName) {
        JTableHeaderOperator headerOperator = table.getHeaderOperator();
        int colInd = findColumnIndex(table, colName);
        if (colInd == -1) {
            throw new IllegalArgumentException("The index for column name \"" + colName + "\" is not founded.");
        }
        Point columnPoint = headerOperator.getPointToClick(colInd);
        headerOperator.clickMouse(columnPoint.x, columnPoint.y + 3, 1); // Click to header
    }
    
    /**
     * Search row with the specified parameters by equals.
     * @param valMap column name/cell value.
     * @return Index of the first essential row. -1 if isn't founded.
     */
    public static int findIndexFirstTableRowWithParams(JTableOperator table, Map<String, String> valMap) {
        Map<String, StringComparator> compMap = valMap.entrySet()
                                                      .stream()
                                                      .collect(Collectors.toMap(Entry::getKey, o -> String::equals));
        return findIndexFirstTableRowWithParams(table, valMap, compMap);
    }
    
    //todo need test
    /**
     * Search row with the specified parameters by the comparators.
     * @param valMap column name/cell value
     * @param compMap column name/String comparator for comparing cell value
     * @return Index of the first essential row. -1 if isn't founded.
     */
    public static int findIndexFirstTableRowWithParams(JTableOperator table, Map<String, String> valMap, Map<String, StringComparator> compMap) {
        
        // Searching and verifying column indexes.
        Map<String, Integer> colIndexMap = valMap.entrySet()
                                                 .stream()
                                                 .collect(Collectors.toMap(Entry::getKey, nameAndVal -> findColumnIndex(table, nameAndVal
                                                         .getKey())));
        Optional<Entry<String, Integer>> wrongColName = colIndexMap.entrySet()
                                                          .stream()
                                                          .filter(entry -> entry.getValue() == -1)
                                                          .findAny();
        if (wrongColName.isPresent()) {
            throw new IllegalArgumentException("The index for column name \"" + wrongColName.get().getKey() + "\" is not founded.");
        }
    
        boolean found;
        int colInd;
        String colName;
        StringComparator colNameComp;
        
        Object cellValObj;
        String cellValStr;
    
        int rowCount = table.getRowCount();
        // Searching element
        for (int rowInd = 0; rowInd < rowCount; rowInd++) {
            found = true;
            // verify row
            for (Entry<String, String> colNameAndValue : valMap.entrySet()) {
                colName = colNameAndValue.getKey();
                colNameComp = compMap.get(colName);
                colInd = colIndexMap.get(colName);
                cellValObj = table.getValueAt(rowInd, colInd);
    
                cellValStr = cellValObj != null ? cellValObj.toString() : "";
    
                if (!colNameComp.equals(colNameAndValue.getValue(), cellValStr)) {
                    found = false;
                    break;
                }
                
            }
            if (found)
                return rowInd;
        }
        return -1;
    }
    
    /**
     * Find column index in the table. Using equals.
     * @param columnName The name of the desired column.
     * @return Index of the column name. The index starts from 0.
     */
    public static int findColumnIndex(JTableOperator table, String columnName) {
        int res = table.findColumn(columnName, String::equals);
        // There is second way to find column name index. The first one may doesn't work.
        if (res == -1) {
            try {
                res = table.getHeaderOperator().getColumnModel().getColumnIndex(columnName);
            } catch (IllegalArgumentException e) {
                LOG.info("Column is not founded.", e);
                return -1;
            }
        }
        return res;
    }
    
    /**
     * Select first table element.
     */
    public static void selectFistTableElem(JTableOperator tableOperator) {
        tableOperator.selectCell(0, 0);
    }
    
    /**
     * Return all the table's data as list of rows.
     * @see #getListMapsFromTable(JTableOperator)
     */
    public static List<Map<String, String>> getListMapsFromTable(JTableOperator table) {
        return getTableRowsFromRange(table, 0, table.getRowCount());
    }
    
    /**
     * Return the table's rows from specified range.
     * @see #getListMapsFromTable(JTableOperator)
     */
    public static List<Map<String, String>> getTableRowsFromRange(JTableOperator table, int startIndex, int lastIndex) {
        List<Map<String, String>> resList = new LinkedList<>();
        for (int row = startIndex; row < lastIndex; row++) {
            resList.add(getMapFromTableByRowIndex(table, row));
        }
        return resList;
    }
    
    /**
     * Return from table's row with specified index a map (Key - sting column name. Value - sting row value)
     * with the correct order. If the table is empty - the map will be empty.
     */
    public static Map<String, String> getMapFromTableByRowIndex(JTableOperator tableOperator, int rowIndex) {
        Map<String, String> resMap = new LinkedHashMap<>();
        int columnCount = tableOperator.getColumnCount();
        Object cellValue;
        for (int column = 0; column < columnCount; column++) {
            cellValue = tableOperator.getValueAt(rowIndex, column);
            resMap.put(tableOperator.getColumnName(column), cellValue == null ? "" : cellValue.toString());
        }
        return resMap;
    }
    
    /**
     * Return map with row's values from selected row.
     */
    public static Map<String, String> getMapFromSelectRow(JTableOperator table) {
        return getMapFromTableByRowIndex(table, table.getSelectedRow());
    }
    
    
    //******************** TREE *****************************************************************
    
    /**
     * Select to the specified tree node by path.
     * @param tree  The tree with essential node.
     * @param paths The path to node.
     */
    public static void chooseTreeNode(JTreeOperator tree, String[] paths) {
        TreePath node = tree.findPath(paths[0].trim());
        tree.expandPath(node);
        for (int i = 1; i < paths.length; i++) {
            for (TreePath childPath : tree.getChildPaths(node)) {
                if (childPath.toString().contains(paths[i].trim())) {
                    if (i != paths.length - 1) {
                        node = childPath;
                        tree.expandPath(node);
                        break;
                    } else {
                        tree.selectPath(childPath);
                        return;
                    }
                }
            }
        }
    }
    
    
    //********************  LABEL *****************************************************************
    
    /**
     * @param container The container containing the label.
     * @param chooser   Label chooser.
     * @return Label text.
     */
    public static String getLabelText(ContainerOperator container, ComponentChooser chooser) {
        return new JLabelOperator(container, chooser).getText();
    }
    
    
    //********************  CHECK BOX *****************************************************************
    
    /**
     * Set check box value.
     */
    public static void setCheckBox(ContainerOperator container, ComponentChooser chooser, boolean value) {
//        new JCheckBoxOperator(container, chooser).setSelected(value);
        new JCheckBoxOperator(container, chooser).changeSelection(value);
    }
    
    // is selected
    
    /**
     * Check checkbox value.
     */
    public static boolean isSelectedCheckBox(ContainerOperator container, ComponentChooser chooser) {
        return new JCheckBoxOperator(container, chooser).isSelected();
    }
    
    //********************  COMBO BOX *****************************************************************
    
    /**
     * Choose specified element in combo box.
     * @param comboBox   Jemmy operator of combo box.
     * @param itemName   Selected item name.
     * @param comparator Comparator for searching an essential item.
     * @throws SwingBackRuntimeException when essential item is't founded.
     */
    public static void chooseComboBoxItem(JComboBoxOperator comboBox, String itemName, StringComparator comparator) {
        int itemIndex = comboBox.findItemIndex(itemName, comparator);
        if (itemIndex == -1) {
            throw new SwingBackRuntimeException("Item with name \"" + itemName + "\" is not founded in combo box.");
        }
        comboBox.setSelectedIndex(itemIndex);
        comboBox.pushKey(KeyEvent.VK_ENTER);
    }
    
    /**
     * Return list string values of the combo box, if combo box is empty - return empty list.
     * @return list string values of the combo box.
     */
    public static List<String> getComboBoxItems(JComboBoxOperator comboBoxOperator) {
        int itemCount = comboBoxOperator.getItemCount();
        List<String> items = new LinkedList<>();
        for (int i = 0; i < itemCount; i++) {
            items.add(comboBoxOperator.getItemAt(i).toString());
        }
        return items;
    }
    
    //********************  Context Menu *****************************************************************
    
    
    //********************  OTHER *****************************************************************
    
    
    /**
     * Return text from JOptionPane-dialog
     * @param dialog dialog with JOptionPane.
     * @return Message text.
     */
    public static String getJOptionPaneMes(JDialogOperator dialog) {
        Component component = new ContainerOperator(dialog.getContentPane()).getComponent(0);
        return ((JOptionPane) component).getMessage().toString();
    }
    
    //******************** CHECKING *****************************************************************
    
    /**
     * Check presence components in the form.
     * @param chooser   Component's chooser in which search will be performed.
     * @param container Container with essential component.
     * @param type      Component type.
     * @return true - if element is presence, otherwise - false.
     */
    public static boolean isComponentPresence(ContainerOperator container, Form.ComponentType type,
                                              ComponentChooser chooser) {
        Window window = container.getWindow();
        // Search using JComponentOperator.findJComponent() unsuitable. Choose component may be on index.
        switch (type) {
            case BUTTON:
                return JButtonOperator.findJButton(window, chooser) != null;
            case CHECK_BOX:
                return JCheckBoxOperator.findJCheckBox(window, chooser) != null;
            case COMBO_BOX:
                return JComboBoxOperator.findJComboBox(window, chooser) != null;
            case RADIO_BUTTON:
                return JRadioButtonOperator.findJRadioButton(window, chooser) != null;
            case TEXT_FIELD:
                return JTextFieldOperator.findJTextField(window, chooser) != null;
            case TEXT_AREA:
                return JTextAreaOperator.findJTextArea(window, chooser) != null;
            case LABEL:
                return JLabelOperator.findJLabel(window, chooser) != null;
            case TREE:
                return JTreeOperator.findJTree(window, chooser) != null;
            case TABLE:
                return JTableOperator.findJTable(window, chooser) != null;
            default:
                throw new UnsupportedOperationException("Presence check is not implemented for type " + type + ".");
        }
    }
    
    /**
     * Check editability components in the form.
     * @param chooser   Component's chooser in which search will be performed.
     * @param container Container with essential component.
     * @param type      Component type.
     * @return true - if element is editable, otherwise - false.
     */
    public static boolean isComponentEditable(ContainerOperator container, Form.ComponentType type,
                                              ComponentChooser chooser) {
        System.out.println("type " + type);
        switch (type) {
            case TEXT_FIELD:
                return new JTextFieldOperator(container, chooser).isEditable();
            case TEXT_AREA:
                return new JTextAreaOperator(container, chooser).isEditable();
            case CHECK_BOX:
                return new JCheckBoxOperator(container, chooser).isEnabled();
            case RADIO_BUTTON:
                return new JRadioButtonOperator(container, chooser).isEnabled();
            default:
                throw new UnsupportedOperationException("Editability check is not implemented for type " + type + ".");
        }
    }
    
    
}
