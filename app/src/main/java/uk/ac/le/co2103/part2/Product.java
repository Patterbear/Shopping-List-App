package uk.ac.le.co2103.part2;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


// Class for product entity
// I added a private primary key for this class
// I tried to use 'name' originally, but it caused SQL constraint violations when creating items -
// - of the same name in different lists
@Entity(tableName = "products")
public class Product {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "productId")
    private int productId;

    @NonNull
    public String name;

    @NonNull
    public int quantity;

    @NonNull
    public String unit;

    @NonNull
    public int listId;




    public Product(@NonNull String name, @NonNull int quantity, @NonNull String unit, @NonNull int listId) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.listId = listId;
    }

    public String getItem() {
        return name + " (" + quantity + " " + unit + ")";
    }

    public int getProductId() {
        return this.productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
