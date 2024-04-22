package com.kharedji.onlineshopping.presentation.screens.main

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kharedji.onlineshopping.domain.models.CartViewModel
import com.kharedji.onlineshopping.maps.MapViewModel
import com.kharedji.onlineshopping.presentation.screens.OrdersScreen
import com.kharedji.onlineshopping.presentation.screens.cart.CartScreen
import com.kharedji.onlineshopping.presentation.screens.home.HomeScreen
import com.kharedji.onlineshopping.presentation.screens.profile.ProfileScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    paddingValues: PaddingValues = PaddingValues(),
    navController: NavController? = null,
    mapViewModel: MapViewModel,
    cartViewModel: CartViewModel= hiltViewModel()
) {
    val context= LocalContext.current
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        4
    }

    val scope = rememberCoroutineScope()
    val tabData = listOf(
        "Home" to Icons.Filled.Home,
        "Cart" to Icons.Filled.ShoppingCart,
        "Order" to Icons.Rounded.DateRange,
        "Profile" to Icons.Filled.AccountCircle
    )

    BackHandler {
        if (pagerState.currentPage != 0) {
            scope.launch {
                pagerState.animateScrollToPage(0)
            }
        }
        if (pagerState.currentPage==0){
            context as Activity
            context.finish()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(0.8f) //
                .fillMaxWidth()
                .shadow(2.dp)
                .background(Color.White)
                .wrapContentHeight()
        ) { page ->
            when (page) {
                0 -> HomeScreen(navController = navController)
                1 -> CartScreen(navController = navController,cartViewModel)
                2-> OrdersScreen(navController = navController!!)
                3-> ProfileScreen(navController,mapViewModel)

            }
        }


        TabRow(
            selectedTabIndex = pagerState.currentPage,
            divider = {
                Spacer(modifier = Modifier.height(5.dp))
            },
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    height = 3.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            tabData.forEachIndexed { index, (title, icon) ->
                LeadingIconTab(
                    icon = { Icon(imageVector = icon, contentDescription = null) },
                    text = { /*Text(text = title, style = MaterialTheme.typography.labelMedium)*/ },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },

                    unselectedContentColor = Color.Black
                )
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(mapViewModel = hiltViewModel())
}