package pl.polsl.largetableviewer.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class LargeTextVieverApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL url = new File("src/pl/polsl/largetableviewer/view/layout.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Multiple Large Text File Viewer");
        setUserAgentStylesheet(STYLESHEET_MODENA);
        primaryStage.setScene(new Scene(root, 1100, 780));
        primaryStage.setMaxWidth(1100);
        primaryStage.setMinWidth(800);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
