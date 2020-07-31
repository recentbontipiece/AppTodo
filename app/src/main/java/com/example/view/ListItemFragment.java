package com.example.view;



import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.Util.Action;
import com.example.model.Item;
import com.example.viewModel.ListViewModel;
import com.example.simpletodoapp.R;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;




public class ListItemFragment extends Fragment {


    private ListViewModel viewModel;
    private String TAG = ListItemFragment.class.getName();

    private ItemsAdapter itemListAdapter;


    @BindView(R.id.rvItems)
    RecyclerView rvItems;


    @BindView(R.id.etItem)
    EditText etItem;


    @BindView(R.id.btnAdd)
    Button btnAdd;

    @Nullable
    @BindView(R.id.itemText)
    TextView itemText;


    private int position;
    private String item;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_item, container, false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);

        btnAdd.setOnClickListener(view1 -> {

            String todoItem = etItem.getText().toString();
            if(todoItem.equalsIgnoreCase("")){
                etItem.setHint(R.string.add_itemHint);
                etItem.setError(getString(R.string.add_itemHint));
            }
            else {
                    Item item     = viewModel.addItem(todoItem);
                    itemListAdapter.addItem(item, viewModel.itemSize());
                etItem.setText("");
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewModel = ViewModelProviders.of(this).get(ListViewModel.class);



        itemListAdapter = new ItemsAdapter(new ArrayList<>(), communication);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItems.setAdapter(itemListAdapter);
        rvItems.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        if (getArguments() !=null) {
            position = ListItemFragmentArgs.fromBundle(getArguments()).getUpdateposition();
            item     = ListItemFragmentArgs.fromBundle(getArguments()).getUpdateitem();
            if (!item.equals("null")){
                viewModel.updateItem(position, item);
            }
        }

        viewModel.loadItems();

        observeViewModel();
    }

    private void observeViewModel(){

        viewModel.mitems.observe(this, items -> {
            if(items !=null && items instanceof List){
                itemListAdapter.updateList(items);
            }
        });
    }

    FragmentCommunication communication = new FragmentCommunication() {
        @Override
        public void action(String action, int position) {
           if (action.equals(Action.DELETE.name())) {
               viewModel.removeItem(position);
               itemListAdapter.removeItem(position);

//               if (viewModel.itemSize() == 0 ) {
//                   itemListAdapter.clearItem();
//               }
            }
        }
    };

}
