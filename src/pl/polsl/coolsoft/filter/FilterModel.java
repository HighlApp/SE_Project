package pl.polsl.coolsoft.filter;

import java.io.Serializable;

public class FilterModel implements Serializable {

    private String name;
    private char columnSeparator;
    private char rowSeparator;
    private String columnExportRange;
    private String rowExportRange;
    private int fieldMaxLength;
    private boolean transposed;

    public FilterModel() {}

    public FilterModel(String name, char columnSeparator, char rowSeparator, String columnExportRange,
                       String rowExportRange, int fieldMaxLength, boolean transposed) {
        this.name = name;
        this.columnSeparator = columnSeparator;
        this.rowSeparator = rowSeparator;
        this.columnExportRange = columnExportRange;
        this.rowExportRange = rowExportRange;
        this.fieldMaxLength = fieldMaxLength;
        this.transposed = transposed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getColumnSeparator() {
        return columnSeparator;
    }

    public void setColumnSeparator(char columnSeparator) {
        this.columnSeparator = columnSeparator;
    }

    public char getRowSeparator() {
        return rowSeparator;
    }

    public void setRowSeparator(char rowSeparator) {
        this.rowSeparator = rowSeparator;
    }

    public String getColumnExportRange() {
        return columnExportRange;
    }

    public void setColumnExportRange(String columnExportRange) {
        this.columnExportRange = columnExportRange;
    }

    public String getRowExportRange() {
        return rowExportRange;
    }

    public void setRowExportRange(String rowExportRange) {
        this.rowExportRange = rowExportRange;
    }

    public int getFieldMaxLength() {
        return fieldMaxLength;
    }

    public void setFieldMaxLength(int fieldMaxLength) {
        this.fieldMaxLength = fieldMaxLength;
    }

    public boolean isTransposed() {
        return transposed;
    }

    public void setTransposed(boolean transposed) {
        this.transposed = transposed;
    }

    @Override
    public String toString() {
        return "FilterModel{" +
                "name='" + name + '\'' +
                ", columnSeparator=" + columnSeparator +
                ", rowSeparator=" + rowSeparator +
                ", columnExportRange='" + columnExportRange + '\'' +
                ", rowExportRange='" + rowExportRange + '\'' +
                ", fieldMaxLength=" + fieldMaxLength +
                ", transposed=" + transposed +
                '}';
    }
}
