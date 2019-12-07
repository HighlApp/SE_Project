package pl.polsl.largetableviewer.table.view;

import pl.polsl.largetableviewer.table.exception.TableSourceFileOpeningException;
import pl.polsl.largetableviewer.table.model.Cell;
import pl.polsl.largetableviewer.table.model.Row;

import java.io.*;

public class TableReader {
    private final char columnSeparator;
    private final char rowSeparator;
    private final File dataSource;
    private final BufferedReader tableStream;
    private int numberOfRows = 0;
    private int numberOfColumns = 0;
    private int numberOfColumnForCurrentRow = 0;
    private boolean columnsCounted = false;
    private boolean rowsCounted = false;

    public TableReader(char columnSeparator, File sourceFile) throws TableSourceFileOpeningException {
        this(columnSeparator, '\n', sourceFile);
    }

    public TableReader(char columnSeparator, char rowSeparator, File sourceFile)
            throws TableSourceFileOpeningException {
        this.columnSeparator = columnSeparator;
        this.rowSeparator = rowSeparator;
        try {
            dataSource = sourceFile;
            tableStream = new BufferedReader(new FileReader(dataSource));
        } catch (FileNotFoundException ex) {
            throw new TableSourceFileOpeningException("Specified data/table source file could not have been opened!",
                    ex);
        }
    }

    private int readChar() throws IOException {
        return tableStream.read();
    }

    @Deprecated
    public Cell readNextCell() throws TableSourceFileOpeningException {
        if(rowsCounted) {
            return null;
        }
        int charCode;
        char symbol;
        StringBuilder cellContent = new StringBuilder();
        try {
            while ((charCode = readChar()) != -1) {
                symbol = (char) charCode;
                if (symbol == columnSeparator || symbol == rowSeparator) {
                    ++numberOfColumnForCurrentRow;
                    if (symbol == rowSeparator) {
                        ++numberOfRows;
                        if (columnsCounted == false) {
                            numberOfColumns = numberOfColumnForCurrentRow;
                            columnsCounted = true;
                        } else {
                            if (numberOfColumns != numberOfColumnForCurrentRow) {
                                throw new TableSourceFileOpeningException(
                                        "Input table file is corrupted. Number of columns for rows do not match (not rectangular table)!");
                            }

                        }
                        numberOfColumnForCurrentRow = 0;
                    }
                    return new Cell(cellContent.toString(),numberOfRows, numberOfColumnForCurrentRow,true);
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
            return new Cell(cellContent.toString(),numberOfRows, numberOfColumnForCurrentRow,true);
//            return cellContent.toString();
        }
        return null;

    }

    public Cell nextCell() throws TableSourceFileOpeningException{
        if(rowsCounted) {
            return null;
        }
        if(numberOfRows==0){
            numberOfColumnForCurrentRow =1;
            numberOfRows=1;
            numberOfColumns=1;
        }
        int symbolCode;
        char symbol;
        StringBuilder cellContent = new StringBuilder();
        Cell cell;
        try{
            while((symbolCode=readChar())!=-1){ //-1 means end of the file
                symbol=(char) symbolCode;
                if(symbol==columnSeparator||symbol==rowSeparator){
                    cell = new Cell(cellContent.toString(), numberOfRows, numberOfColumnForCurrentRow);
                    if(symbol==columnSeparator) {
                        ++numberOfColumnForCurrentRow;
                    }else{ //it is rowSeparator
                        numberOfColumns=numberOfColumnForCurrentRow;
                        columnsCounted=true;
                        ++numberOfRows;
                        numberOfColumnForCurrentRow=1;
                    }
                    return cell;
                }else{
                    if(symbol!='\n'&&symbol!='\r'){
                        cellContent.append(symbol);
                    }
                }
            }
            //-1 occured, data in buffer is to store (it might be empty if last cell was empty
            cell = new Cell(cellContent.toString(), numberOfRows, numberOfColumnForCurrentRow);
            rowsCounted=true;
            return cell;
        }catch (IOException ex){
            throw new TableSourceFileOpeningException("Reading file was interrupted, could not finish the operation!",
                    ex);
        }
    }

    public Row nextRow() throws TableSourceFileOpeningException{
        Row row = new Row();
        Cell cell;
        while((cell = nextCell())!=null){
            if(columnsCounted &&  cell.getColumnNumber()==numberOfColumns){
                row.addCell(cell);
                break;
            }else{
                row.addCell(cell);
            }
        }
        if(row.size()>0){ //there are some cells, but there might be a fewer number of columns if user used the readCell method before this one
            row.setRowNumber(row.getCell(1).getRowNumber());
            return row;
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
