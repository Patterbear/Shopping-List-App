package uk.ac.le.co2103.part2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

// ViewHolder for Products
public class ProductViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemTextView;

    private ProductViewHolder(View itemView) {
        super(itemView);
        itemTextView = itemView.findViewById(R.id.textView);
    }

    public void bind(String text) {
        itemTextView.setText(text);
    }

    static ProductViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ProductViewHolder(view);
    }
}
