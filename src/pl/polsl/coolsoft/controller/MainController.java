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
import java.util.Optional;
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
    private TextField cFilterStringValue;
    @FXML
    private Spinner cFilterNumericValueFrom;
    @FXML
    private Spinner cFilterNumericValueTo;
    @FXML
    private Spinner fieldMaxLength;
    @FXML
    private Button submitButton;
    @FXML
    private Button saveNewFilterButton;
    @FXML
    private TextArea fileContentsTextArea;
    @FXML
    private ChoiceBox filterChoiceBox;
    @FXML
    private ChoiceBox stringFilterModeChoiceBox;

    private List<Range> filterRanges;
    private List<Range> columnExportRanges;
    private List<Range> rowExportRanges;

    //This method name must be 'initialize'!
    @SuppressWarnings("unchecked")
    public void initialize() {
        SpinnerValueFactory<Integer> fieldMaxLengthFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 20);
        SpinnerValueFactory<Double> cFilterNumericValueFromFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(-Double.MAX_VALUE, Double.MAX_VALUE, 0);
        SpinnerValueFactory<Double> cFilterNumericValueToFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(-Double.MAX_VALUE, Double.MAX_VALUE, 0);


        filterChoiceBox.setMaxWidth(Double.POSITIVE_INFINITY);
        fieldMaxLength.setMaxWidth(Double.POSITIVE_INFINITY);

        fieldMaxLength.setValueFactory(fieldMaxLengthFactory);
        cFilterNumericValueFrom.setValueFactory(cFilterNumericValueFromFactory);
        cFilterNumericValueTo.setValueFactory(cFilterNumericValueToFactory);
        cFilterRange.setDisable(true);
        cFilterNumericValueFrom.setVisible(false);
        cFilterNumericValueTo.setVisible(false);
        cFilterStringValue.setVisible(false);
        stringFilterModeChoiceBox.setVisible(false);
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
        boolean isVisibleString = false;
        boolean isVisibleNumeric = false;
        if ("Numeric condition".equals(filterChoiceBox.getValue())) {
            isVisibleString = false;
            isVisibleNumeric = true;
        } else if ("String condition".equals(filterChoiceBox.getValue())) {
            isVisibleString = true;
            isVisibleNumeric = false;
        }
        cFilterRange.setDisable(!(isVisibleString || isVisibleNumeric));
        cFilterStringValue.setVisible(isVisibleString);
        stringFilterModeChoiceBox.setVisible(isVisibleString);
        cFilterNumericValueFrom.setVisible(isVisibleNumeric);
        cFilterNumericValueTo.setVisible(isVisibleNumeric);
    }

    @FXML
    protected void handleSaveNewFilterButtonAction(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Saving new filter");
        dialog.setContentText("New filter name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> System.out.println("New filter name: " + name));
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        // Set the ranges
        // TODO(B) walidacja brak krzyżowania się zakresów
        try {

            filterRanges = convertStringToRanges(cFilterRange.getText());
            columnExportRanges = convertStringToRanges(cExportRange.getText());
            rowExportRanges = convertStringToRanges(rExportRange.getText());
        } catch (NumberFormatException e) {
            Window owner = submitButton.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Warning!",
                    "Incorrect range parameter! The format is as follows: X-X;XX-XX;XXX-XXX");
            return;
        }
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
