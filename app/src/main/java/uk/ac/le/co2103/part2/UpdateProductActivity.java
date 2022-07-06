package uk.ac.le.co2103.part2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Activity for updating products
public class UpdateProductActivity extends AppCompatActivity {

    private static final String TAG = AddProductActivity.class.getSimpleName();
    public static ProductViewModel productViewModel;
    public static List<Product> listProducts;
    public static String currentItemName;

    private EditText editTextItem;
    private EditText editTextQuantity;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        editTextItem = findViewById(R.id.editTextProductName);
        editTextQuantity = findViewById(R.id.editTextProductQuantity);
        spinner = findViewById(R.id.spinnerProductUnits);

        final Button incrementButton = findViewById(R.id.incrementButton);
        final Button decrementButton = findViewById(R.id.decrementButton);
        final Button saveButton = findViewById(R.id.saveProductChangesButton);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        // Sets the input views to have the current products values
        // Also saves the name of the current item so it can be used for validation upon saving
        productViewModel.getProduct(Integer.parseInt(getIntent().getStringExtra(ProductListAdapter.PRODUCT_ID))).observe(this, items -> {
            editTextItem.setText(items.get(0).name);
            editTextQuantity.setText(Integer.toString(items.get(0).quantity));
            spinner.setSelection(((ArrayAdapter) spinner.getAdapter()).getPosition(items.get(0).unit));
            currentItemName = editTextItem.getText().toString();
        });


        productViewModel.getListItems(Integer.parseInt(getIntent().getStringExtra(ProductListAdapter.LIST_ID))).observe(this, items -> {
            listProducts = items;
        });

        // Button that increases quantity by 1
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextQuantity.setText(Integer.toString(Integer.parseInt(editTextQuantity.getText().toString()) + 1));
            }
        });

        // Button that decreases quantity by 1
        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextQuantity.getText().toString().equals("1")) {
                    Toast.makeText(view.getContext(), "Quantity cannot be less than 1", Toast.LENGTH_LONG).show();
                } else{
                editTextQuantity.setText(Integer.toString(Integer.parseInt(editTextQuantity.getText().toString()) - 1));
                }
            }

        });

        // OnClick Listener for the 'Save' button
        // Checks if the current items new name (if changed) already exists
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Checking updated details");

                String testNamesString = "";

                for(int i=0; i < listProducts.size(); i++) {
                    testNamesString = testNamesString + listProducts.get(i).name + ",";
                }

                if (testNamesString.length() != 0) {
                    testNamesString = testNamesString.substring(0, testNamesString.length()-1);
                }

                List<String> testNamesList = new ArrayList<String>(Arrays.asList(testNamesString.split(",")));

                // Removes the current items name, to prevent the changes being unsaved due to the name already existing
                testNamesList.remove(currentItemName);

                if (testNamesList.contains(editTextItem.getText().toString())) {
                    Log.d(TAG, "Product already exists.");
                    Toast.makeText(getApplicationContext(), "Product already exists", Toast.LENGTH_LONG).show();
                } else {
                    productViewModel.updateProduct(Integer.parseInt(getIntent().getStringExtra(ProductListAdapter.PRODUCT_ID)),
                            editTextItem.getText().toString(),
                            Integer.parseInt(editTextQuantity.getText().toString()),
                            spinner.getSelectedItem().toString());
                    finish();
                }
            }
        });
    }
}