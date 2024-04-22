package com.kharedji.onlineshopping.domain.models

import com.google.firebase.database.DataSnapshot

data class ProductData(
    val id: String = "",
    val name:String?="",
    val category: String = "",
    val price: String = "",
    val description: String = "",
    val imageUrl: String = ""
) {
    companion object {
        // Function to convert DataSnapshot to ProductData
        fun fromSnapshot(snapshot: DataSnapshot): ProductData {
            val id = snapshot.key ?: ""
            val category = snapshot.child("category").getValue(String::class.java) ?: ""
            val price = snapshot.child("price").getValue(String::class.java) ?: ""
            val description = snapshot.child("description").getValue(String::class.java) ?: ""
            val imageUrl = snapshot.child("imageUrl").getValue(String::class.java) ?: ""
            return ProductData(id, category, price, description, imageUrl)
        }
    }

    // Function to convert ProductData to HashMap for pushing to Firebase database
    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["category"] = category
        result["price"] = price
        result["description"] = description
        result["imageUrl"] = imageUrl
        return result
    }
}
