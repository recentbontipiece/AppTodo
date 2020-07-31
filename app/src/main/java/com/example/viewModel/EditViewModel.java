package com.example.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.model.Item;


public class EditViewModel extends AndroidViewModel {

    public MutableLiveData<Item> item = new MutableLiveData<Item>();


    public EditViewModel(@NonNull Application application) {
        super(application);
    }

    public void showItem(int position, String itemtext){
        item.setValue(new Item(position,itemtext));
    }
}
