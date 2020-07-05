package com.dragon.final_app.activity.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Restaurants")
data class RestEntity(
    @PrimaryKey val rest_id: Int,
    @ColumnInfo(name = "food_name") val restName: String,
    @ColumnInfo(name = "food_rating") val rating: String,
    @ColumnInfo(name = "food_price") val price: String,
    @ColumnInfo(name = "food_image") val restImage: String
)