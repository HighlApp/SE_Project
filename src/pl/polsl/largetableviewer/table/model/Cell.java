package pl.polsl.largetableviewer.table.model;

public class Cell {
    private String cellContent;
    private int rowNumber;
    private int columnNumber;
    private boolean visible;

    public Cell() {
    }

    public Cell(String cellContent, int rowNumber, int columnNumber) {
        this.cellContent = cellContent;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        visible=true;
    }

    public Cell(String cellContent, int rowNumber, int columnNumber, boolean visible) {
        this.cellContent = cellContent;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.visible = visible;
    }

    public String getCellContent() {
        return cellContent;
    }

    public void setCellContent(String cellContent) {
        this.cellContent = cellContent;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return cellContent;
    }
}
