package pl.polsl.largetableviewer.table.model;

import java.util.List;

public class Row {
    private boolean visible;
    private long rowNumber;
    private boolean transpose;
    private List<Cell> rowCells;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public long getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(long rowNumber) {
        this.rowNumber = rowNumber;
    }

    public boolean isTranspose() {
        return transpose;
    }

    public void setTranspose(boolean transpose) {
        this.transpose = transpose;
    }

    public List<Cell> getRowCells() {
        return rowCells;
    }

    public void setRowCells(List<Cell> rowCells) {
        this.rowCells = rowCells;
    }
}
