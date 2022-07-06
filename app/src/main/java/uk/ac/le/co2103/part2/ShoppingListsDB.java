package uk.ac.le.co2103.part2;


import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

// Database for shopping lists
@Database(entities = {ShoppingList.class}, version = 1, exportSchema = false)
public abstract class ShoppingListsDB extends RoomDatabase {

    public abstract ShoppingListDao shoppingListDao();

    private static volatile ShoppingListsDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ShoppingListsDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ShoppingListsDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ShoppingListsDB.class, "shopping_lists_db")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                ShoppingListDao dao = INSTANCE.shoppingListDao();
                dao.deleteAll();
            });
        }
    };
}