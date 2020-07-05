package com.dragon.final_app.activity.model

data class OrderHistoryRestaurant(
    var orderId: String,
    var restaurantName: String,
    var totalCost: String,
    var orderPlacedAt: String
)