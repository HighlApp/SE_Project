package pl.polsl.coolsoft.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import pl.polsl.coolsoft.view.AlertHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainController {
    @FXML
    private TextField fileNameField;
    @FXML
    private TextField cExportRange;
    @FXML
    private TextField rExportRange;
    @FXML
    private TextField cFilterRange;
    @FXML
    private Spinner fieldMaxLength;
    @FXML
    private Button submitButton;
    @FXML
    private TextArea fileContentsTextArea;
    @FXML
    private ChoiceBox filterChoiceBox;

    private List<Range> filterRanges;
    private List<Range> columnExportRanges;
    private List<Range> rowExportRanges;

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


        fieldMaxLength.setValueFactory(fieldMaxLengthFactory);
        cFilterRange.setDisable(true);

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
        cFilterRange.setDisable(isDisabled);
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        // Set the ranges
        // TODO(B) walidacja brak krzyżowania się zakresów
        filterRanges = convertStringToRanges(cFilterRange.getText());
        columnExportRanges = convertStringToRanges(cExportRange.getText());
        rowExportRanges = convertStringToRanges(rExportRange.getText());

        //FIXME
        Window owner = submitButton.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Warning!",
                    "Twardy dysk stracił połączenie z twardszym dyskiem, a najtwardszy dysk sobie wypadł!");
    }

    private List<Range> convertStringToRanges(String inputString) {
        List<Range> ranges = new ArrayList<>();
        String[] strRanges = inputString.replace(" ", "").split(";");
        for (String strRange : strRanges) {
            if ("".equals(strRange)) continue;
            String[] ends = strRange.split("-");
            ranges.add(new Range(Integer.parseInt(ends[0]), Integer.parseInt(ends[1])));
        }
        return ranges;
    }
}
