package uk.ac.le.co2103.part2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Activity for the shopping list screen
// Displays all of the products in the chosen shopping list
// Allows user to add, update or delete products
public class ShoppingListActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ProductViewModel productViewModel;

    public static List<Product> listProducts;
    public static final int ADD_ITEM_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_shopping_list);




        Log.d(TAG, "Creating recyclerView");
        RecyclerView recyclerView = findViewById(R.id.recyclerviewProduct);
        final ProductListAdapter adapter = new ProductListAdapter(new ProductListAdapter.ItemDiff(), this, getIntent().getStringExtra(ShoppingListAdapter.LIST_ID));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Log.d(TAG, "Adding items to adapter");
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);


        productViewModel.getListItems(Integer.parseInt(getIntent().getStringExtra(ShoppingListAdapter.LIST_ID))).observe(this, items -> {
            listProducts = items;
            adapter.submitList(items);
        });



        // My android studio tells me the 'startActivityForResult' method is deprecated
        // I used it here because its what we used in the lab, it works anyway
        Log.d(TAG, "Setting up floating action button");
        FloatingActionButton fabAddProduct = findViewById(R.id.fabAddProduct);
        fabAddProduct.setOnClickListener( view -> {
            Intent intent = new Intent(ShoppingListActivity.this, AddProductActivity.class);
            startActivityForResult(intent, ADD_ITEM_ACTIVITY_REQUEST_CODE);
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the result is ok, the new list has its name checked
        // If the name has been used in another list a toast message appears
        // If the name is unique and new then it adds the list to the database
        if (requestCode == ADD_ITEM_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d(TAG, "Checking new list name");
            String testNamesString = "";

            // Again I had to use a string and then convert it to a list because of odd errors
            // Works fine though
            for(int i=0; i < listProducts.size(); i++) {
                testNamesString = testNamesString + listProducts.get(i).name + ",";
            }

            // Removes final comma
            if (testNamesString.length() != 0) {
                testNamesString = testNamesString.substring(0, testNamesString.length()-1);
            }

            // Creates list from the string and uses commas to split it
            List<String> testNamesList = new ArrayList<String>(Arrays.asList(testNamesString.split(",")));

            if (testNamesList.contains(data.getStringExtra(AddProductActivity.EXTRA_NAME))) {
                Log.d(TAG, "Product already exists.");
                Toast.makeText(getApplicationContext(), "Product already exists", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "Adding Product with name " + getIntent().getStringExtra(AddProductActivity.EXTRA_NAME));
                Product product = new Product(data.getStringExtra(AddProductActivity.EXTRA_NAME), Integer.parseInt(data.getStringExtra(AddProductActivity.EXTRA_QUANTITY)), data.getStringExtra(AddProductActivity.EXTRA_UNITS), Integer.parseInt(getIntent().getStringExtra(ShoppingListAdapter.LIST_ID)));
                productViewModel.insert(product);
            }
        // If the submitted name is blank, a toast message appears
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.product_empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}