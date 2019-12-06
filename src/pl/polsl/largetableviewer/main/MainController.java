package pl.polsl.largetableviewer.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import pl.polsl.largetableviewer.filter.FilterException;
import pl.polsl.largetableviewer.filter.FilterModel;
import pl.polsl.largetableviewer.filter.FilterService;
import pl.polsl.largetableviewer.view.Range;
import pl.polsl.largetableviewer.table.controller.TableController;
import pl.polsl.largetableviewer.table.exception.TableControllerInitializationException;
import pl.polsl.largetableviewer.table.exception.WrongColumnException;
import pl.polsl.largetableviewer.table.exception.WrongRowException;
import pl.polsl.largetableviewer.table.model.Row;
import pl.polsl.largetableviewer.table.model.Table;
import pl.polsl.largetableviewer.view.AlertHelper;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainController {

    private TableController tableController;
    private FilterService filterService;

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
    private Spinner<Integer> fieldMaxLength;
    @FXML
    private Button submitButton;
    @FXML
    private Button saveNewFilterButton;
    @FXML
    private TextArea fileContentsTextArea;
    @FXML
    private ChoiceBox<String> selectSavedFilterChoiceBox;
    @FXML
    private CheckBox transposedCheckBox;

    private List<Range> filterRanges;
    private List<Range> columnExportRanges;
    private List<Range> rowExportRanges;

    private File inputFile;

    //This method name must be 'initialize'!
    @SuppressWarnings("unchecked")
    public void initialize() throws FilterException {
        filterService = new FilterService();
        List<FilterModel> filterList = filterService.getFilterList();
        for (FilterModel filter : filterList) {
            selectSavedFilterChoiceBox.getItems().add(filter.getName());
        }

        SpinnerValueFactory<Integer> fieldMaxLengthFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 20);

        fieldMaxLength.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                fieldMaxLength.increment(0);
            }
        });

        fieldMaxLength.setMaxWidth(Double.POSITIVE_INFINITY);
        fieldMaxLength.setValueFactory(fieldMaxLengthFactory);
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

    @FXML
    protected void handleSaveNewFilterButtonAction(ActionEvent event) {
        Window owner = saveNewFilterButton.getScene().getWindow();
        if ("".equals(colSeparator.getText()) || "".equals(rowSeparator.getText())) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Error!",
                    "You must specify separators!");
            return;
        } else if (colSeparator.getText().length() > 1 || rowSeparator.getText().length() > 1) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Error!",
                    "Separator must be a single character!");
            return;
        }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Saving new filter");
        dialog.setContentText("New filter name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            System.out.println("New filter name: " + name);
            FilterModel filterModel = new FilterModel();
            filterModel.setName(name);
            filterModel.setColumnSeparator(colSeparator.getText().charAt(0));
            filterModel.setRowSeparator(rowSeparator.getText().charAt(0));
            filterModel.setColumnExportRange(cExportRange.getText());
            filterModel.setRowExportRange(rExportRange.getText());
            filterModel.setFieldMaxLength(fieldMaxLength.getValue());
            filterModel.setTransposed(transposedCheckBox.isSelected());
            try {
                filterService.saveFilter(filterModel);
                selectSavedFilterChoiceBox.getItems().add(name);
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Information",
                    "Filter " + name + " has been saved successfuly!");
            } catch (FilterException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    protected void handleFiterSelectionAction(ActionEvent event) {
        FilterModel filter = filterService.getModelByName(selectSavedFilterChoiceBox.getValue());
        colSeparator.setText(String.valueOf(filter.getColumnSeparator()));
        rowSeparator.setText(String.valueOf(filter.getRowSeparator()));
        cExportRange.setText(String.valueOf(filter.getColumnExportRange()));
        rExportRange.setText(String.valueOf(filter.getRowExportRange()));
        fieldMaxLength.getValueFactory().setValue(filter.getFieldMaxLength());
        transposedCheckBox.setSelected(filter.isTransposed());
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) throws TableControllerInitializationException, WrongRowException, WrongColumnException {
        mapRanges();
        performTransformations();
        //TODO zapis do pliku
//        Window owner = submitButton.getScene().getWindow();
//            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Warning!",
//                    "Twardy dysk stracił połączenie z twardszym dyskiem, a najtwardszy dysk sobie wypadł!");
    }

    @FXML
    protected void handleRefreshButtonAction(ActionEvent event) throws TableControllerInitializationException, WrongColumnException, WrongRowException {
        mapRanges();
        performTransformations();
        String previewString = tableController.getTableStringRepresentation(
                colSeparator.getText().charAt(0), '\n',
                (Integer)fieldMaxLength.getValue(), 20);
//        String tableStringRepresentation = tableController.getTableStringRepresentation(
//                colSeparator.getText().charAt(0), '\n', (Integer)fieldMaxLength.getValue());
//        tableStringRepresentation.
//        Table table = tableController.getTable();
//        StringBuilder previewString = new StringBuilder();
//        int counter = 20;
//        for (Row row : table.getRows()) {
//            if (row.isVisible()) {
//                for (Cell cell : row.getCells()) {
//                    if (cell.isVisible()) {
//                        previewString.append(cell.getContent()).append(" | ");
//                    }
//                }
//            }
//            if (--counter <= 0) break;
//            previewString.append("\n");
//        }
        fileContentsTextArea.clear();
        fileContentsTextArea.appendText(previewString);
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

    private void performTransformations() throws TableControllerInitializationException, WrongRowException, WrongColumnException {
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

        if (transposedCheckBox.isSelected()) {
            for (Row row : table.getRows()) {
                if (row.isVisible()) {
                    tableController.transposeRow(row.getRowNumber());
                }
            }
        }
    }

    private void mapRanges() {
        try {
            filterRanges = convertStringToRanges(cFilterRange.getText());
            columnExportRanges = convertStringToRanges(cExportRange.getText());
            rowExportRanges = convertStringToRanges(rExportRange.getText());
        } catch (NumberFormatException e) {
            Window owner = submitButton.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Warning!",
                    "Incorrect range parameter! The format is as follows: X-X;XX-XX;XXX-XXX");
        }
    }
}
