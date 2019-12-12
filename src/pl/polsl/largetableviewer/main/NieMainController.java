package pl.polsl.largetableviewer.main;

import pl.polsl.largetableviewer.table.controller.TableController;
import pl.polsl.largetableviewer.table.exception.TableControllerInitializationException;
import pl.polsl.largetableviewer.table.exception.WrongColumnException;
import pl.polsl.largetableviewer.table.exception.WrongRowException;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Responsible for managing the communication between all controllers (JavaFX - GUI controller, Filter controller and Table controller).
 */
public class NieMainController {
    //controllers


    public static void main(String[] args) {
        //Bartek controller
        TableController tableController;
        try {
            tableController = new TableController(',', ';',new File("D:\\studia\\sem7\\SE\\SE_Project2\\doTestow\\firstSmallTable.txt"));
            List<Integer> allRows = new LinkedList<>();
            List<Integer> allColumns= new LinkedList<>();

            for(int i=1;i<4;++i){
                allRows.add(i);
                allColumns.add(i);
            }

            List<Integer> rowList = new LinkedList<>();
            rowList.add(3);
            rowList.add(2);
//            rowList.add(1);
            List<Integer> columnList = new LinkedList<>();
            columnList.add(1);
            tableController.sequenceSearch("i", allRows, allColumns);
            System.out.println(tableController.getTableStringRepresentation(',', '\n', 10)+"\n\n");
            tableController.setRowsAndColumnsVisibility(rowList, columnList, false);
            System.out.println(tableController.getTableStringRepresentation(',', '\n', 10));
            tableController.exportTable(',', ';', 10, "D:\\studia\\sem7\\SE\\SE_Project2\\doTestow\\firstSmallTableExport.txt");
//            tableController.exportTable(',', ';', 3, "C:\\Users\\Przemysław\\Desktop\\exportTable.txt");

        } catch(Exception ex){
            ex.printStackTrace();
        }

    }
}
