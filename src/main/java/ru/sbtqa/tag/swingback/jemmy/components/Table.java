package ru.sbtqa.tag.swingback.jemmy.components;

import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JTableHeaderOperator;
import org.netbeans.jemmy.operators.JTableOperator;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Table extends JTableOperator {

    public Table(ContainerOperator cont, ComponentChooser chooser) {
        super(cont, chooser);
    }

    /**
     * Return the table's rows from specified range.
     */
    public java.util.List<Map<String, String>> getTableRowsFromRange(int startIndex, int lastIndex) {
        java.util.List<Map<String, String>> resList = new LinkedList<>();
        for (int row = startIndex; row < lastIndex; row++) {
            resList.add(getMapFromTableByRowIndex(row));
        }
        return resList;
    }

    /**
     * Return from table's row with specified index a map (Key - sting column name. Value - sting row value)
     * with the correct order. If the table is empty - the map will be empty.
     */
    public Map<String, String> getMapFromTableByRowIndex(int rowIndex) {
        Map<String, String> resMap = new LinkedHashMap<>();
        int columnCount = getColumnCount();
        Object cellValue;
        for (int column = 0; column < columnCount; column++) {
            cellValue = getValueAt(rowIndex, column);
            resMap.put(getColumnName(column), cellValue == null ? "" : cellValue.toString());
        }
        return resMap;
    }

    /**
     * Return map with row's values from selected row.
     */
    public Map<String, String> getMapFromSelectRow() {
        return getMapFromTableByRowIndex(getSelectedRow());
    }

    /**
     * Select first table element.
     */
    public void selectFistTableElem() {
        this.selectCell(0, 0);
    }

    /**
     * Click on the table column title.
     * @throws IllegalArgumentException If index isn't founded.
     */
    public void clickOnTableColumnTitle(String colName) {
        JTableHeaderOperator headerOperator = this.getHeaderOperator();
        int colInd = findColumnIndex(colName);
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
    public int findIndexFirstTableRowWithParams(Map<String, String> valMap) {
        Map<String, StringComparator> compMap = valMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, o -> String::equals));
        return findIndexFirstTableRowWithParams(valMap, compMap);
    }

    /**
     * Search row with the specified parameters by the comparators.
     * @param valMap column name/cell value
     * @param compMap column name/String comparator for comparing cell value
     * @return Index of the first essential row. -1 if isn't founded.
     */
    public int findIndexFirstTableRowWithParams(Map<String, String> valMap, Map<String, StringComparator> compMap) {

        // Searching and verifying column indexes.
        Map<String, Integer> colIndexMap = valMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, nameAndVal -> findColumnIndex(nameAndVal.getKey())));
        Optional<Map.Entry<String, Integer>> wrongColName = colIndexMap.entrySet()
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

        int rowCount = this.getRowCount();
        // Searching element
        for (int rowInd = 0; rowInd < rowCount; rowInd++) {
            found = true;
            // verify row
            for (Map.Entry<String, String> colNameAndValue : valMap.entrySet()) {
                colName = colNameAndValue.getKey();
                colNameComp = compMap.get(colName);
                colInd = colIndexMap.get(colName);
                cellValObj = this.getValueAt(rowInd, colInd);

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
    public int findColumnIndex(String columnName) {
        int res = this.findColumn(columnName, String::equals);
        // There is second way to find column name index. The first one may doesn't work.
        if (res == -1) {
            try {
                res = this.getHeaderOperator().getColumnModel().getColumnIndex(columnName);
            } catch (IllegalArgumentException e) {
//                LOG.info("Column is not founded.", e);
                return -1;
            }
        }
        return res;
    }
}
