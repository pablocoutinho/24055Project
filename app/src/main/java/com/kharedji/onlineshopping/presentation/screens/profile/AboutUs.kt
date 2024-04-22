package com.kharedji.onlineshopping.presentation.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// Data class to represent team members
data class TeamMember(
    val name: String,
    val role: String
)

// Sample data for team members
val teamMembers = listOf(
    TeamMember("Founder's Name", "Pablo Coutinho"),
    TeamMember("Design Team", "Pablo Coutinho"),
    TeamMember("Customer Support Team", "Pablo Coutinho"),
    TeamMember("Tech Team", "Pablo Coutinho")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(paddingValues: PaddingValues,navController: NavController) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "About Us",
                    color = Color.White,
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)

        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Welcome to MemoSphere, where style meets convenience.",
                color = Color.Black,
                style =MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Our Story",
                color = Color.Black,
                style =MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "It all began with a passion for fashion and a desire to revolutionize the way you shop. Founded in 2024, Memo Sphere has grown from a small idea into a thriving community of fashion enthusiasts.",
                color = Color.Black,
                style =MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Our Values",
                color = Color.Black,
                style =MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Quality, Style, Accessibility, Community",
                color = Color.Black,
                style =MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Our Team",
                color = Color.Black,
                style =MaterialTheme.typography.bodyLarge
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(teamMembers) { member ->
                    Text(
                        text = "${member.name} - ${member.role}",
                        color = Color.Black,
                        style =MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Text(
                text = "Get in Touch",
                color = Color.Black,
                style =MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Have a question or feedback? We'd love to hear from you! Reach out to us at [support@Memospehere.com] or connect with us on social media [MemoSphere].",
                color = Color.Black,
                style =MaterialTheme.typography.bodyLarge
            )
        }    }

}

@Preview
@Composable
fun PreviewAboutUsScreen() {
    AboutUsScreen(PaddingValues(10.dp), rememberNavController())
}
