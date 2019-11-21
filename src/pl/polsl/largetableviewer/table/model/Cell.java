package pl.polsl.largetableviewer.table.model;

public class Cell {
    private String cellContents;
    private int row;
    private int column;
    private boolean visible;

    public String getCellContents() {
        return cellContents;
    }

    public void setCellContents(String cellContents) {
        this.cellContents = cellContents;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
