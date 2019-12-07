package pl.polsl.largetableviewer.table.service;

import pl.polsl.largetableviewer.table.exception.WrongColumnException;
import pl.polsl.largetableviewer.table.exception.WrongRowException;
import pl.polsl.largetableviewer.table.model.Cell;
import pl.polsl.largetableviewer.table.model.Row;
import pl.polsl.largetableviewer.table.model.Table;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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

    public void addRow(Row row) {
        List<Row> tableRows = table.getRows();
        tableRows.add(row);
        table.setRows(tableRows);
    }

    public Row getRow(int rowNumber) {
        for (Row row : table.getRows()) {
            if (row.getRowNumber() == rowNumber) {
                return row;
            }
        }
        return null;
    }

    public void addCell(int rowNumber, Cell cell) throws WrongRowException {
        Row row = getRow(rowNumber);
        if (row == null) {
            throw new WrongRowException("Cannot insert Cell into specified Row, because the Row is nonexistent." + " Row number: " + rowNumber);
        }
        List<Cell> rowCells = row.getCells();
        rowCells.add(cell);
    }

    public Cell getCell(int columnNumber, int rowNumber) throws WrongRowException {
        Row row = getRow(rowNumber);
        if (row == null) {
            throw new WrongRowException("Cannot get Cell from specified Row, because the Row is nonexistent." + " Row number: " + rowNumber);
        }
        for (Cell cell : row.getCells()) {
            if (cell.getColumnNumber() == columnNumber) {
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


    private void setColumnsVisibility(List<Integer> columnsNumbers, boolean visible) throws WrongColumnException {
        for (Integer columnNumber : columnsNumbers) {
            if (columnNumber > table.getNumberOfColumns()) {
                throw new WrongColumnException("Specified column number exceeds the range of possible column numbers");
            }
        }

        List<Cell> rowCells;

        for (Row row : table.getRows()) {
            if (row.isVisible()) {
                rowCells = row.getCells();
                for (Cell cell : rowCells) {
                    if (columnsNumbers.contains(cell.getColumnNumber())) {
                        cell.setVisible(visible);
                    }
                }
            }
        }
    }

    public void setRowsAndColumnsVisibility(List<Integer> rowsNumbers, List<Integer> columnsNumbers, boolean visibility) throws WrongRowException, WrongColumnException {
        if (rowsNumbers != null) {
            setRowsVisibility(rowsNumbers, visibility);
        }
        if (columnsNumbers != null) {
            setColumnsVisibility(columnsNumbers, visibility);
        }
    }

    public Row transposeRow(int rowNumber) {
        Row row = getRow(rowNumber);
        if (row.isTranspose()) {
            row.setTranspose(false);
        } else {
            row.setTranspose(true);
        }
        return row;
    }

    public void setTableSize(int rows, int columns) {
        table.setNumberOfRows(rows);
        table.setNumberOfColumns(columns);
    }

    public void setAllCellsVisibile(boolean visibile) {
        for (Row row : table.getRows()) {
            row.setVisible(visibile);
            for (Cell cell : row.getCells()) {
                cell.setVisible(visibile);
            }
        }
    }

    public void setRowsVisibleForSequence(String sequence, List<Integer> inRows, List<Integer> inColumns) throws WrongRowException, WrongColumnException {
        boolean remainVisible = false;
        if (inRows == null || inRows.size() == 0) {
            throw new WrongRowException("Did not specified any row.");
        }
        if (inColumns == null || inColumns.size() == 0) {
            throw new WrongColumnException("Did not specify any column.");
        }
        setAllCellsVisibile(false);
        setRowsAndColumnsVisibility(inRows, inColumns, true);

        for (Row row : table.getRows()) {
            if (row.isVisible()) {
                remainVisible = false;
                for (Cell cell : row.getCells()) {
                    if(cell.isVisible()) {
                        if (cell.getContent().contains(sequence)) {
                            remainVisible = true;
                        }
                    }else{
                        cell.setVisible(true);
                    }
                }
                row.setVisible(remainVisible);
            }
        }


    }

    public List<Integer> getVisibleRowsNumbers() {
        List<Integer> visibleRows = new LinkedList<>();
        for (Row row : table.getRows()) {
            if (row.isVisible()) {
                visibleRows.add(row.getRowNumber());
            }
        }
        return visibleRows;
    }

    public String tableStringRepresentation(char columnSeparator, char rowSeparator, int cellMaxLength) {
        StringBuilder stringRepresentation = new StringBuilder();
        boolean cellAffected = false;
        ListIterator<Cell> cellsIterator;
        Cell previousCell;
        for (Row row : table.getRows()) {
            if (row.isVisible()) {
                cellAffected = false;
                if (!row.isTranspose()) {
                    for (Cell cell : row.getCells()) {
                        if (cell.isVisible()) {
                            stringRepresentation.append(cell.toString().substring(0, Math.min(cell.toString().length(), cellMaxLength)) + columnSeparator);
                            cellAffected = true;
                        }
                    }
                } else { //row is transposed
                    cellsIterator = row.getCells().listIterator(row.getCells().size());
                    while (cellsIterator.hasPrevious()) {
                        previousCell = cellsIterator.previous();
                        if (previousCell.isVisible()) {
                            stringRepresentation.append(previousCell.toString().substring(0, Math.min(previousCell.toString().length(), cellMaxLength)) + columnSeparator);
                            cellAffected = true;
                        }
                    }
                }
                if (cellAffected) {
                    stringRepresentation.delete(stringRepresentation.length() - 1, stringRepresentation.length());
                    stringRepresentation.append(rowSeparator);
                }
            }
        }
        if (stringRepresentation.length() > 0) {
            stringRepresentation.deleteCharAt(stringRepresentation.length() - 1);
        }
        return stringRepresentation.toString();
    }

    public void exportTableToFile(char columnSeparator, char rowSeparator, int cellMaxLength, File outputFile) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
        ListIterator<Cell> cellsIterator;
        Cell previousCell;
        String toWrite;
        for (Row row : table.getRows()) {
            if (row.isVisible()) {
                if (!row.isTranspose()) {
                    for (Cell cell : row.getCells()) {
                        if (cell.isVisible()) {
                            toWrite = cell.toString().substring(0, Math.min(cell.toString().length(), cellMaxLength));
                            if (cell.getColumnNumber() == table.getNumberOfColumns()) {
                                bufferedWriter.write(toWrite);
                            } else {
                                bufferedWriter.write(toWrite + columnSeparator);
                            }
                        }
                    }
                } else { //is transposed
                    cellsIterator = row.getCells().listIterator(row.getCells().size());
                    while (cellsIterator.hasPrevious()) {
                        previousCell = cellsIterator.previous();
                        if (previousCell.isVisible()) {
                            toWrite = previousCell.toString().substring(0, Math.min(previousCell.toString().length(), cellMaxLength));
                            if (previousCell.getColumnNumber() == 1) {
                                bufferedWriter.write(toWrite);
                            } else {
                                bufferedWriter.write(toWrite + columnSeparator);
                            }
                        }
                    }
                }

                if (row.getRowNumber() != table.getNumberOfRows()) {
                    bufferedWriter.write(rowSeparator);
                }
            }
        }
        bufferedWriter.close();
    }

}
