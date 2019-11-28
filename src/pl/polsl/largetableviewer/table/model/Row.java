package pl.polsl.largetableviewer.table.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Row {
    private boolean visible;
    private int rowNumber;
    private boolean transpose;
    private List<Cell> rowCells = new LinkedList<>();

    public Row() {
        visible=true;
        transpose=false;
    }

    public Row(boolean visible, int rowNumber, boolean transpose) {
        this.visible = visible;
        this.rowNumber = rowNumber;
        this.transpose = transpose;
    }

    public Row(boolean visible, int rowNumber, boolean transpose, List<Cell> rowCells) {
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

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public boolean isTranspose() {
        return transpose;
    }

    public void setTranspose(boolean transpose) {
        this.transpose = transpose;
    }

    public List<Cell> getRowCells() {
        return Collections.unmodifiableList(rowCells);
    }

//    public void setRowCells(List<Cell> rowCells) {
//        this.rowCells = rowCells;
//    }

    public void addCell(Cell cell){
        rowCells.add(cell);
    }

    public int size(){
        return rowCells.size();
    }

    public Cell getCell(int atColumn){
        return rowCells.get(atColumn-1);
    }

}
