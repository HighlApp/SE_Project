package pl.polsl.coolsoft.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import pl.polsl.coolsoft.view.AlertHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainController {
    @FXML
    private TextField fileNameField;
    @FXML
    private Spinner colExportRangeFrom;
    @FXML
    private Spinner colExportRangeTo;
    @FXML
    private Spinner rowExportRangeFrom;
    @FXML
    private Spinner rowExportRangeTo;
    @FXML
    private Spinner colFilterRangeFrom;
    @FXML
    private Spinner colFilterRangeTo;
    @FXML
    private Spinner fieldMaxLength;
    @FXML
    private Button submitButton;
    @FXML
    private TextArea fileContentsTextArea;
    @FXML
    private ChoiceBox filterChoiceBox;


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
        SpinnerValueFactory<Integer> colExportFromFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1);
        SpinnerValueFactory<Integer> colExportToFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 20);
        SpinnerValueFactory<Integer> fieldMaxLengthFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 20);


        colExportRangeFrom.setValueFactory(colRangeFromFactory);
        colExportRangeTo.setValueFactory(colRangeToFactory);
        rowExportRangeFrom.setValueFactory(rowRangeFromFactory);
        rowExportRangeTo.setValueFactory(rowRangeToFactory);
        colFilterRangeFrom.setValueFactory(colExportFromFactory);
        colFilterRangeTo.setValueFactory(colExportToFactory);
        fieldMaxLength.setValueFactory(fieldMaxLengthFactory);

        colFilterRangeFrom.setDisable(true);
        colFilterRangeTo.setDisable(true);

    }

    @FXML
    protected void openFileChooser() {
        Window owner = fileNameField.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Any file extension", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        File inputFile = fileChooser.showOpenDialog(owner);
        if (inputFile == null) {
            return;
        }
        fileNameField.setText(inputFile.toString());

        try {
            Scanner s = new Scanner(inputFile);
            while (s.hasNextLine()) {
                fileContentsTextArea.appendText(s.nextLine() + "\n");
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }

    @FXML
    protected void handleFilterChoiceBoxAction(ActionEvent event) {
        boolean isDisabled = false;
        if ("None".equals(filterChoiceBox.getValue())) {
            isDisabled = true;
        }
        colFilterRangeFrom.setDisable(isDisabled);
        colFilterRangeTo.setDisable(isDisabled);
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        Window owner = submitButton.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Warning!",
                    "Twardy dysk stracił połączenie z twardszym dyskiem, a najtwardszy dysk sobie wypadł!");
    }
}
