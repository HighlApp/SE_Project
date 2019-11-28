package pl.polsl.largetableviewer.main;

import pl.polsl.largetableviewer.table.controller.TableController;
import pl.polsl.largetableviewer.table.exception.TableControllerInitializationException;

/**
 * Responsible for managing the communication between all controllers (JavaFX - GUI controller, Filter controller and Table controller).
 */
public class MainController {
    //controllers
    private TableController tableController;


    public void run(){
        //Bartek controller
        try {
            tableController = new TableController(',', ';', "D:\\studia\\sem7\\SE\\SE_Project2\\doTestow\\firstSmallTable.txt");
        }catch (TableControllerInitializationException ex){
            ex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("hold");

    }

}
