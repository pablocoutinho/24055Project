package com.kharedji.onlineshopping.navigation

sealed class Screen(val route: String) {
    data object SignUp : Screen("signup")
    data object SignIn : Screen("signin")
    data object Main : Screen("main")
    data object Home : Screen("home")
    data object Aboutus : Screen("aboutus")
    data object productDetails : Screen("productDetails")
    data object checkOut : Screen("checkout")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}