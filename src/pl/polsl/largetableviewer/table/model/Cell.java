package pl.polsl.largetableviewer.table.model;

public class Cell {
    private String content;
    private int rowNumber;
    private int columnNumber;
    private boolean visible;

    public Cell() {
    }

    public Cell(String content, int rowNumber, int columnNumber) {
        this.content = content;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        visible=true;
    }

    public Cell(String content, int rowNumber, int columnNumber, boolean visible) {
        this.content = content;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.visible = visible;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        return content;
    }
}
