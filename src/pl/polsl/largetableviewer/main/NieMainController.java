package pl.polsl.largetableviewer.main;

import pl.polsl.largetableviewer.table.controller.TableController;
import pl.polsl.largetableviewer.table.exception.TableControllerInitializationException;
import pl.polsl.largetableviewer.table.exception.WrongColumnException;
import pl.polsl.largetableviewer.table.exception.WrongRowException;

/**
 * Responsible for managing the communication between all controllers (JavaFX - GUI controller, Filter controller and Table controller).
 */
public class NieMainController {
    //controllers
    private TableController tableController;


    public void run() {
        //Bartek controller
        try {
//            tableController = new TableController(',', ';', "D:\\studia\\sem7\\SE\\SE_Project2\\doTestow\\firstSmallTable.txt");
            tableController.sequenceSearch("ja", null, null);
            System.out.println(tableController.getTableStringRepresentation(',', '\n'));

        } catch (WrongColumnException e) {
            e.printStackTrace();
        } catch (WrongRowException e) {
            e.printStackTrace();
        }

    }
}
