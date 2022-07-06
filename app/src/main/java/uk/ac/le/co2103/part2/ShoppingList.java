package uk.ac.le.co2103.part2;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Class for shopping lists
@Entity(tableName = "shopping_lists")
public class ShoppingList {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "listId")
    private int listId;

    @NonNull
    public String name;

    public String image;

   public ShoppingList(@NonNull String name, String image) {
       this.name = name;
       this.image = image;
    }

    public String getShoppingList() {
        return name;
    }

    public int getListId() {
        return this.listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getImageUri() {
       return this.image;
    }
}
