package uk.ac.le.co2103.part2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// DAO for Shopping lists
@Dao
public interface ShoppingListDao {

    @Insert
    void insert(ShoppingList shoppingList);

    @Query("DELETE FROM shopping_lists")
    void deleteAll();

    @Query("SELECT * FROM shopping_lists ORDER BY name ASC")
    LiveData<List<ShoppingList>> getShoppingLists();

    @Query("SELECT * FROM shopping_lists WHERE listId = :listId")
    LiveData<List<ShoppingList>> getShoppingListById(int listId);

    @Query("SELECT * FROM shopping_lists")
    LiveData<List<ShoppingList>> getShoppingListsDefaultOrder();

    @Query("DELETE FROM shopping_lists WHERE listId = :listId")
    void deleteList(int listId);

}
