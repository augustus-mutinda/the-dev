package com.augustusmutinda.the.dev

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import com.augustusmutinda.the.dev.ui.theme.TheDevTheme
import com.augustusmutinda.the.dev.viewmodels.DeveloperViewmodel

class MainActivity : ComponentActivity() {
    val developerViewmodel = DeveloperViewmodel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheDevTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val state by developerViewmodel.developerOptionsState.collectAsState()

                        when (state) {
                            "Default" -> {
                                LaunchedEffect(Unit) {
                                    developerViewmodel.isDeveloperOptionsEnabled(this@MainActivity)
                                }
                            }

                            "checking" -> Button(onClick = { /*TODO*/ }) {
                                CircularProgressIndicator()
                            }

                            "enabled" -> Button(onClick = {
                                val intent =
                                    Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }) {
                                Text("View Developer options", color = White)
                            }

                            "disabled" -> {
                                Text("Developer Options Are Disabled on this device.")
                                Button(onClick = {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }) {
                                    Text("Enable Developer options", color = White)
                                }
                            }

                            "unavailable" -> {
                                Text("Developer Options Are restricted on this device.", color = White)
                            }
                        }
                    }
                }
            }
        }
    }
}