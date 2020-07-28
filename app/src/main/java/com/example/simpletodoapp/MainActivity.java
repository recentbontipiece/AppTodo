package com.example.simpletodoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;





public class MainActivity extends AppCompatActivity  {

    public static final String KEY_ITEM_TEXT     = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE    = 20;


    private String TAG = MainActivity.class.getName();

    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;

    ItemsAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd  = findViewById(R.id.btnAdd);
        etItem  = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);


        loadItems();

        ItemsAdapter.OnClickListener onClickListener  = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent editActivity = new Intent(MainActivity.this, EditActivity.class);
                editActivity.putExtra(KEY_ITEM_TEXT,items.get(position));
                editActivity.putExtra(KEY_ITEM_POSITION,position);
                startActivityForResult(editActivity,EDIT_TEXT_CODE);
            }
        };

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {

                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                saveItems();
                Toast.makeText(getApplicationContext(), "Items was removed", Toast.LENGTH_SHORT).show();
            }
        };

        itemsAdapter  =  new ItemsAdapter(items, onLongClickListener,onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager( new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String todoItem = etItem.getText().toString();
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");
                saveItems();
                Toast.makeText(getApplicationContext(), "Items was added", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Handle Result of the edit Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            //Retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //extract the origial position of the edited item
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //Update the model at the right position with new item text
            items.set(position, itemText);
            //notify the adpater
            itemsAdapter.notifyItemChanged(position);
            //persist the changes
            saveItems();

            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();


        } else {
            Log.w(TAG, "Unknown call to onActivityResult");
        }
    }

    private File getDataFile() {
      return new File(getFilesDir(),"data.text");
    }

    //This function will load utems by reading every line of the data line
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e(TAG, "Error reading items",e);
            items = new ArrayList<>();
        }
    }

    //This function saves items by wrting them ubti the data file
    private void saveItems(){
        try {
          FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error writing items",e);
        }
    }
}
