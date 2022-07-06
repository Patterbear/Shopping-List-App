package uk.ac.le.co2103.part2;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

//DAO for Products
@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);

    @Query("DELETE FROM products")
    void deleteAll();

    @Query("SELECT * FROM products WHERE listId = :listId")
    LiveData<List<Product>> getShoppingList(int listId);

    @Query("SELECT * FROM products ORDER BY name ASC")
    LiveData<List<Product>> getAll();

    @Query("DELETE FROM products WHERE listId = :listId")
    void deleteProductsById(int listId);

    @Query("DELETE FROM products WHERE productId = :productId")
    void deleteProduct(int productId);

    @Query("SELECT * FROM products WHERE productId = :productId")
    LiveData<List<Product>> getProduct(int productId);

    @Query("UPDATE products SET name = :name, quantity = :quantity, unit = :unit WHERE productId = :productId")
    void updateProduct(int productId, String name, int quantity, String unit);

}
