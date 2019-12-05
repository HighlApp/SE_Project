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
    private TextField colSeparator;
    @FXML
    private TextField rowSeparator;
    @FXML
    private TextField cExportRange;
    @FXML
    private TextField rExportRange;
    @FXML
    private TextField cFilterRange;
    @FXML
    private TextField cFilterStringValue;
    @FXML
    private Spinner fieldMaxLength;
    @FXML
    private Button submitButton;
    @FXML
    private Button saveNewFilterButton;
    @FXML
    private TextArea fileContentsTextArea;

    private List<Range> filterRanges;
    private List<Range> columnExportRanges;
    private List<Range> rowExportRanges;

    private File inputFile;

    //This method name must be 'initialize'!
    @SuppressWarnings("unchecked")
    public void initialize() {
        SpinnerValueFactory<Integer> fieldMaxLengthFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 20);
        SpinnerValueFactory<Double> cFilterNumericValueFromFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(-Double.MAX_VALUE, Double.MAX_VALUE, 0);
        SpinnerValueFactory<Double> cFilterNumericValueToFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(-Double.MAX_VALUE, Double.MAX_VALUE, 0);


        fieldMaxLength.setMaxWidth(Double.POSITIVE_INFINITY);
        fieldMaxLength.setValueFactory(fieldMaxLengthFactory);
//        cFilterRange.setDisable(true);
//        cFilterStringValue.setVisible(false);
//        stringFilterModeChoiceBox.setVisible(false);
    }

    @FXML
    protected void openFileChooser() {
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

//    @FXML
//    protected void handleFilterChoiceBoxAction(ActionEvent event) {
//        boolean isVisibleString = false;
//        boolean isVisibleNumeric = false;
//        if ("Numeric condition".equals(filterChoiceBox.getValue())) {
//            isVisibleString = false;
//            isVisibleNumeric = true;
//        } else if ("String condition".equals(filterChoiceBox.getValue())) {
//            isVisibleString = true;
//            isVisibleNumeric = false;
//        }
//        cFilterRange.setDisable(!(isVisibleString || isVisibleNumeric));
//        cFilterStringValue.setVisible(isVisibleString);
//        stringFilterModeChoiceBox.setVisible(isVisibleString);
//    }

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
        tableController = new TableController(
                colSeparator.getText().charAt(0),
                rowSeparator.getText().charAt(0),
                inputFile);

        Table table = tableController.getTable();
        List<Range> fullTableRowRange = Collections.singletonList(new Range(1, table.getNumberOfRows()));
        List<Range> fullTableColRange = Collections.singletonList(new Range(1, table.getNumberOfColumns()));

        tableController.setAllCellsVisibility(false); //reset all
        tableController.setRowsAndColumnsVisibility(
                translateRangeToIntegerList(rowExportRanges.isEmpty() ? fullTableRowRange : rowExportRanges),
                translateRangeToIntegerList(columnExportRanges.isEmpty() ? fullTableColRange : columnExportRanges),
                true);
        if (!"".equals(cFilterStringValue.getText())) {
            tableController.sequenceSearch(cFilterStringValue.getText(),
                    translateRangeToIntegerList(fullTableRowRange),
                    translateRangeToIntegerList(filterRanges));
        }

        String tableStringRepresentation = tableController.getTableStringRepresentation(colSeparator.getText().charAt(0), '\n');
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
            ranges.add(new Range(Integer.parseInt(ends[0]),
                    (ends.length == 2) ? Integer.parseInt(ends[1]) : Integer.parseInt(ends[0])));
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
