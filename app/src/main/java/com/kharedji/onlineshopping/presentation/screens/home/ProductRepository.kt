package com.kharedji.onlineshopping.presentation.screens.home

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kharedji.onlineshopping.data.utils.State
import com.kharedji.onlineshopping.domain.models.CartModel
import com.kharedji.onlineshopping.domain.models.ProductData

class ProductRepository {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("products")

    interface ProductDataCallback {
        fun onDataLoaded(products: List<ProductData>)
        fun onError(error: DatabaseError)
    }

    fun getProducts(callback: ProductDataCallback) {
        Log.e("aaa", "getProducts: ")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("aaa", "onDataChange: ")
                val productList = mutableListOf<ProductData>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(ProductData::class.java)
                    if (product != null) {
                        productList.add(product)
                    }
                }
                callback.onDataLoaded(productList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("aaa", "onCancelled: ")
                callback.onError(error)
            }
        })
    }

    fun addToCart(product: ProductData, it:Int,callback: (ProductData?) -> Unit) {
        val reference = FirebaseDatabase.getInstance().reference.child("cart")
        val id = reference.push().key
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val cartModel = CartModel(cartId = id!!, userId!!, product, it)
        reference.child(id).setValue(cartModel).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(product)
            } else {
                callback(null)
            }

        }
    }

    fun getSingleProduct(id: String, callback: (ProductData?) -> Unit) {
        databaseReference.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("aaa", "onDataChange: ")
                val product = snapshot.getValue(ProductData::class.java)
                if (product != null) {
                    callback(product)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("aaa", "onCancelled: ")
                callback(null)
            }
        })
    }
}
