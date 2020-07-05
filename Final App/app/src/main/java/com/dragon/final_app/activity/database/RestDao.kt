package com.dragon.final_app.activity.database


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface RestDao {
    @Insert
    fun insertFood(restEntity: RestEntity)

    @Delete
    fun deleteFood(restEntity: RestEntity)

    @Query("SELECT * FROM Restaurants")
    fun getAllRests(): List<RestEntity>

    @Query("SELECT * FROM Restaurants WHERE rest_id = :restId")
    fun getRestById(restId: String): RestEntity

    @Query("SELECT COUNT(food_name) FROM Restaurants")
    fun getFavouriteRestaurantCount(): Int
}