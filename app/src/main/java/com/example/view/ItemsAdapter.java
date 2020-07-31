package com.example.view;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Util.Action;
import com.example.utilc.PositionHandle;
import com.example.model.Item;
import java.util.List;
import com.example.simpletodoapp.R;
import com.example.simpletodoapp.databinding.ListItemBinding;





public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> implements ItemClickListItemListener, ItemLongClickListener {

    private String TAG = ItemsAdapter.class.getName();
    private List<Item> itemList;

    FragmentCommunication mComminication;

    public ItemsAdapter(List<Item> itemList, FragmentCommunication mComminication){
      this.itemList = itemList;
      this.mComminication  = mComminication;
    }


    public void updateList(List<Item> newItemsList){
        itemList.clear();
        itemList.addAll(newItemsList);
        notifyDataSetChanged();
    }

    public void removeItem(int position)
    {
        //if (position >= itemList.size()) return;


        itemList.remove(position);
        Log.i(TAG, "Adapter Remove Item "+position + "Item Size "+itemList.size());

//        notifyItemRemoved(position);
        notifyDataSetChanged();

    }

    public void addItem(Item item, int position)
    {
        itemList.add(item);
        Log.i(TAG, "Insert Position "+position + "Item Size "+itemList.size());
        //notifyItemInserted(itemList.size() - 1);

        notifyDataSetChanged();
    }


    public void clearItem()
    {
      itemList.clear();
    }


    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemBinding view = DataBindingUtil.inflate(inflater, R.layout.list_item,parent, false);

       // view.setPosition();
        Log.i(TAG, "Are you call One ");


        return new ItemsViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
      PositionHandle positionHandle  = new PositionHandle();
        positionHandle.setPosition(position);
        holder.itemView.setPosition(positionHandle);
      holder.itemView.setItem(itemList.get(position));
      holder.itemView.setListener(this);
      holder.itemView.setListener2(this);
    }

    @Override
    public int getItemCount() {
       return itemList.size();
    }


    @Override
    public void onItemClick(View view, PositionHandle position) {
        ListItemFragmentDirections.ActionEdit action = ListItemFragmentDirections.actionEdit();
        action.setItem(itemList.get(Integer.valueOf(position.position)).itemText);
        action.setPosition(Integer.valueOf(position.position));
        Navigation.findNavController(view).navigate(action);
    }

    @Override
    public boolean onLongClick(View view, PositionHandle position) {
        mComminication.action(Action.DELETE.name(),Integer.valueOf(position.position));
        return true;
    }


    class ItemsViewHolder extends RecyclerView.ViewHolder {

       private ListItemBinding itemView;

       public ItemsViewHolder(@NonNull  ListItemBinding itemView) {
         super(itemView.getRoot());
         this.itemView = itemView;
       }
    }
}
