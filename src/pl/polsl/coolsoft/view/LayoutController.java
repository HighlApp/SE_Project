package pl.polsl.coolsoft.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class LayoutController {
    @FXML
    private TextField fileNameField;

    @FXML
    private Spinner colRangeFrom;

    @FXML
    private Spinner colRangeTo;

    @FXML
    private Spinner rowRangeFrom;

    @FXML
    private Spinner rowRangeTo;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private Button selectFileButton;

    //This method name must be 'initialize'!
    @SuppressWarnings("unchecked")
    public void initialize() {
        SpinnerValueFactory<Integer> colRangeFromFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1);
        SpinnerValueFactory<Integer> colRangeToFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 20);
        SpinnerValueFactory<Integer> rowRangeFromFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1);
        SpinnerValueFactory<Integer> rowRangeToFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 20);


        colRangeFrom.setValueFactory(colRangeFromFactory);
        colRangeTo.setValueFactory(colRangeToFactory);
        rowRangeFrom.setValueFactory(rowRangeFromFactory);
        rowRangeTo.setValueFactory(rowRangeToFactory);
    }

    @FXML
    protected void openFileChooser() {
        Window owner = fileNameField.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Any file extension", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(owner);
        if (file == null) {
            return;
        }
        fileNameField.setText(file.toString());

    }
    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        Window owner = submitButton.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Warning!",
                    "Twardy dysk stracił połączenie z twardszym dyskiem, a najtwardszy dysk sobie wypadł!");
    }
}
