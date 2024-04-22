package com.kharedji.onlineshopping.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kharedji.onlineshopping.data.utils.State
import com.kharedji.onlineshopping.domain.models.ProductData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SingleProductViewModel @Inject constructor() : ViewModel() {
    val productRepository=ProductRepository()

    private val _productstate: MutableStateFlow<State<ProductData>> =
        MutableStateFlow(State.loading())
    val productstate: StateFlow<State<ProductData>> = _productstate.asStateFlow()

    fun getProductById(id:String){
        viewModelScope.launch {
            productRepository.getSingleProduct(id){
                if (it!=null){
                    _productstate.value=State.success(it)
                }else{
                    _productstate.value=State.error("Error..")

                }
            }
        }
    }
}