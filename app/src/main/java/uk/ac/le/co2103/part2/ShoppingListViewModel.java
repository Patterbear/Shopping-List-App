package uk.ac.le.co2103.part2;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

// View model for Shopping Lists
public class ShoppingListViewModel extends AndroidViewModel {
    private ShoppingListRepository repo;

    private final LiveData<List<ShoppingList>> allItems;

    public ShoppingListViewModel(Application application) {
        super(application);
        repo = new ShoppingListRepository(application);
        allItems = repo.getAllItems();
    }

    LiveData<List<ShoppingList>> getAllItems() { return allItems; }

    // Method isn't used but I've kept it as it seems to be standard to have a method that gets an item by its primary key
    public LiveData<List<ShoppingList>> getShoppingListById(int listId) {
        return repo.getShoppingListById(listId);
    }

    public void insert(ShoppingList shoppingList) { repo.insert(shoppingList); }

    public void deleteList(int listId) { repo.deleteList(listId);}

}
