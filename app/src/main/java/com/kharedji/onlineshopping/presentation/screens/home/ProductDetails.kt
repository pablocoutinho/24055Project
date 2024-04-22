package com.kharedji.onlineshopping.presentation.screens.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kharedji.onlineshopping.R
import com.kharedji.onlineshopping.domain.models.ProductData

@Composable
fun ProductDetails(
    productData: String,
    navController: NavController,
    productviewModel: ProductViewModel,
    onAddToCartClicked: (ProductData, Int) -> Unit
) {
    val context = LocalContext.current
    val viewModel: SingleProductViewModel = hiltViewModel()
    val state = viewModel.productstate.collectAsState()
    val cartState = productviewModel.cartState.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewModel.getProductById(productData)
    }
    when {
        state.value.loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    Modifier
                        .shadow(5.dp)
                )
            }
        }

        state.value.error?.isNotEmpty() == true -> {

        }

        cartState.value.loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    Modifier.wrapContentSize()
                )
            }
        }

        cartState.value.data != null -> {
            Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show()
            val productData = state.value.data!!
            ShowProductDetails(productData = productData) { data, i ->
                onAddToCartClicked(data, i)
            }
        }

        else -> {
            val productData = state.value.data!!
            ShowProductDetails(productData = productData) { data, i ->
                onAddToCartClicked(data, i)
            }
        }
    }

}

@Composable
fun ShowProductDetails(productData: ProductData, onAddToCartClicked: (ProductData, Int) -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = productData.imageUrl, // Placeholder image resource
            contentDescription = "Product Image",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = productData.name ?: "", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Category: " + productData.category,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = productData.price + "$", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = productData.description, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
        AddToCartSection(productData, onAddToCartClicked)
    }
}


@Composable
fun AddToCartSection(
    productData: ProductData,
    onAddToCartClicked: (ProductData, Int) -> Unit
) {
    val quantityState = remember { mutableStateOf(1) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { quantityState.value++ }) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Add",
                tint = MaterialTheme.colorScheme.primary
            )

        }

        Text(text = "${quantityState.value}", modifier = Modifier.padding(horizontal = 8.dp))

        IconButton(
            onClick = { if (quantityState.value > 1) quantityState.value-- })
        {
            Icon(
                painter = painterResource(id = R.drawable.baseline_remove_circle_24),
                "remove", tint = MaterialTheme.colorScheme.primary
            )
        }

        Button(
            onClick = { onAddToCartClicked(productData, quantityState.value) },
            modifier = Modifier.padding(start = 8.dp),
            shape = RectangleShape
        ) {
            Text("Add to Cart")
        }
    }
}
