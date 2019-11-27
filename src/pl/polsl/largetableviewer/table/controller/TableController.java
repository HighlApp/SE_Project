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
        int cellCounter;
        Row row = new Row(true, 0, false);
        Cell cell;
        while (!tableReader.isColumnsCounted()) {
            row.addCell(tableReader.readNextCell());
        }
        tableService.addRow(row);
        cellCounter = tableReader.getNumberOfColumns() + 1;
        row = null;
        while (!tableReader.isRowsCounted()) {
            cell = tableReader.readNextCell();
            if(cell==null && cellCounter == tableReader.getNumberOfColumns()){
                break; //proper finish. If second condition is not met, TODO: throw exception
            }
            if (cellCounter >= tableReader.getNumberOfColumns()) {
                cellCounter = 1;
                if (row != null) {
                    tableService.addRow(row);
                }
                row = new Row(true, cell.getRowNumber(), false);
            }
            row.addCell(cell);
            ++cellCounter;
        }
        System.out.println("Finished with: " + tableReader.getNumberOfColumns() + " columns and " + tableReader.getNumberOfRows()+" rows.");
    }
}
