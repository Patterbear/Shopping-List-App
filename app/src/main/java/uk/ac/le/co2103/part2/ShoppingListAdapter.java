package uk.ac.le.co2103.part2;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.List;

// Class for shopping list adapter
public class ShoppingListAdapter extends ListAdapter<ShoppingList, ShoppingListViewHolder> {
    public static final String LIST_ID = "uk.ac.le.co2103.part2.LIST_ID";

    private static final String TAG = MainActivity.class.getSimpleName();

    private ShoppingListViewModel shoppingListViewModel;
    private ProductViewModel productViewModel;
    public static MainActivity mainActivity;
    public static List<ShoppingList> shoppingLists;

    public ShoppingListAdapter(@NonNull DiffUtil.ItemCallback<ShoppingList> diffCallback, MainActivity mainActivity) {
        super(diffCallback);
        this.mainActivity = mainActivity;

    }

    @Override
    public ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ShoppingListViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ShoppingListViewHolder holder, int position) {
        ShoppingList current = getItem(position);
        holder.bind(current.getShoppingList());

        // For some reason due to the emulator storage, no images can be found at their paths
        /*
        if (!(current.getImageUri() == null)) {
            holder.bindImage(current.getImageUri());
            Log.d("DEBUG" , current.getImageUri());
        }
        */


        // OnClick listener for clicking on list items
        // Starts the shopping list activity and gives it the listId
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ShoppingListActivity.class);

                shoppingListViewModel = new ViewModelProvider(mainActivity).get(ShoppingListViewModel.class);
                shoppingListViewModel.getAllItems().observe(mainActivity, items -> {
                    shoppingLists = items;
                });

                intent.putExtra(LIST_ID, Integer.toString(shoppingLists.get(holder.getAdapterPosition()).getListId()));

                view.getContext().startActivity(intent);
            }
        });

        // OnLongClick listener for list items
        // Gives the user the option to delete lists
        // Cascade deletes any products belonging to the list
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                shoppingListViewModel = new ViewModelProvider(mainActivity).get(ShoppingListViewModel.class);
                productViewModel = new ViewModelProvider(mainActivity).get(ProductViewModel.class);

                shoppingListViewModel.getAllItems().observe(mainActivity, items -> {
                    shoppingLists = items;
                });

                Log.d(TAG, "Creating delete list dialog box");
                AlertDialog.Builder deleteAlert = new AlertDialog.Builder(view.getRootView().getContext());
                deleteAlert.setMessage("Do you want to delete Shopping List " + shoppingLists.get(holder.getAdapterPosition()).name + "?");
                deleteAlert.setTitle(R.string.delete_list_title);

                // 'Yes' button causes selected list and its products to be deleted
                deleteAlert.setPositiveButton(R.string.button_yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d(TAG, "Cascade deleting child products");
                                productViewModel.deleteProductsById(shoppingLists.get(holder.getAdapterPosition()).getListId());
                                Log.d(TAG, "Deleting shopping list");
                                shoppingListViewModel.deleteList(shoppingLists.get(holder.getAdapterPosition()).getListId());
                            }
                        });
                // 'No' button returns user to the shopping list
                deleteAlert.setNegativeButton(R.string.button_no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d(TAG, "Returning to shopping list");
                            }
                        });
                deleteAlert.show();


                return true;
            }
        });
    }

    static class ItemDiff extends DiffUtil.ItemCallback<ShoppingList> {

        @Override
        public boolean areItemsTheSame(@NonNull ShoppingList oldShoppingList, @NonNull ShoppingList newShoppingList) {
            return oldShoppingList == newShoppingList;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ShoppingList oldShoppingList, @NonNull ShoppingList newShoppingList) {
            return oldShoppingList.name.equals(newShoppingList.name);
        }
    }
}
