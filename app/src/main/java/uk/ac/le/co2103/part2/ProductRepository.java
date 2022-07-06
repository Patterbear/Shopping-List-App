package uk.ac.le.co2103.part2;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;


// Repository for products
public class ProductRepository {
    private ProductDao productDao;
    private LiveData<List<Product>> allProducts;
    private LiveData<List<Product>> listItems;

    ProductRepository(Application application) {
        ProductsDB db = ProductsDB.getDatabase(application);
        productDao = db.productDao();
        allProducts = productDao.getAll();
    }


    LiveData<List<Product>> getAllItems() {
        return allProducts;
    }


    LiveData<List<Product>> getListItems(int listId) { return productDao.getShoppingList(listId); }


    void insert(Product product) {
        ProductsDB.databaseWriteExecutor.execute(() -> {
            productDao.insert(product);
        });
    }


    void deleteProductsById(int listId) {
        ProductsDB.databaseWriteExecutor.execute(() -> {
            productDao.deleteProductsById(listId);
        });
    }


    void deleteProduct(int productId) {
        ProductsDB.databaseWriteExecutor.execute(() -> {
            productDao.deleteProduct(productId);
        });
    }


    LiveData<List<Product>> getProduct(int productId) { return productDao.getProduct(productId); }


    void updateProduct(int productId, String name, int quantity, String unit) {
        ProductsDB.databaseWriteExecutor.execute(() -> {
            productDao.updateProduct(productId, name, quantity, unit);
        });
    }
}
