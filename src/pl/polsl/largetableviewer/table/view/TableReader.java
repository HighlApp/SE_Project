package pl.polsl.largetableviewer.table.view;

import pl.polsl.largetableviewer.table.exception.TableSourceFileOpeningException;
import pl.polsl.largetableviewer.table.model.Cell;

import java.io.*;

public class TableReader {
    private final char columnSeparator;
    private final char rowSeparator;
    private final File dataSource;
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
        this.columnSeparator = columnSeparator;
        this.rowSeparator = rowSeparator;
        try {
            dataSource = new File(sourceFilePath);
            tableStream = new BufferedReader(new FileReader(dataSource));
        } catch (FileNotFoundException ex) {
            throw new TableSourceFileOpeningException("Specified data/table source file could not have been opened!",
                    ex);
        }
    }

    private int readChar() throws IOException {
        return tableStream.read();
    }

    public Cell readNextCell() throws TableSourceFileOpeningException {
        if(rowsCounted){
            return null;
        }

        int charCode;
        char symbol;
        StringBuilder cellContent = new StringBuilder("");
        try {
            while ((charCode = readChar()) != -1) {
                symbol = (char) charCode;
                if (symbol == columnSeparator || symbol == rowSeparator) {
                    ++numberOfColumnsForCurrentRow;
                    if (symbol == rowSeparator) {
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
                    }
                    return new Cell(cellContent.toString(),numberOfRows,numberOfColumnsForCurrentRow,true);
                }
                if(symbol!='\r'&&symbol!='\n') {
                    cellContent.append(symbol);
                }
            }
        } catch (IOException ex) {
            throw new TableSourceFileOpeningException("Reading file was interrupted, could not finish the operation!",
                    ex);
        }

        // EOF handling below.
        rowsCounted = true;
        try {
            tableStream.close();
        }catch (IOException ex){
            throw new TableSourceFileOpeningException("Closing the source file was not possible.", ex);
        }

        if (cellContent.length() > 0) {
            return new Cell(cellContent.toString(),numberOfRows,numberOfColumnsForCurrentRow,true);
//            return cellContent.toString();
        }
        return null;

    }

    public char getColumnSeparator() {
        return columnSeparator;
    }

    public char getRowSeparator() {
        return rowSeparator;
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
