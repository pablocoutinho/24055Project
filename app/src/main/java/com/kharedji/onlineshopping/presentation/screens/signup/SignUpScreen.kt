package com.kharedji.onlineshopping.presentation.screens.signup

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kharedji.onlineshopping.R
import com.kharedji.onlineshopping.domain.models.UsersData
import com.kharedji.onlineshopping.navigation.Screen
import com.kharedji.onlineshopping.presentation.screens.signup.view_models.SignUpViewModel

@Composable
fun SignUpScreen(
    paddingValues: PaddingValues = PaddingValues(),
    navController: NavController? = null,
    viewModel: SignUpViewModel? = null
) {
    val uiState = viewModel?.uiState?.collectAsState()
    val context = LocalContext.current

    var email by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var name by rememberSaveable {
        mutableStateOf("")
    }
    var phone by rememberSaveable {
        mutableStateOf("")
    }
    var address by rememberSaveable {
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
        Toast.makeText(context,"Signup Successfully!! Login To Continue",Toast.LENGTH_SHORT).show()
        navController?.navigateUp()
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
            text = "SignUp to Your Account",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(top = 20.dp)

        )
        Spacer(modifier = Modifier.height(20.dp))

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
            placeholder = {
                Text(text = "Phone Number")
            },
            onValueChange = {
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
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (phone.isEmpty()) {
                    Toast.makeText(context, "Phone is Empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (email.isEmpty()) {
                    Toast.makeText(context, "Email is Empty", Toast.LENGTH_SHORT).show()
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
                val user=UsersData(name,phone,email,address)
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel?.signUp(user, password = password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)

        ) {
            Text(text = "Sign Up")
        }

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
        else if (uiState?.value?.error?.isNotEmpty() == true) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = uiState.value.error ?: "error signing up",
                color = Color.Red
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun Preview() {
    SignUpScreen()
}