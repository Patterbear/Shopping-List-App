package uk.ac.le.co2103.part2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddProductActivity extends AppCompatActivity {

    private static final String TAG = AddProductActivity.class.getSimpleName();
    public static final String EXTRA_NAME = "uk.ac.le.co2103.part2.NAME";
    public static final String EXTRA_QUANTITY = "uk.ac.le.co2103.part2.QUANTITY";
    public static final String EXTRA_UNITS = "uk.ac.le.co2103.part2.UNITS";


    private EditText editTextItem;
    private EditText editTextQuantity;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_add_item);

        editTextItem = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        spinner = findViewById(R.id.spinner);

        final Button createButton = findViewById(R.id.button_create_product);
        createButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(editTextItem.getText())) {
                Log.i(TAG, "Empty text field could be controlled in UI (Save button disabled)");
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                Log.i(TAG, "Adding item to list");
                String product_name = editTextItem.getText().toString();
                String product_quantity = editTextQuantity.getText().toString();
                String product_units = spinner.getSelectedItem().toString();


                replyIntent.putExtra(EXTRA_NAME, product_name);
                replyIntent.putExtra(EXTRA_QUANTITY, product_quantity);
                replyIntent.putExtra(EXTRA_UNITS, product_units);


                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}