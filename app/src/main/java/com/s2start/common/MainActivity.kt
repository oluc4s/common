package com.s2start.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.s2start.common.ui.theme.CommonTheme
import com.s2start.core.screen.GenericScreen
import com.s2start.core.screen.rememberScreenState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CommonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GenericScreen(
                        rememberScreenState(),
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        Text("teste da tela")
                    }
                }
            }
        }
    }
}
