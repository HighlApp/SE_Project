package pl.polsl.largetableviewer.table.service;

import pl.polsl.largetableviewer.table.exception.WrongColumnException;
import pl.polsl.largetableviewer.table.exception.WrongRowException;
import pl.polsl.largetableviewer.table.model.Cell;
import pl.polsl.largetableviewer.table.model.Row;
import pl.polsl.largetableviewer.table.model.Table;

import java.util.Iterator;
import java.util.List;

public class TableService {

    private Table table;

    public TableService() {
        table = new Table();
    }

    public TableService(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public void addRow(Row row){
        List<Row> tableRows = table.getTableRows();
        tableRows.add(row);
        table.setTableRows(tableRows);
    }

    public Row getRow(long rowNumber){
        for(Row row : table.getTableRows()){
            if(row.getRowNumber()==rowNumber){
                return row;
            }
        }
        return null;
    }

    public void addCell(long rowNumber, Cell cell) throws WrongRowException {
        Row row = getRow(rowNumber);
        if(row==null){
            throw new WrongRowException("Cannot insert Cell into specified Row, because the Row is nonexistent."+" Row number: "+rowNumber);
        }
        List<Cell> rowCells = row.getRowCells();
        rowCells.add(cell);
        row.setRowCells(rowCells);
    }

    public Cell getCell(long columnNumber, long rowNumber) throws WrongRowException{
        Row row = getRow(rowNumber);
        if(row==null){
            throw new WrongRowException("Cannot get Cell from specified Row, because the Row is nonexistent." +" Row number: "+rowNumber);
        }
        for(Cell cell : row.getRowCells()){
            if(cell.getColumn()==columnNumber){
                return cell;
            }
        }
        return null;
    }

    private void setRowsVisibility(List<Integer> rowsNumbers, boolean visible) throws WrongRowException {
        Row row;
        for (Integer rowNumber : rowsNumbers) {
            row = getRow(rowNumber);
            if (row == null) {
                throw new WrongRowException("Cannot set Row visibility, because the Row is nonexistent." + " Row number: " + rowNumber);
            }
            row.setVisible(visible);
        }
    }


    private void setColumnsVisibility(List<Integer> columnsNumbers, boolean visible) {
        List<Cell> rowCells;

        for(Row row : table.getTableRows()){
            if(row.isVisible()){
                rowCells=row.getRowCells();
                for(Cell cell : rowCells){
                    if(columnsNumbers.contains(cell.getColumn())){
                        cell.setVisible(visible);
                    }
                }
            }
        }
    }

    public Row transposeRow(long rowNumber){
        Row row = getRow(rowNumber);
        if(row.isTranspose()){
            row.setTranspose(false);
        }else {
            row.setTranspose(true);
        }
        return row;
    }
}
