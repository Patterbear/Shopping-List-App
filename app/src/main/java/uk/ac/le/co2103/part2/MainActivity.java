package uk.ac.le.co2103.part2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ShoppingListViewModel shoppingListViewModel;
    public static final int ADD_ITEM_ACTIVITY_REQUEST_CODE = 1;

    public static List<ShoppingList> shoppingLists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_main);

        Log.d(TAG, "Creating recyclerView");
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ShoppingListAdapter adapter = new ShoppingListAdapter(new ShoppingListAdapter.ItemDiff(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        Log.d(TAG, "Adding items to adapter");
        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);
        shoppingListViewModel.getAllItems().observe(this, items -> {
            shoppingLists = items;
            adapter.submitList(items);
        });

        Log.d(TAG, "Setting up floating action button");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener( view -> {
            Intent intent = new Intent(MainActivity.this, CreateListActivity.class);
            startActivityForResult(intent, ADD_ITEM_ACTIVITY_REQUEST_CODE);
        });


    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ITEM_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            String testNamesString = "";

            // Creates string from all of the names of the current shopping list products
            // I had to use a string and then make it into a list because it was producing a lot of errors
            for(int i=0; i < shoppingLists.size(); i++) {
                testNamesString = testNamesString + shoppingLists.get(i).name + ",";
            }

            // Removes final comma
            if (testNamesString.length() != 0) {
                testNamesString = testNamesString.substring(0, testNamesString.length()-1);
            }

            // Creates list of product names by splitting the string
            List<String> testNamesList = new ArrayList<String>(Arrays.asList(testNamesString.split(",")));

            // If the new list's name has already been used, a toast message appears
            // Otherwise, the new list is added.
            if (testNamesList.contains(data.getStringExtra(CreateListActivity.EXTRA_REPLY))) {
                Log.d(TAG, "List with name already exists.");
                Toast.makeText(getApplicationContext(), "List with that name already exists", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "Creating list");
                ShoppingList shoppingList = new ShoppingList(data.getStringExtra(CreateListActivity.EXTRA_REPLY), data.getStringExtra(CreateListActivity.EXTRA_IMAGE));
                shoppingListViewModel.insert(shoppingList);
            }

        // Executes if the list's name is empty
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.list_empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

}