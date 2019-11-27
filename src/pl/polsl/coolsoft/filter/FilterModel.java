package pl.polsl.coolsoft.filter;

import java.io.Serializable;

public class FilterModel implements Serializable {

    private String name;
    private Integer row;
    private Integer column;

    public FilterModel() {}

    public FilterModel(String name, Integer row, Integer column) {
        this.name = name;
        this.row = row;
        this.column = column;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "FilterModel{" +
                "name='" + name + '\'' +
                ", row=" + row +
                ", column=" + column +
                '}';
    }
}
