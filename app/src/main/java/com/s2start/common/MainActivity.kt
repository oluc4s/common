package com.s2start.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.s2start.common.ui.theme.CommonTheme
import com.s2start.core.screen.BottomSheetLayout
import com.s2start.core.screen.Caller
import com.s2start.core.screen.GenericScreen
import com.s2start.core.screen.rememberScreenState
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScreenSample()
        }
    }
}

@Parcelize
object ShowBottomSheet : Caller

@Composable
fun ScreenSample(){
    val state = rememberScreenState()
    val scope = rememberCoroutineScope()

    CommonTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            GenericScreen(
                state,
                sheetContent = {
                    handleSheet<ShowBottomSheet> { caller ->
                        BottomSheetLayout{
                            Column(Modifier.fillMaxWidth().heightIn(400.dp,700.dp)) {
                                Button(onClick = {
                                    scope.launch { state.bottomSheetState.hide() }
                                }) { Text("teste") }
                            }
                        }
                    }
                },
                modifier = Modifier.padding(innerPadding)
            ) {
                Column(Modifier.fillMaxSize().background(Color.Gray)) {
                    Button(onClick = {
                        scope.launch { state.bottomSheetState.show(ShowBottomSheet) }
                    }) {
                        Text("teste")
                    }
                }
            }
        }
    }
}