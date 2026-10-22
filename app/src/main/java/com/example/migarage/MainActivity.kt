package com.example.migarage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.migarage.uil.AppNavigation
import com.example.migarage.ui.theme.MiGarageTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Firebase al arrancar la app
        FirebaseApp.initializeApp(this)

        setContent {
            MiGarageTheme {
                AppNavigation()
            }
        }
    }
}