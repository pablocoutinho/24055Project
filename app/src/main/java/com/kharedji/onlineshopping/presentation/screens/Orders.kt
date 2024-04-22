package com.kharedji.onlineshopping.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kharedji.onlineshopping.R
import com.kharedji.onlineshopping.domain.models.CartModel
import com.kharedji.onlineshopping.domain.models.OrderModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavController) {
    val (orders, setOrders) = remember { mutableStateOf<List<OrderModel>>(emptyList()) }
    val (isLoading, setLoading) = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        fetchOrdersFromFirebase { fetchedOrders ->
            setOrders(fetchedOrders)
            setLoading(false)
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Orders",
                    color = Color.White,
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)

        )
    }) {
        if (isLoading) {
            // Show loading indicator
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            OrderList(orders = orders, paddingValues = it)
        }
    }
}

@Composable
fun OrderList(orders: List<OrderModel>, paddingValues: PaddingValues) {
    if (orders.size > 0) {
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(orders) { order ->
                OrderItem(order = order)
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No Orders Found ", style = MaterialTheme.typography.titleLarge.copy(
                    color =
                    Color.Red, fontSize = TextUnit(22f, TextUnitType.Sp)
                )
            )


        }
    }
}

@Composable
fun OrderItem(order: OrderModel) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()

    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            order.cartsModel.values.forEach { cart ->
                CartItem(cart = cart)
            }
            Text(text = "Order ID: ${order.orderId}", fontWeight = FontWeight.Bold)
            Text(text = "Order Date: ${order.orderDate}")
            Text(text = "Total Products: ${order.total_products}")
            Text(text = "Total Price: ${order.total_price}")
        }
    }
}

@Composable
fun CartItem(cart: CartModel) {

    Row(
        modifier = Modifier
            .shadow(2.dp)
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            cart.product.imageUrl,
            contentDescription = "Product Image: ${cart.product.description}", // Replace with actual description
            modifier = Modifier.size(80.dp),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_launcher)
        )
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Product Name: ${cart.product.name}", fontWeight = FontWeight.Bold)
            Text(text = "Price: ${cart.product.price}")
            Text(text = "Quantity: ${cart.quantity}")
        }
    }


}

fun fetchOrdersFromFirebase(onSuccess: (List<OrderModel>) -> Unit) {
    // Fetch orders from Firebase
    // Assuming you have some Firebase API to retrieve orders
    // This function should be implemented based on your Firebase setup
    // Example:
    val list = mutableListOf<OrderModel>()
    val database = FirebaseDatabase.getInstance().reference.child("orders")
    val id = FirebaseAuth.getInstance().currentUser!!.uid
    database.orderByChild("userId").equalTo(id)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val orders = it.getValue(OrderModel::class.java)
                        list.add(orders!!)
                    }
                    onSuccess(list)
                } else {
                    onSuccess(emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onSuccess(emptyList())
            }

        })

}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    OrderList(orders = listOf(), paddingValues = PaddingValues(10.dp))
}