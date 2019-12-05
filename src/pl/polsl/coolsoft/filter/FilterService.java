package pl.polsl.coolsoft.filter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FilterService {

    private final String fileName = "./filterList";
    private FilterModel activeModel;
    private List<FilterModel> filterList;

    public FilterService() {
        filterList = new ArrayList<>();
    }

    public void saveFilter(FilterModel filterModel) throws FilterException {

        filterList = doesFileExist() ? (ArrayList<FilterModel>) getFilterList() : filterList;

        checkIfExceededTenRecords(filterList);

        if (nameAlreadyExists(filterList, filterModel)) {
            throw new FilterException("Name already exists!");
        }

        filterList.add(filterModel);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("filterList");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(filterList);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            throw new FilterException("Error while saving object! " + e.getMessage());
        }
    }

    public List<FilterModel> getFilterList() throws FilterException {

        if (!doesFileExist())
            return filterList; // if file doesn't exist return simply blank list

        else {
            try {
                FileInputStream fileInputStream = new FileInputStream("filterList");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                filterList = (List<FilterModel>) objectInputStream.readObject();

                objectInputStream.close();
                fileInputStream.close();

                setActiveModel(filterList);

                return filterList;

            } catch (IOException e) {
                throw new FilterException("Error while IO operation! " + e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new FilterException("Class not found" + e.getMessage());
            }
        }

    }

    public FilterModel getModelByName(String name) {
        return filterList.stream()
                .filter(filter -> filter.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public void deleteFilter(String name) {
        filterList.removeIf(filter -> filter.getName().equals(name));
    }

    public FilterModel getActiveModel() {
        return activeModel;
    }

    private boolean nameAlreadyExists(List<FilterModel> filterList, FilterModel filterModel) {
        return filterList.stream().anyMatch(filter -> filter.getName().equals(filterModel.getName()));
    }

    private void checkIfExceededTenRecords(List<FilterModel> filterList) {
        if(filterList.size() >= 10) //if greater than 10, remove first saved filter and add current one
            filterList.remove(0);
    }

    private void setActiveModel(List<FilterModel> filterList) {
        this.activeModel = filterList.get(filterList.size() - 1);
    }

    private boolean doesFileExist() {
        File tmpDir = new File(fileName);
        return tmpDir.exists();
    }

}
