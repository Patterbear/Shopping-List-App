package uk.ac.le.co2103.part2;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

// ViewModel for Products
public class ProductViewModel extends AndroidViewModel {
    private ProductRepository repo;

    private final LiveData<List<Product>> allItems;

    public ProductViewModel(Application application) {
        super(application);
        repo = new ProductRepository(application);
        allItems = repo.getAllItems();
    }

    // Method is unused but I decided to keep it as they seem to be standard in android rooms
    LiveData<List<Product>> getAllItems() { return allItems; }

    public void insert(Product product) { repo.insert(product); }

    public LiveData<List<Product>> getListItems(int listId) {
        return repo.getListItems(listId);
    }

    public void deleteProductsById(int listId) { repo.deleteProductsById(listId);}

    public void deleteProduct(int productId) { repo.deleteProduct(productId);}

    public LiveData<List<Product>> getProduct(int productId) { return repo.getProduct(productId);}

    public void updateProduct(int productId, String name, int quantity, String unit) { repo.updateProduct(productId, name, quantity, unit);}
}
