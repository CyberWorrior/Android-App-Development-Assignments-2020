package com.dragon.final_app.activity.Cartdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodDao {
    @Insert
    fun insertFood(foodEntity: FoodEntity)

    @Delete
    fun deleteFood(foodEntity: FoodEntity)

    @Query("SELECT * FROM FoodCart")
    fun getAllCartItems(): List<FoodEntity>

    @Query("SELECT * FROM FoodCart WHERE foodId = :food_Id")
    fun getCartItemById(food_Id: String): FoodEntity

    @Query("SELECT food_price FROM FoodCart")
    fun getallPrice(): List<String>

    @Query("DELETE FROM FoodCart")
    fun deleteEverything()
}