package pl.polsl.largetableviewer.main;

/**
 * Repsonsible for creating the entry point of the application
 */
public class Main {
    //Application EntryPoint
    public static void main(String[] args){
        MainController mainController = new MainController();
        mainController.run();
    }
}
