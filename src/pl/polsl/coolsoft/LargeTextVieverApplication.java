package pl.polsl.coolsoft;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.polsl.coolsoft.filter.FilterException;
import pl.polsl.coolsoft.filter.FilterModel;
import pl.polsl.coolsoft.filter.FilterService;

import java.io.File;
import java.net.URL;

public class LargeTextVieverApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL url = new File("src/pl/polsl/coolsoft/view/layout.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Multiple Large Text File Viewer");
        setUserAgentStylesheet(STYLESHEET_MODENA);
        primaryStage.setScene(new Scene(root, 1300, 750));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
