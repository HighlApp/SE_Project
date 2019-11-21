package pl.polsl.largetableviewer.table.controller;

import pl.polsl.largetableviewer.table.exception.ControllerInitializationException;
import pl.polsl.largetableviewer.table.exception.TableSourceFileOpeningException;
import pl.polsl.largetableviewer.table.service.TableService;
import pl.polsl.largetableviewer.table.view.TableReader;

public class TableController {

    private final TableReader tableReader;
    private final TableService tableService;

    public TableController(char columnSeparator, char rowSeparator, String sourceFilePath) throws ControllerInitializationException {
        try {
            tableReader = new TableReader(columnSeparator, rowSeparator, sourceFilePath);
            tableService = new TableService();
        } catch (TableSourceFileOpeningException ex) {
            throw new ControllerInitializationException("Could not initialize table file reader.", ex);
        }
    }
}
