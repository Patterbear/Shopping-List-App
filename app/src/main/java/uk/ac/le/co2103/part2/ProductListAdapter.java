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

// Product list adapter
public class ProductListAdapter extends ListAdapter<Product, ProductViewHolder> {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String PRODUCT_ID = "uk.ac.le.co2103.part2.PRODUCT_ID";
    public static final String LIST_ID = "ul.ac.le.co2013.part2.LIST_ID";

    public static ProductViewModel productViewModel;
    public static ShoppingListActivity shoppingListActivity;
    public static List<Product> products;
    public static int listId;

    public ProductListAdapter(@NonNull DiffUtil.ItemCallback<Product> diffCallback, ShoppingListActivity shoppingListActivity, String listId) {
        super(diffCallback);
        this.shoppingListActivity = shoppingListActivity;
        this.listId = Integer.parseInt(listId);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ProductViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product current = getItem(position);
        holder.bind(current.getItem());

        productViewModel = new ViewModelProvider(shoppingListActivity).get(ProductViewModel.class);

        productViewModel.getListItems(listId).observe(shoppingListActivity, items -> {
            products = items;
        });

        // OnClick Listener for each item in the recycler view
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            // Dialog appears when list items are clicked
            // Gives them the choice of deleting or editing the product
            @Override
            public void onClick(View view) {
                AlertDialog.Builder deleteOrEditAlert = new AlertDialog.Builder(view.getRootView().getContext());
                deleteOrEditAlert.setMessage("Do you want to delete or edit product " + products.get(holder.getAdapterPosition()).name + "?");
                deleteOrEditAlert.setTitle(R.string.delete_edit_product_title);

                // Delete button
                // Deletes the product if pressed
                deleteOrEditAlert.setNegativeButton(R.string.delete_product_button,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d(TAG, "Deleting product");
                                productViewModel.deleteProduct(products.get(holder.getAdapterPosition()).getProductId());
                            }
                        });

                // Edit button
                // Takes user to UpdateProductActivity if pressed
                deleteOrEditAlert.setPositiveButton(R.string.edit_product_button,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d(TAG, "Moving to update product activity");
                                Intent intent = new Intent(view.getContext(), UpdateProductActivity.class);
                                intent.putExtra(PRODUCT_ID, Integer.toString(products.get(holder.getAdapterPosition()).getProductId()));
                                intent.putExtra(LIST_ID, Integer.toString(listId));
                                view.getContext().startActivity(intent);
                            }
                        });
                deleteOrEditAlert.show();
            }
        });

        // OnLongClick listener for each item in recycler view
        // Just displays a description of the item, pluralises the units if the quantity is more than 1
        // I initialise details as a blank string because it was giving me trouble when I just intialised it on its own
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                String details ="";
                Product product = products.get(holder.getAdapterPosition());
                if (product.quantity == 1) {
                    details = product.quantity + " " + product.unit + " of " + product.name;
                } else {
                    details = product.quantity + " " + product.unit + "s of " + product.name;
                }

                Toast.makeText(view.getContext(), details, Toast.LENGTH_LONG).show();

                return true;
            }
        });
    }

    static class ItemDiff extends DiffUtil.ItemCallback<Product> {

        @Override
        public boolean areItemsTheSame(@NonNull Product oldProduct, @NonNull Product newProduct) {
            return oldProduct == newProduct;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldProduct, @NonNull Product newProduct) {
            return oldProduct.getItem().equals(newProduct.getItem());
        }
    }
}
