package pl.polsl.largetableviewer.table.model;

import java.util.List;

public class Table {
    private int numberOfColumns;
    private int numberOfRows;
    /**
     * Outer list is a list of of rows. Inner list is the list of cells in a row.
     */
    private List<Row> tableRows;

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public List<Row> getTableRows() {
        return tableRows;
    }

    public void setTableRows(List<Row> tableRows) {
        this.tableRows = tableRows;
    }
}