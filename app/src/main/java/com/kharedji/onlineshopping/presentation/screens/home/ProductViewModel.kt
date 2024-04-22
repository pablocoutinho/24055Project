package com.kharedji.onlineshopping.presentation.screens.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseError
import com.kharedji.onlineshopping.data.utils.State
import com.kharedji.onlineshopping.domain.models.ProductData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(): ViewModel() {

    private val productRepository = ProductRepository()
    private val _state:MutableStateFlow<State<List<ProductData>>> = MutableStateFlow(State.loading())
    val state: StateFlow<State<List<ProductData>>> =_state.asStateFlow()
    private val _cartState: MutableStateFlow<State<ProductData>> =
        MutableStateFlow(State())
    val cartState: StateFlow<State<ProductData>> = _cartState.asStateFlow()


    fun fetchProducts() {
        _state.value = State.loading()
        productRepository.getProducts(object : ProductRepository.ProductDataCallback {
            override fun onDataLoaded(products: List<ProductData>) {
                _state.value = State.success(products)
            }

            override fun onError(error: DatabaseError) {
                _state.value = State.error(error.message)
            }
        })
    }
    fun addToCart(productData: ProductData, i: Int){
        _cartState.value= State.loading()
        viewModelScope.launch {
            productRepository.addToCart(productData,i){
                if (it!=null){
                    _cartState.value= State.success(it)
                }else{
                    _cartState.value=State.error("Something Went Wrong")
                }
            }

        }
    }
}
