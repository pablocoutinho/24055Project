package com.kharedji.onlineshopping.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.kharedji.onlineshopping.domain.models.CartViewModel
import com.kharedji.onlineshopping.maps.MapViewModel
import com.kharedji.onlineshopping.presentation.screens.Checkout
import com.kharedji.onlineshopping.presentation.screens.home.ProductDetails
import com.kharedji.onlineshopping.presentation.screens.home.ProductViewModel
import com.kharedji.onlineshopping.presentation.screens.main.MainScreen
import com.kharedji.onlineshopping.presentation.screens.profile.AboutUsScreen
import com.kharedji.onlineshopping.presentation.screens.signin.SignInScreen
import com.kharedji.onlineshopping.presentation.screens.signin.view_models.SignInViewModel
import com.kharedji.onlineshopping.presentation.screens.signup.SignUpScreen
import com.kharedji.onlineshopping.presentation.screens.signup.view_models.SignUpViewModel

@Composable
fun Navigation(
    padding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    mapViewModel: MapViewModel
) {
    val cartViewModel:CartViewModel= hiltViewModel()

    val startDestination =
        if (FirebaseAuth.getInstance().currentUser?.uid == null) Screen.SignIn.route else Screen.Main.route
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Screen.SignUp.route) {
            val viewModel: SignUpViewModel = hiltViewModel()
            SignUpScreen(
                paddingValues = padding,
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(route = Screen.SignIn.route) {
            val viewModel: SignInViewModel = hiltViewModel()
            SignInScreen(
                paddingValues = padding,
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(route = Screen.Main.route) {
            MainScreen(
                paddingValues = padding,
                navController = navController,
                mapViewModel,
                cartViewModel
            )
        }
        composable(route = Screen.Aboutus.route) {
            AboutUsScreen(
                paddingValues = padding,
                navController = navController,
            )
        }
        composable(route = Screen.checkOut.route) {
            Checkout(
                paddingValues = padding,
                navController = navController,
                cartViewModel
            )
        }
        composable(
            Screen.productDetails.route + "/{id}",
            listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) {
            val productViewModel:ProductViewModel = hiltViewModel()
            val id = it.arguments?.getString("id")?:""
            Log.e("aaa", "Navigation: ${id}", )
            ProductDetails(navController = navController, productData = id, productviewModel = productViewModel){data,it->
                productViewModel.addToCart(data,it)
            }
        }
    }
}