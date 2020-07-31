package com.example.viewModel;



import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.model.Item;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;



public class ListViewModel extends AndroidViewModel {


    private String TAG = ListViewModel.class.getName();



    List<String> items;

    public MutableLiveData<List<Item>> mitems  = new MutableLiveData<List<Item>>();


    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    private File getDataFile() {
        return new File(getApplication().getFilesDir(),"data.text");
    }

    public void loadItems(){
        try {
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
            ArrayList<Item> listItem = new ArrayList<Item>();
            for(int i = 0; i<items.size();i++){
                listItem.add(new Item(i,items.get(i)));
            }


            if(listItem.size()>0){
                mitems.setValue(listItem);
            }

        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    //This function saves items by writing them in the data file
    public void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error writing items",e);
        }
    }

    public void removeItem(int position) {
       items.remove(position);
       saveItems();
        Log.i(TAG, "Remove Item "+position + "Item Size "+items.size());

    }

    public int itemSize(){
        return items.size();
    }

    public List<String> getItems(){
        return items;
    }

    public String getItem(int position){
        return items.get(position);
    }

    public Item  addItem(String item){
      items.add(item);
      saveItems();
      return  new Item(items.size()-1, item);
    }

    public void updateItem(int position, String item){
        loadItems();
        items.set(position, item);
        saveItems();
    }
}
