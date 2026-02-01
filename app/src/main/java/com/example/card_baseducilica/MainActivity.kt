package com.example.card_baseducilica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.card_baseducilica.ui.theme.CardBasedUcilicaTheme
import com.example.card_baseducilica.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CardBasedUcilicaTheme {
                AppNavGraph()
            }
        }
    }
}

