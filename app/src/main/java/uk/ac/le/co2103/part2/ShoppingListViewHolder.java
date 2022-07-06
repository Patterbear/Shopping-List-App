package uk.ac.le.co2103.part2;

import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

// View holder for shopping lists
public class ShoppingListViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemTextView;
    private final ImageView listImage;

    private ShoppingListViewHolder(View itemView) {
        super(itemView);
        itemTextView = itemView.findViewById(R.id.textView);
        listImage = itemView.findViewById(R.id.listImageView);
    }

    public void bind(String text) {
        itemTextView.setText(text);
    }

    public void bindImage(String image) {
        File image_file = new File(image);
        listImage.setImageURI(Uri.fromFile(image_file));
    }
    static ShoppingListViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ShoppingListViewHolder(view);
    }
}
