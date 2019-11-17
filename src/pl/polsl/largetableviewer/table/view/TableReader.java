package pl.polsl.largetableviewer.table.view;

import pl.polsl.largetableviewer.table.exception.TableSourceFileOpeningException;

import java.io.*;

public class TableReader {
    private final char COLUMN_SEPARATOR;
    private final char ROW_SEPARATOR;
    private final File DATA_SOURCE;
    private final BufferedReader tableStream;
    private int numberOfRows = 0;
    private int numberOfColumns = 0;
    private int numberOfColumnsForCurrentRow = 0;
    private boolean columnsCounted = false;
    private boolean rowsCounted = false;

    public TableReader(char columnSeparator, String sourceFilePath) throws TableSourceFileOpeningException {
        this(columnSeparator, '\n', sourceFilePath);
    }

    public TableReader(char columnSeparator, char rowSeparator, String sourceFilePath)
            throws TableSourceFileOpeningException {
        COLUMN_SEPARATOR = columnSeparator;
        ROW_SEPARATOR = rowSeparator;
        try {
            DATA_SOURCE = new File(sourceFilePath);
            tableStream = new BufferedReader(new FileReader(DATA_SOURCE));
        } catch (FileNotFoundException ex) {
            throw new TableSourceFileOpeningException("Specified data/table source file could not have been opened!",
                    ex);
        }
    }

    private int readChar() throws IOException {
        return tableStream.read();
    }

    public String readNextCell() throws TableSourceFileOpeningException {
        int charCode;
        StringBuilder cellContent = new StringBuilder("");
        try {
            while ((charCode = readChar()) != -1) {
                if (charCode == COLUMN_SEPARATOR || charCode == ROW_SEPARATOR) {
                    if (charCode == ROW_SEPARATOR) {
                        ++numberOfRows;
                        if (columnsCounted == false) {
                            numberOfColumns = numberOfColumnsForCurrentRow;
                            columnsCounted = true;
                        } else {
                            if (numberOfColumns != numberOfColumnsForCurrentRow) {
                                throw new TableSourceFileOpeningException(
                                        "Input table file is corrupted. Number of columns for rows do not match (not rectangular table)!");
                            }

                        }
                        numberOfColumnsForCurrentRow = 0;
                    } else if (charCode == COLUMN_SEPARATOR) {
                        ++numberOfColumnsForCurrentRow;
                    }

                    return cellContent.toString();
                }
                cellContent.append(charCode);
            }
        } catch (IOException ex) {
            throw new TableSourceFileOpeningException("Reading file was interrupted, could not finish the operation!",
                    ex);
        }
        // EOF handling below.
        rowsCounted = true;
        if (cellContent.length() > 0) {
            return cellContent.toString();
        }
        return null;

    }

    public char getColumnSeparator() {
        return COLUMN_SEPARATOR;
    }

    public char getRowSeparator() {
        return ROW_SEPARATOR;
    }

    public int getNumberOfRows() {
        if (rowsCounted) {
            return numberOfRows;
        }
        return -1;
    }

    public int getNumberOfColumns() {
        if (columnsCounted) {
            return numberOfColumns;
        }
        return -1;
    }

    public boolean isColumnsCounted() {
        return columnsCounted;
    }

    public boolean isRowsCounted() {
        return rowsCounted;
    }


}
