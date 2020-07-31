package com.example.view;




import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.model.Item;
import com.example.simpletodoapp.R;
import com.example.simpletodoapp.databinding.FragmentEditBinding;
import com.example.viewModel.EditViewModel;


public class EditFragment extends Fragment implements ItemClickListener {


    private EditViewModel viewModel;
    private int position;
    private String item;
    private FragmentEditBinding binding;



    private String TAG = EditFragment.class.getName();


    public EditFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit, container, false);

        this.binding.setListener(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() !=null) {
             position = EditFragmentArgs.fromBundle(getArguments()).getPosition();
             item     = EditFragmentArgs.fromBundle(getArguments()).getItem();
        }

        setHasOptionsMenu(true);

        viewModel = ViewModelProviders.of(this).get(EditViewModel.class);
        viewModel.showItem(position, item);

        updateText();
    }

    public void updateText(){
        viewModel.item.observe(this, item1 -> {
            if (item1 !=null && item1 instanceof Item && getContext() !=null){
                binding.setItem(item1);
            }
        });
    }

    @Override
    public void onItemClick(View view) {
        String  updateItem = this.binding.etItem.getText().toString().trim();

        if(updateItem.equalsIgnoreCase("")){
             this.binding.etItem.setError(getString(R.string.edit_txtHint));
             this.binding.etItem.setHint(R.string.edit_txtHint);

        }else {
            EditFragmentDirections.ActionListFragment action =  EditFragmentDirections.actionListFragment();
            action.setUpdateitem(updateItem);
            action.setUpdateposition(position);
            Navigation.findNavController(view).navigate(action);
        }

    }
}
