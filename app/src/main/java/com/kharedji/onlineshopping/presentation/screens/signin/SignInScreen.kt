package com.kharedji.onlineshopping.presentation.screens.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kharedji.onlineshopping.R
import com.kharedji.onlineshopping.navigation.Screen
import com.kharedji.onlineshopping.presentation.screens.signin.view_models.SignInViewModel

@Composable
fun SignInScreen(
    paddingValues: PaddingValues = PaddingValues(),
    navController: NavController? = null,
    viewModel: SignInViewModel? = null
) {
    val uiState = viewModel?.uiState?.collectAsState()

    var email by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }

    var isEmailValid by rememberSaveable {
        mutableStateOf(false)
    }

    var isPasswordValid by rememberSaveable {
        mutableStateOf(false)
    }

    val localFocusManager = LocalFocusManager.current

    // non-null data in the uiState means that the sign up request
    // was processed successfully and the account has been created.
    // Therefore the NavController can route to the MainScreen.
    uiState?.value?.data?.let {
        navController?.apply {
            navigate(Screen.Main.route)
            /*.also {
            popBackStack(
                route = Screen.SignIn.route,
                inclusive = true
            )
        }*/
        }
        viewModel.resetUiState()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher), contentDescription = "",
            modifier = Modifier
                .size(100.dp)
                .padding(top = 20.dp)
        )
        Text(
            text = "SignIn to Your Account",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(vertical = 20.dp)
        )

        TextField(
            label = {
                Text(text = "Email")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "email")
            },
            value = email,
            placeholder = {
                Text(text = "Email")
            },
            onValueChange = {
                email = it
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = {
                Text(text = "Password")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "password")
            },
            value = password,
            placeholder = {
                Text(text = "Password")
            },
            onValueChange = {
                password = it
            }, modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel?.signIn(email = email, password = password)
            }
        },modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp)
            ) {
            Text(text = "Sign In")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Don't have an account? Sign up",
            color = Color.Gray,
            modifier = Modifier
                .clickable {
                    navController?.navigate(Screen.SignUp.route)
                }
        )

        if (uiState?.value?.loading == true) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .pointerInput(Unit) {}
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                        .align(Alignment.Center)
                        .padding(10.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    strokeCap = StrokeCap.Round
                )
            }
        }
        if (uiState?.value?.error?.isNotEmpty() == true) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = uiState.value.error ?: "error logging in",
                color = Color.Red
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignInScreen() {
    SignInScreen()
}