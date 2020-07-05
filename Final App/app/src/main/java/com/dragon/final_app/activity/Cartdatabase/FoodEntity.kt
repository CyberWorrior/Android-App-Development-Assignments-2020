package com.dragon.final_app.activity.Cartdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "FoodCart")
data class FoodEntity(
    @PrimaryKey val foodId: Int,
    @ColumnInfo(name = "food_name") val foodName: String,
    @ColumnInfo(name = "food_price") val foodPrice: String
)