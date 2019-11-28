package pl.polsl.coolsoft.filter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FilterView {

    private final String fileName = "./filterList";

    public void saveFilter(FilterModel filterModel) {

        ArrayList<FilterModel> filterList;

        if(!doesFileExist())
            filterList = new ArrayList<>();
        else
            filterList = (ArrayList<FilterModel>) getFilterList(); //load list to check its size

        if(filterList.size() >= 10) //if greater than 10, remove first saved filter and add current one
            filterList.remove(0);

        filterList.add(filterModel);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("filterList");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(filterList);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<FilterModel> getFilterList() {

        ArrayList<FilterModel> filterList = new ArrayList<>();

        if (!doesFileExist())
            return filterList; // if file doesn't exist return simply blank list

        try {
            FileInputStream fileInputStream = new FileInputStream("filterList");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            filterList = (ArrayList) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
            e.printStackTrace();
        }

        //Verify list data
        for (FilterModel model : filterList) {
            System.out.println(model);
        }

        return filterList;
    }

    private boolean doesFileExist() {
        File tmpDir = new File(fileName);
        return tmpDir.exists();
    }


    //wczytywanie filtrow z pliku, jesli nie ma to nic
    //uzytkownik klika zapisz filtr, zapisuje ostatnie zmiany do pliku

}
