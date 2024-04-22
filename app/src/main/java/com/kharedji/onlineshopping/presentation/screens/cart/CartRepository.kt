package com.kharedji.onlineshopping.presentation.screens.cart

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.values
import com.kharedji.onlineshopping.domain.models.CartModel
import com.kharedji.onlineshopping.domain.models.ProductData

class CartRepository {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("cart")

    interface CartDataCallback {
        fun onDataLoaded(cartItems: List<CartModel>)
        fun onError(error: DatabaseError)
    }

    fun getCartItems(userId: String, callback: CartDataCallback) {
        databaseReference.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cartItems = mutableListOf<CartModel>()
                snapshot.children.forEach { cartItemSnapshot ->
                    val product = cartItemSnapshot.child("product").getValue(ProductData::class.java)
                    val quantity = cartItemSnapshot.child("quantity").getValue(Int::class.java) ?: 0
                    val id=cartItemSnapshot.child("cartId").getValue(String::class.java)?:""
                    val userId=cartItemSnapshot.child("userId").getValue(String::class.java)?:""
                    product?.let {
                        cartItems.add(CartModel(id,userId,it, quantity))
                    }
                }
                callback.onDataLoaded(cartItems)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onError(error)
            }
        })
    }

    fun deleteItem(id:String){
        databaseReference.child(id).removeValue()
    }
}
