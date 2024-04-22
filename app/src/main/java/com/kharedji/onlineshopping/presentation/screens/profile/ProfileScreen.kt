package com.kharedji.onlineshopping.presentation.screens.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kharedji.onlineshopping.R
import com.kharedji.onlineshopping.domain.models.UsersData
import com.kharedji.onlineshopping.maps.MapScreen
import com.kharedji.onlineshopping.maps.MapViewModel
import com.kharedji.onlineshopping.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController? = null,
    mapViewModel: MapViewModel
) {
    var user by remember { mutableStateOf(UsersData()) }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        val id = FirebaseAuth.getInstance().currentUser!!.uid

        val database = FirebaseDatabase.getInstance().reference
        val reference = database.child("users").child(id) // Replace "user_id" with actual user ID
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(UsersData::class.java) ?: UsersData()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Profile",
                    color = Color.White,
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)

        )
    }) {
        ProfileDetails(user = user, navController, it, mapViewModel)
    }
}

@Composable
fun ProfileDetails(
    user: UsersData,
    navController: NavController? = null,
    paddingValues: PaddingValues,
    mapViewModel: MapViewModel
) {
    val scrollState= rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .verticalScroll(scrollState, true)
            .padding(15.dp)
            .background(Color.White, RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Image(
            painter = painterResource(id = R.drawable.profile), // Add your image resource here
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(6.dp),
            contentScale = ContentScale.Crop,
        )


        Spacer(modifier = Modifier.height(16.dp))
        ProfileItem("Name", user.name ?: "")
        ProfileItem("Phone Number", user.phoneNumber)
        ProfileItem("Email", user.email)
        ProfileItem("Address", user.address)
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = {
            navController?.navigate(Screen.Aboutus.route)
        }) {
            Text(text = "About us")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController?.navigate(Screen.SignIn.route) {
                    popUpTo(Screen.SignIn.route) {
                        inclusive = true
                    }
                }
            }, Modifier.fillMaxWidth()
        ) {
            Icon(
                painterResource(id = R.drawable.baseline_logout_24), "",
                Modifier.padding(10.dp)
            )
            Text(text = "LogOut")
        }
        Spacer(modifier = Modifier.height(16.dp))

        MapScreen(
            state = mapViewModel.state.value,
        )

    }
}

@Composable
fun ProfileItem(title: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileDetails(
        UsersData("Ali", "03044728003", "Test@Gmail.com", "Lahore"),
        paddingValues = PaddingValues(40.dp),
        mapViewModel = hiltViewModel()
    )
}