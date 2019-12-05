package pl.polsl.largetableviewer.table.controller;

import pl.polsl.largetableviewer.table.exception.TableControllerInitializationException;
import pl.polsl.largetableviewer.table.exception.TableSourceFileOpeningException;
import pl.polsl.largetableviewer.table.exception.WrongColumnException;
import pl.polsl.largetableviewer.table.exception.WrongRowException;
import pl.polsl.largetableviewer.table.model.Row;
import pl.polsl.largetableviewer.table.model.Table;
import pl.polsl.largetableviewer.table.service.TableService;
import pl.polsl.largetableviewer.table.view.TableReader;

import java.io.File;
import java.util.List;

public class TableController {

    private TableReader tableReader;
    private TableService tableService;

    /**
     * Initializes the table file by inserting it to the memory
     * @param columnSeparator column separator
     * @param rowSeparator row separator
     * @param sourceFile table file
     * @throws TableControllerInitializationException in case of initialization problem (file corrupt, reading interrupts)
     */
    public TableController(char columnSeparator, char rowSeparator, File sourceFile) throws TableControllerInitializationException {
        reinitializeTable(columnSeparator, rowSeparator, sourceFile);
    }

    /**
     * Used for defining new table, with new separators.
     * @param columnSeparator column separator
     * @param rowSeparator row separator
     * @param sourceFile table file
     * @throws TableControllerInitializationException in case of initialization problem (file corrupt, reading interrupts)
     */
    public void reinitializeTable(char columnSeparator, char rowSeparator, File sourceFile) throws TableControllerInitializationException {
        try {
            tableReader = new TableReader(columnSeparator, rowSeparator, sourceFile);
            tableService = new TableService();
            initializeTable();
        } catch (TableSourceFileOpeningException ex) {
            throw new TableControllerInitializationException("Could not initialize table file reader.", ex);
        }
    }

    private void initializeTable() throws TableSourceFileOpeningException {
        Row row = null;
        while(!tableReader.isRowsCounted()){
            row=tableReader.nextRow();
            tableService.addRow(row);
        }
        tableService.setTableSize(row.getRowNumber(), row.size());
    }

    /**
     * Search the given string sequence in the contents of cells, for each row
     * @param sequence specified substring
     * @param inRows rows to check (null if all)
     * @param inColumns columns to check (null if all)
     * @return returns the number of rows, that contain this substring
     * @throws WrongRowException thrown if row number exceed the table row dimension
     * @throws WrongColumnException thrown if column number exceed the table column dimension
     */
    public List<Integer> sequenceSearch(String sequence, List<Integer> inRows, List<Integer> inColumns) throws WrongRowException, WrongColumnException {
        tableService.setRowsVisibleForSequence(sequence, inRows, inColumns);
        return tableService.getVisibleRowsNumbers();
    }

    public Table getTable(){
        return tableService.getTable();
    }

    /**
     * Usefull for simple table presentation as a String
     * @param columnSeparator separator of column
     * @param rowSeparator separator of rows
     * @return table representation in form of String
     */
    public String getTableStringRepresentation(char columnSeparator, char rowSeparator){
        return tableService.tableStringRepresentation(columnSeparator, rowSeparator);
    }

    /**
     * Changes the visibility of chosen rows and columns
     * @param rowsNumbers list of row numbers to change
     * @param columnsNumbers lost of column numbers to change
     * @param visible defines if the chosen entities should be or not visible
     * @throws WrongColumnException in case of exceeding max column value
     * @throws WrongRowException in case of exceeding max row value
     */
    public void setRowsAndColumnsVisibility(List<Integer> rowsNumbers, List<Integer> columnsNumbers, boolean visible) throws WrongColumnException, WrongRowException{
        tableService.setRowsAndColumnsVisibility(rowsNumbers, columnsNumbers, visible);
    }

    public void setAllCellsVisibility(boolean visible){
        tableService.setAllCellsVisibile(visible);
    }
}
