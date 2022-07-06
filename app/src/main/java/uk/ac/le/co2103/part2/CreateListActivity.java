package uk.ac.le.co2103.part2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class CreateListActivity extends AppCompatActivity {

    private static final String TAG = CreateListActivity.class.getSimpleName();
    public static final String EXTRA_REPLY = "uk.edu.le.co2103.lab13.REPLY";
    public static final String EXTRA_IMAGE = "uk.edu.le.co2103.IMAGE";

    public static String imagePath = "";

    private final int GALLERY_REQ_CODE = 1000;
    ImageView imgGallery;

    private EditText editListNameItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_create_list);

        editListNameItem = findViewById(R.id.editTextName);
        imgGallery = findViewById(R.id.imgGallery);

        // Code for the upload button
        // Opens the phone's gallery and lets the user choose a photo
        Button btnGallery = findViewById(R.id.btnGallery);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Opening gallery");
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });


        //'Create' Button
        // Puts two extras in the intent, the name of the item (EXTRA_REPLY) and the optional image (EXTRA_IMAGE)
        final Button button = findViewById(R.id.button_create_list);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(editListNameItem.getText())) {
                Log.i(TAG, "Empty text field could be controlled in UI (Save button disabled)");
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                Log.i(TAG, "Adding Shopping List");
                String item = editListNameItem.getText().toString();

                replyIntent.putExtra(EXTRA_REPLY, item);
                replyIntent.putExtra(EXTRA_IMAGE, imagePath);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }


    // Activity result for uploading an image
    // Sets the on screen image to the uploaded one
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK) {
            if(requestCode==GALLERY_REQ_CODE) {
                Log.d(TAG, "Changing image to uploaded one");
                imgGallery.setImageURI(data.getData());

                File file = new File(data.getData().getPath());
                final String[] split = file.getPath().split(":");
                imagePath = split[1];
            }
        }
    }
}