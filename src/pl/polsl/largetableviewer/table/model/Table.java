package pl.polsl.largetableviewer.table.model;

import java.util.LinkedList;
import java.util.List;

public class Table {
    private String name;
    private int numberOfColumns;
    private int numberOfRows;
    /**
     * Outer list is a list of of rows. Inner list is the list of cells in a row.
     */
    private List<Row> rows = new LinkedList<>();

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

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public void addRow(Row row){
        rows.add(row);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
