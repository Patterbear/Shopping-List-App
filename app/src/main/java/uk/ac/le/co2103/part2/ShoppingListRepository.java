package uk.ac.le.co2103.part2;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

// Repository for shopping lists
public class ShoppingListRepository {
    private ShoppingListDao shoppingListDao;
    private LiveData<List<ShoppingList>> allShoppingLists;
    private LiveData<List<ShoppingList>> allShoppingListsDefaultOrder;

    ShoppingListRepository(Application application) {
        ShoppingListsDB db = ShoppingListsDB.getDatabase(application);
        shoppingListDao = db.shoppingListDao();
        allShoppingLists = shoppingListDao.getShoppingLists();
        allShoppingListsDefaultOrder = shoppingListDao.getShoppingListsDefaultOrder();
    }

    LiveData<List<ShoppingList>> getAllItems() {
        return allShoppingLists;
    }

    public LiveData<List<ShoppingList>> getShoppingListById(int listId) {
        return shoppingListDao.getShoppingListById(listId);
    }

    void insert(ShoppingList shoppingList) {
        ShoppingListsDB.databaseWriteExecutor.execute(() -> {
            shoppingListDao.insert(shoppingList);
        });
    }

    void deleteList(int listId) {
        ShoppingListsDB.databaseWriteExecutor.execute(() -> {
            shoppingListDao.deleteList(listId);
        });
    }

    LiveData<List<ShoppingList>> getALlItemsDefaultOrder() {
        return allShoppingListsDefaultOrder;
    }

}
