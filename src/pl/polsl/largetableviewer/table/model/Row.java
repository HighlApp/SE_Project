package pl.polsl.largetableviewer.table.model;

import java.util.LinkedList;
import java.util.List;

public class Row {
    private boolean visible;
    private long rowNumber;
    private boolean transpose;
    private List<Cell> rowCells = new LinkedList<>();

    public Row() {
    }

    public Row(boolean visible, long rowNumber, boolean transpose) {
        this.visible = visible;
        this.rowNumber = rowNumber;
        this.transpose = transpose;
    }

    public Row(boolean visible, long rowNumber, boolean transpose, List<Cell> rowCells) {
        this.visible = visible;
        this.rowNumber = rowNumber;
        this.transpose = transpose;
        this.rowCells = rowCells;
    }

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

    public void addCell(Cell cell){
        rowCells.add(cell);
    }
}
