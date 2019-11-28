package pl.polsl.largetableviewer.table.controller;

import pl.polsl.largetableviewer.table.exception.TableControllerInitializationException;
import pl.polsl.largetableviewer.table.exception.TableSourceFileOpeningException;
import pl.polsl.largetableviewer.table.model.Cell;
import pl.polsl.largetableviewer.table.model.Row;
import pl.polsl.largetableviewer.table.service.TableService;
import pl.polsl.largetableviewer.table.view.TableReader;

public class TableController {

    private TableReader tableReader;
    private TableService tableService;

    public TableController(char columnSeparator, char rowSeparator, String sourceFilePath) throws TableControllerInitializationException {
        reinitializeTable(columnSeparator, rowSeparator, sourceFilePath);
    }

    public void reinitializeTable(char columnSeparator, char rowSeparator, String sourceFilePath) throws TableControllerInitializationException {
        try {
            tableReader = new TableReader(columnSeparator, rowSeparator, sourceFilePath);
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
        tableService.getTable().setNumberOfRows(row.getRowNumber());
        tableService.getTable().setNumberOfColumns(row.size());
    }
}
