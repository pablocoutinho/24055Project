package com.kharedji.onlineshopping.presentation.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.kharedji.onlineshopping.R
import com.kharedji.onlineshopping.domain.models.CartModel
import com.kharedji.onlineshopping.domain.models.CartViewModel
import com.kharedji.onlineshopping.navigation.Screen

@Composable
fun CartScreen(navController: NavController?, cartViewModel: CartViewModel = hiltViewModel()) {
    val cartState by cartViewModel.cartState.observeAsState()
    LaunchedEffect(key1 = Unit) {
        cartViewModel.fetchCartItems(FirebaseAuth.getInstance().currentUser!!.uid)
    }
    when {
        cartState?.loading == true -> {
            // Show loading indicator
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        cartState?.error != null -> {
            // Show error message
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: ${cartState!!.error}",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        else -> {
            // Show cart items and total price
            Column(modifier = Modifier.fillMaxSize()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cart",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 20.sp, fontWeight = FontWeight.Bold
                        )
                    )
                }
                LazyColumn(Modifier.weight(1f)) {
                    items(cartState?.data ?: emptyList()) { cartItem ->
                        CartItem(cartItem = cartItem, cartViewModel)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display total price and product count
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Price: ${cartState?.data?.sumOf { it.quantity * it.product.price.toInt() }}",
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Total Products: ${cartState?.data?.sumOf { it.quantity }}",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController?.navigate(Screen.checkOut.route)
                    }, modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Checkout")
                }
            }
        }
    }
}

@Composable
fun CartItem(cartItem: CartModel, cartViewModel: CartViewModel?=null) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Display product name
            AsyncImage(
                cartItem.product.imageUrl,
                contentDescription = "Product Image: ${cartItem.product.description}", // Replace with actual description
                modifier = Modifier
                    .width(90.dp)
                    .requiredHeightIn(80.dp, 100.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_launcher)
            )

            Column(
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp)
                    .weight(1f)
            ) {
                Text(
                    text = cartItem.product.name.toString(),
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.size(5.dp))
                Text(
                    text = "${cartItem.product.price} $",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Left
                ) {
                    IconButton(
                        modifier = Modifier
                            .size(20.dp),
                        onClick = { cartViewModel!!.incrementQuantity(cartItem.cartId) }) {
                        Icon(

                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(text = "${cartItem.quantity}")
                    Spacer(modifier = Modifier.size(10.dp))

                    IconButton(
                        modifier = Modifier
                            .size(20.dp),
                        onClick = { cartViewModel!!.decrementQuantity(cartItem.cartId) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_remove_circle_24),
                            "remove",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "",
                modifier = Modifier
                    .wrapContentSize(align = Alignment.TopEnd)
                    .padding(10.dp)
                    .clickable {
                        cartViewModel!!.deleteItem(cartItem.cartId)
                    }
            )
        }

    }
}
