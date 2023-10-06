package com.binar.foodorder.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.binar.foodorder.data.dummy.DummyFoodDataSourceImpl
import com.binar.foodorder.data.local.database.AppDatabase.Companion.getInstance
import com.binar.foodorder.data.local.database.dao.CartDao
import com.binar.foodorder.data.local.database.dao.FoodDao
import com.binar.foodorder.data.local.database.entity.CartEntity
import com.binar.foodorder.data.local.database.entity.FoodEntity
import com.binar.foodorder.data.local.database.mapper.toFoodEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Database(
    entities = [CartEntity::class, FoodEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao
    abstract fun foodDao(): FoodDao

    companion object {
        private const val DB_NAME = "Food.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .addCallback(DatabaseSeederCallback(context))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

class DatabaseSeederCallback(private val context: Context) : RoomDatabase.Callback() {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        scope.launch {
            getInstance(context).foodDao().insertFood(prepopulateFoods())
            getInstance(context).cartDao().insertCarts(prepopulateCarts())
        }
    }

    private fun prepopulateFoods(): List<FoodEntity> {
        return DummyFoodDataSourceImpl().getFoodList().toFoodEntity()
    }

    private fun prepopulateCarts(): List<CartEntity> {
        return mutableListOf(
            CartEntity(
                id = 1,
                foodId = 1,
                itemNotes = "makanan yang fresh ya",
                itemQuantity = 3
            ),
            CartEntity(
                id = 2,
                foodId = 2,
                itemNotes = "makanan yang fresh yaaaaaa",
                itemQuantity = 6
            ),
        )
    }
}