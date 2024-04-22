package com.kharedji.onlineshopping.presentation.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kharedji.onlineshopping.R
import com.kharedji.onlineshopping.ui.theme.MemoSphereTheme
import kotlinx.coroutines.delay

private const val SPLASH_SCREEN_DURATION = 3000L // Duration of splash screen in milliseconds
private const val SPLASH_SCREEN_FADE_IN_DURATION = 1000

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemoSphereTheme {
                SplashScreen {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            }
        }

    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onSplashScreenFinished: () -> Unit
) {
    var animationPlayed by remember { mutableStateOf(false) }

    LaunchedEffect(animationPlayed) {
        delay(SPLASH_SCREEN_DURATION)
        onSplashScreenFinished()
    }

    val fadeInAlpha = animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = SPLASH_SCREEN_FADE_IN_DURATION), label = ""
    ).value

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome to",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,

            )
            Text(
                text = "Memosphere",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 28.sp,
            )
        }
    }

    if (!animationPlayed) {
        animationPlayed = true
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MemoSphereTheme {
        SplashScreen {

        }
    }
}