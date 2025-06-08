package com.example.wizkid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding // Import padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.wizkid.navigation.AppNavigation
import com.example.wizkid.ui.theme.WizKidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WizKidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding)) // Pass modifier with padding
                }
            }
        }
    }
}