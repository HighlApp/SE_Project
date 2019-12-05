package pl.polsl.largetableviewer.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import pl.polsl.largetableviewer.Range;
import pl.polsl.largetableviewer.table.controller.TableController;
import pl.polsl.largetableviewer.table.exception.TableControllerInitializationException;
import pl.polsl.largetableviewer.table.exception.WrongColumnException;
import pl.polsl.largetableviewer.table.exception.WrongRowException;
import pl.polsl.largetableviewer.table.model.Table;
import pl.polsl.largetableviewer.view.AlertHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainController {

    private TableController tableController;

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

    private File inputFile;

    //This method name must be 'initialize'!
    @SuppressWarnings("unchecked")
    public void initialize() throws TableControllerInitializationException {
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
    protected void openFileChooser() throws TableControllerInitializationException {
        Window owner = fileNameField.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Any file extension", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        inputFile = fileChooser.showOpenDialog(owner);
        if (inputFile == null) {
            return;
            //TODO error
        }
        fileNameField.setText(inputFile.toString());
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
    protected void handleSubmitButtonAction(ActionEvent event) throws TableControllerInitializationException, WrongRowException, WrongColumnException {
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
        tableController = new TableController(',', ';', inputFile);
        Table table = tableController.getTable();
        Range fullTableRowRange = new Range(1, table.getNumberOfRows());
        Range fullTableColumnRange = new Range(1, table.getNumberOfColumns());


        tableController.setAllCellsVisibility(false);

        tableController.setRowsAndColumnsVisibility(
                translateRangeToIntegerList(rowExportRanges),
                translateRangeToIntegerList(columnExportRanges),
                true);

        String tableStringRepresentation = tableController.getTableStringRepresentation(',', '\n');
        //%%
        fileContentsTextArea.clear();
        fileContentsTextArea.appendText(tableStringRepresentation);


//        Window owner = submitButton.getScene().getWindow();
//            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Warning!",
//                    "Twardy dysk stracił połączenie z twardszym dyskiem, a najtwardszy dysk sobie wypadł!");
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

    private List<Integer> translateRangeToIntegerList(List<Range> ranges) {
        TreeSet<Integer> entries = new TreeSet<>();
        for (Range range : ranges) {
            entries.addAll(IntStream.rangeClosed(range.getFrom(), range.getTo()).boxed().collect(Collectors.toSet()));
        }
        return new ArrayList<>(entries);
    }
}
