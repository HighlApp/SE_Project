package pl.polsl.largetableviewer.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import pl.polsl.largetableviewer.filter.FilterException;
import pl.polsl.largetableviewer.filter.FilterModel;
import pl.polsl.largetableviewer.filter.FilterService;
import pl.polsl.largetableviewer.table.controller.TableController;
import pl.polsl.largetableviewer.table.exception.TableControllerInitializationException;
import pl.polsl.largetableviewer.table.exception.WrongColumnException;
import pl.polsl.largetableviewer.table.exception.WrongRowException;
import pl.polsl.largetableviewer.table.model.Row;
import pl.polsl.largetableviewer.table.model.Table;
import pl.polsl.largetableviewer.view.AlertHelper;
import pl.polsl.largetableviewer.view.Range;

import javax.xml.bind.ValidationException;
import java.io.File;
import java.io.IOException;
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
    private TextField outputFilePath;
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
    @FXML
    private CheckBox newlineCheckBox;
    @FXML
    private CheckBox tabCheckBox;
    @FXML
    private CheckBox colTabCheckBox;

    private List<Range> filterRanges;
    private List<Range> columnExportRanges;
    private List<Range> rowExportRanges;

    private File inputFile;
    private boolean isCheckboxSelected;

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
        isCheckboxSelected = false;

        FilterModel activeModel = filterService.getActiveModel();
        if (activeModel != null) {
            applyFilter(activeModel);
        }
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
        }
        fileNameField.setText(inputFile.toString());
    }

    @FXML
    protected void handleColSeparatorCheckAction(ActionEvent event) {
        colSeparator.clear();
        if (colTabCheckBox.isSelected()) {
            colSeparator.setDisable(true);
        } else {
            colSeparator.setDisable(false);
        }
    }


    @FXML
    protected void handleSeparatorCheckAction(ActionEvent event) {
        rowSeparator.clear();
        final Node source = (Node) event.getSource();
        String id = source.getId();
        if (newlineCheckBox.getId().equals(id) && newlineCheckBox.isSelected()) {
            rowSeparator.setDisable(true);
            tabCheckBox.setSelected(false);
            isCheckboxSelected = true;
        } else if (tabCheckBox.getId().equals(id) && tabCheckBox.isSelected()){
            rowSeparator.setDisable(true);
            newlineCheckBox.setSelected(false);
            isCheckboxSelected = true;
        } else {
            rowSeparator.setDisable(false);
            isCheckboxSelected = false;
        }
    }

    @FXML
    protected void handleSaveNewFilterButtonAction(ActionEvent event) {
        Window owner = saveNewFilterButton.getScene().getWindow();
        if ("".equals(colSeparator.getText()) && !colTabCheckBox.isSelected() || ("".equals(rowSeparator.getText()) && !isCheckboxSelected)) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Error!",
                    "You must specify separators!");
            return;
        } else if (!colTabCheckBox.isSelected() && colSeparator.getText().length() > 1 || (!isCheckboxSelected && rowSeparator.getText().length() > 1)) {
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
            if (!colTabCheckBox.isSelected()) {
                filterModel.setColumnSeparator(colSeparator.getText().charAt(0));
            }
            if (!isCheckboxSelected) {
                filterModel.setRowSeparator(rowSeparator.getText().charAt(0));
            }
            filterModel.setColumnExportRange(cExportRange.getText());
            filterModel.setRowExportRange(rExportRange.getText());
            filterModel.setFieldMaxLength(fieldMaxLength.getValue());
            filterModel.setTransposed(transposedCheckBox.isSelected());
            filterModel.setSearchExpression(cFilterStringValue.getText());
            filterModel.setSearchRange(cFilterRange.getText());
            filterModel.setNewline(newlineCheckBox.isSelected());
            filterModel.setTab(tabCheckBox.isSelected());
            filterModel.setColTab(colTabCheckBox.isSelected());
            try {
                filterService.saveFilter(filterModel);
                selectSavedFilterChoiceBox.getItems().add(name);
                selectSavedFilterChoiceBox.setValue(name);
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
        applyFilter(filter);
    }

    private void applyFilter(FilterModel filter) {
        if (!filter.isColTab()) {
            colSeparator.setText(String.valueOf(filter.getColumnSeparator()));
        } else {
            colSeparator.clear();
        }
        if (!filter.isNewline() && !filter.isTab()) {
            rowSeparator.setText(String.valueOf(filter.getRowSeparator()));
        } else {
            rowSeparator.clear();
        }
        cExportRange.setText(String.valueOf(filter.getColumnExportRange()));
        rExportRange.setText(String.valueOf(filter.getRowExportRange()));
        fieldMaxLength.getValueFactory().setValue(filter.getFieldMaxLength());
        transposedCheckBox.setSelected(filter.isTransposed());
        cFilterStringValue.setText(filter.getSearchExpression());
        cFilterRange.setText(filter.getSearchRange());
        newlineCheckBox.setSelected(filter.isNewline());
        tabCheckBox.setSelected(filter.isTab());
        colTabCheckBox.setSelected(filter.isColTab());

        if(newlineCheckBox.isSelected() || tabCheckBox.isSelected()) {
            rowSeparator.setDisable(true);
        } else {
            rowSeparator.setDisable(false);
        }
        if(colTabCheckBox.isSelected()) {
            colSeparator.setDisable(true);
        } else {
            colSeparator.setDisable(false);
        }
        selectSavedFilterChoiceBox.setValue(filter.getName());
        isCheckboxSelected = true; //we assume it was correctly saved
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) throws TableControllerInitializationException, WrongRowException, WrongColumnException, ValidationException {
        processRequest();
        Window owner = submitButton.getScene().getWindow();
        char rSeparator = allotSeparator();


        try {
            if (!outputFilePath.getText().contains(File.separator)) {
                outputFilePath.setText(inputFile.getParent() + File.separator + outputFilePath.getText());
            }
            tableController.exportTable(
                    colTabCheckBox.isSelected() ? '\t' : colSeparator.getText().charAt(0),
                    rSeparator,
                    fieldMaxLength.getValue(), outputFilePath.getText());
        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Error!",
                    "An error occured during file export!");
            return;
        }
        AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Info",
                "File exported successfuly!");

    }

    private char allotSeparator() {
        char rSeparator;
        if (!isCheckboxSelected) {
            rSeparator = rowSeparator.getText().charAt(0);
        } else if (newlineCheckBox.isSelected()) {
            rSeparator = '\n';
        } else { //tab selected
            rSeparator = '\t';
        }
        return rSeparator;
    }

    private void validateSettings() throws ValidationException {
        String msg = null;
        if (inputFile == null) {
            msg = "You have to specify input file!";
        } else if (outputFilePath.getText().isEmpty()) {
            msg = "You have to specify output file path!";
        } else if (!colTabCheckBox.isSelected() && colSeparator.getText().isEmpty()) {
            msg = "You have to specify column separator!";
        } else if (!isCheckboxSelected && rowSeparator.getText().isEmpty()) {
            msg = "You have to specify row separator!";
        } else if (!cFilterStringValue.getText().isEmpty() && cFilterRange.getText().isEmpty()) {
            msg = "You have to specify columns for search filter!";
        }
        if (msg != null) {
            Window owner = outputFilePath.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Validation error!", msg);
            throw new ValidationException("Incorrect settings specified by user!");
        }
    }

    private void processRequest() throws TableControllerInitializationException, WrongRowException, WrongColumnException, ValidationException {
        validateSettings();
        mapRanges();
        performTransformations();
    }

    @FXML
    protected void handleRefreshButtonAction(ActionEvent event) throws TableControllerInitializationException, WrongColumnException, WrongRowException, ValidationException {
        processRequest();
        String previewString = tableController.getTableStringRepresentation(
                colTabCheckBox.isSelected() ? '\t' : colSeparator.getText().charAt(0),
                '\n',
                fieldMaxLength.getValue(), 20, 200);
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

    private List<Integer> determineEntriesToHide(List<Integer> intEntries, List<Range> fullTableRange) {
        if (intEntries.isEmpty()) {
            return new ArrayList<>();
        }
        TreeSet<Integer> entries = new TreeSet<>(IntStream.rangeClosed(fullTableRange.get(0).getFrom(), fullTableRange.get(0).getTo()).boxed().collect(Collectors.toSet()));
        entries.removeAll(intEntries);
        return new ArrayList<>(entries);
    }

    private void performTransformations() throws TableControllerInitializationException, WrongRowException, WrongColumnException {
        char rSeparator = allotSeparator();
        tableController = new TableController(colTabCheckBox.isSelected() ? '\t' : colSeparator.getText().charAt(0),
                rSeparator, inputFile);

        Table table = tableController.getTable();
        List<Range> fullTableRowRange = Collections.singletonList(new Range(1, table.getNumberOfRows()));
        List<Range> fullTableColRange = Collections.singletonList(new Range(1, table.getNumberOfColumns()));

//        tableController.setAllCellsVisibility(false); //reset all
        try {
            List<Integer> rowIntegers = translateRangeToIntegerList(rowExportRanges);
            List<Integer> colIntegers = translateRangeToIntegerList(columnExportRanges);

            if (!"".equals(cFilterStringValue.getText())) {
                tableController.sequenceSearch(cFilterStringValue.getText(),
                        translateRangeToIntegerList(fullTableRowRange),
                        translateRangeToIntegerList(filterRanges));
            }

            tableController.setRowsAndColumnsVisibility(
                    determineEntriesToHide(rowIntegers, fullTableRowRange),
                    determineEntriesToHide(colIntegers, fullTableColRange),
                    false);


        } catch (WrongRowException | WrongColumnException e) {
            Window owner = outputFilePath.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Error!", "The range specified is too wide");
            return;
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
