package com.kharedji.onlineshopping.presentation.screens.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kharedji.onlineshopping.R
import com.kharedji.onlineshopping.domain.models.CartModel
import com.kharedji.onlineshopping.domain.models.ProductData
import com.kharedji.onlineshopping.navigation.Screen

@Composable
fun ProductScreen(navController: NavController, viewModel: ProductViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchProducts()
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            state.loading -> {
                CircularProgressIndicator()
            }

            state.error != null -> {
                Text(
                    text = state!!.error.toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

            state!!.data != null -> {
                state!!.data?.let {
                    ProductList(
                        products = it,
                        viewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun ProductList(
    products: List<ProductData>,
    viewModel: ProductViewModel,
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Home",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        LazyColumn(Modifier.fillMaxSize()) {
            items(products) { product ->
                ProductItem(product, {
                    navController.navigate(Screen.productDetails.withArgs(it.id))
                }, {
                    showDialog=true
                    val reference = FirebaseDatabase.getInstance().reference.child("cart")
                    val id = reference.push().key
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    val cartModel = CartModel(cartId = id!!, userId!!, product, 1)
                    reference.child(id).setValue(cartModel).addOnCompleteListener {
                        showDialog = false
                        if (it.isSuccessful) {
                            Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                context,
                                it.exception?.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                })
            }
        }
    }
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ProductItem(
    product: ProductData,
    onSelect: (ProductData) -> Unit,
    onClick: (ProductData) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier
                .clickable {
                    onSelect(product)
                }
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                product.imageUrl,
                contentDescription = "Product Image: ${product.description}", // Replace with actual description
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_launcher)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Display product description and price
            Column {
                Text(
                    text = product.name.toString(),
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "${product.price} $",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "${product.category}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                )
                Spacer(modifier = Modifier.width(5.dp))

                Button(
                    onClick = {
                        onClick(product)
                    },
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(), true
                ) {
                    Text(text = "Add To Cart")

                }
            }
        }
    }
}

@Composable
fun ProductImage(product: ProductData) {
    AsyncImage(
        product.imageUrl,
        contentDescription = "Product Image: ${product.category}", // Replace with actual description
        modifier = Modifier.size(80.dp),
        contentScale = ContentScale.Crop,

        // onLoading = {AsyncImagePainter.DefaultTransform},
        // onError = @Composable { ErrorPlaceholder(product.name) }  // Pass product name for description
    )
}

@Composable
private fun LoadingPlaceholder() {
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorPlaceholder(productName: String) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_error_outline_24),
            contentDescription = "Error Loading Image"
        )
        Text(text = "Error loading $productName", color = Color.Red)  // Use product name
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProductItem(ProductData(), {}, {})
}

