package com.example.travelapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.travelapp.ui.theme.navigation.NavGraph
import com.facebook.CallbackManager

class MainActivity : ComponentActivity() {

    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Facebook CallbackManager
        callbackManager = CallbackManager.Factory.create()

        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            NavGraph(navController)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
