package com.kharedji.onlineshopping.domain.models

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.kharedji.onlineshopping.data.utils.State
import com.kharedji.onlineshopping.presentation.screens.cart.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {

    private val cartRepository = CartRepository()
    private val _cartState = MutableLiveData<State<List<CartModel>>>()
    val cartState: LiveData<State<List<CartModel>>>
        get() = _cartState

    init {
        FirebaseAuth.getInstance().currentUser?.let { fetchCartItems(userId = it.uid) }
    }
    fun fetchCartItems(userId: String) {
        _cartState.value = State(loading = true)
        cartRepository.getCartItems(userId, object : CartRepository.CartDataCallback {
            override fun onDataLoaded(cartItems: List<CartModel>) {
                _cartState.value = State.success(cartItems)
            }

            override fun onError(error: DatabaseError) {
                _cartState.value = State.error( error.message)
            }
        })
    }
    fun incrementQuantity(productId: String) {
        val currentCartState = _cartState.value
        currentCartState?.let { state ->
            val updatedCartItems = state.data!!.map { cartItem ->
                if (cartItem.cartId == productId) {
                    CartModel(cartItem.cartId,cartItem.userId,cartItem.product, cartItem.quantity + 1)
                } else {
                    cartItem
                }
            }
            _cartState.value = state.copy(data = updatedCartItems)
        }
    }

    fun decrementQuantity(productId: String) {
        val currentCartState = _cartState.value
        currentCartState?.let { state ->
            val updatedCartItems = state.data!!.map { cartItem ->
                if (cartItem.cartId== productId && cartItem.quantity > 0) {
                    CartModel(cartItem.cartId,cartItem.userId,cartItem.product, cartItem.quantity -1)
                } else {
                    cartItem
                }
            }
            _cartState.value = state.copy(data = updatedCartItems)
        }
    }
    fun deleteItem(productId: String){
        cartRepository.deleteItem(id = productId)
    }
}

