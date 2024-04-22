package com.kharedji.onlineshopping.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kharedji.onlineshopping.domain.models.CartModel
import com.kharedji.onlineshopping.domain.models.CartViewModel
import com.kharedji.onlineshopping.domain.models.OrderModel
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun Checkout(
    paddingValues: PaddingValues = PaddingValues(),
    navController: NavController? = null,
    cartViewModel: CartViewModel
) {
    val context = LocalContext.current
    val cartList = cartViewModel.cartState.value?.copy()?.data

    var name by rememberSaveable {
        mutableStateOf("")
    }
    var phone by rememberSaveable {
        mutableStateOf("")
    }
    var address by rememberSaveable {
        mutableStateOf("")
    }
    var creditCardNumber by rememberSaveable {
        mutableStateOf("")
    }
    var isCreditCardNumberValid by remember { mutableStateOf(true) }

    var expirationDate by rememberSaveable {
        mutableStateOf("")
    }
    var isExpirationDateValid by remember { mutableStateOf(true) }

    var cvv by rememberSaveable {
        mutableStateOf("")
    }
    var isCvvValid by remember { mutableStateOf(true) }
    Log.e("aaa", "Checkout: ${cartList?.size}")
    var showDialog by remember { mutableStateOf(false) }

    val localFocusManager = LocalFocusManager.current
    if (showDialog){
        Dialog(
            onDismissRequest = { showDialog = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(White, shape = RoundedCornerShape(8.dp))
            ) {
                CircularProgressIndicator()
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = {
                Text(text = "Name")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = "password")
            },
            value = name,
            placeholder = {
                Text(text = "Name")
            },
            onValueChange = {
                name = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = {
                Text(text = "Phone Number")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Phone, contentDescription = "password")
            },
            value = phone,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),

            placeholder = {
                Text(text = "Phone Number")
            },
            onValueChange = {it->
                phone = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = {
                Text(text = "Address")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "password")
            },
            value = address,
            placeholder = {
                Text(text = "Address")
            },
            onValueChange = {
                address = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))


        // Credit Card Number TextField
        TextField(
            label = {
                Text(text = "Credit Card Number")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle, contentDescription = "credit card"
                )
            },
            value = creditCardNumber,
            placeholder = {
                Text(text = "Credit Card Number")
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),

            onValueChange = {
                if (it.length < 19) {
                    creditCardNumber = it
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Expiration Date TextField
        TextField(
            label = {
                Text(text = "Expiration Date")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "expiration date"
                )
            },
            value = expirationDate,
            placeholder = {
                Text(text = "MM/YY")
            },

            onValueChange = { newValue ->
                if (newValue.length<5){
                    expirationDate=newValue
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // CVV TextField
        TextField(
            label = {
                Text(text = "CVV")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "cvv")
            },
            value = cvv,
            placeholder = {
                Text(text = "CVV")
            },
            onValueChange = {
                if (it.length <= 3 && it.all { it.isDigit() }) {
                    cvv = it
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (phone.isEmpty()) {
                    Toast.makeText(context, "Phone is Empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (cvv.isEmpty()) {
                    Toast.makeText(context, "Cvv is Empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (address.isEmpty()) {
                    Toast.makeText(context, "Address is Empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (name.isEmpty()) {
                    Toast.makeText(context, "Name is Empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (expirationDate.isEmpty()) {
                    Toast.makeText(context, "Expiry Date is Empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (creditCardNumber.isEmpty()){
                    Toast.makeText(context, "Credit Card is Empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }


                showDialog=true

                val databaseReference = FirebaseDatabase.getInstance().reference.child("orders")
                val orderId = databaseReference.push().key
                val date =
                    SimpleDateFormat("dd/MM/yyyy hh:mm").format(Date(System.currentTimeMillis()))
                val hashMap: HashMap<String, CartModel> = hashMapOf()
                val totalProducts = cartList?.sumOf { it.quantity }
                val totalPrice = cartList?.sumOf { it.product.price.toInt() * it.quantity }
                cartList?.onEach {
                    val id = databaseReference.push().key
                    hashMap.put(id!!, it)
                }
                val orderList = OrderModel(
                    orderId!!,
                    date,
                    hashMap,
                    totalProducts!!,
                    totalPrice!!,
                    FirebaseAuth.getInstance().currentUser!!.uid
                )
                databaseReference.child(orderId).setValue(orderList).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val cartRef = FirebaseDatabase.getInstance().reference.child("cart")
                        cartList.onEachIndexed { index, it ->
                            cartRef.child(it.cartId).removeValue()
                            if (index == cartList.size - 1) {
                                showDialog=false
                                navController?.navigateUp()
                            }
                        }
                    } else {
                        Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)

        ) {
            Text(text = "Confirm Order")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun Preview() {
}