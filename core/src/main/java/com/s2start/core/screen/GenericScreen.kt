package com.s2start.biblia.utils.screen

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.android.parcel.Parcelize

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GenericScreen(
    screenState: ScreenState,
    sheetContent: (@Composable BottomSheetHandlerScope.() -> Unit)? = null,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
        Column(Modifier.fillMaxSize()) {
            if (sheetContent != null) {
                ModalBottomSheetLayout(sheetContent = {
                    BottomSheetHandlerScope().run {
                        currentCaller = screenState.bottomSheetState.bottomSheetCaller.value
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                            contentAlignment = Alignment.Center
                        ){
                            Box(modifier = Modifier
                                .width(38.dp)
                                .height(4.dp)
                                .clip(
                                    RoundedCornerShape(2.dp)
                                )
                                .background(Color.Gray))
                        }

                        sheetContent()
                        if (!sheetHandled) {
                            Spacer(modifier = Modifier.size(10.dp))
                        }
                    }
                },
                    scrimColor = Color.Black.copy(0.8f),
                    sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
                    sheetElevation = 9.dp,
                    sheetState = screenState.bottomSheetState.modalBottomSheetState,
                    content = {
                        Scaffold(
                            topBar = topBar,
                            bottomBar = bottomBar,
                            snackbarHost = snackbarHost
                        ){ innerPadding ->
                            Column(Modifier.padding(innerPadding)) {
                                content()
                            }
                        }
                    })

            } else {
                Scaffold(
                    topBar = topBar,
                    bottomBar = bottomBar,
                    snackbarHost = snackbarHost
                ){ innerPadding ->
                Column(Modifier.padding(innerPadding)) {
                    content()
                }
                }
            }
        }


}


class BottomSheetHandlerScope {
    var currentCaller: Caller = InitialCaller
    var sheetHandled: Boolean = false

    @Composable
    inline fun <reified T : Caller> handleSheet(noinline sheet: @Composable (T) -> Unit) {
        if (currentCaller is T && currentCaller !is InitialCaller && !sheetHandled) {
            sheet(currentCaller as T)
            sheetHandled = true
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Stable
class BottomSheetState(
    val modalBottomSheetState: ModalBottomSheetState,
    val bottomSheetCaller: MutableState<Caller>
) {
    suspend fun show(caller: Caller = DefaultCaller) {
        bottomSheetCaller.value = caller
        modalBottomSheetState.show()
    }

    suspend fun hide() {
        modalBottomSheetState.hide()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberScreenState(
    sheetState: BottomSheetState = rememberBottomSheetState()
): ScreenState = remember { ScreenState(sheetState) }


@Stable
class ScreenState(
    val bottomSheetState: BottomSheetState
)


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberBottomSheetState(
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
): BottomSheetState {
    val bottomSheetControllerState = rememberSaveable { mutableStateOf<Caller>(InitialCaller) }
    return remember(sheetState, bottomSheetControllerState) {
        BottomSheetState(sheetState, bottomSheetControllerState)
    }.also {
        BottomSheetStateObserver(it)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomSheetStateObserver(sheetState: BottomSheetState) {
    if (sheetState.modalBottomSheetState.currentValue == ModalBottomSheetValue.Hidden) {
        sheetState.bottomSheetCaller.value = InitialCaller
    }
    LaunchedEffect(sheetState.modalBottomSheetState.currentValue) {

    }
}


@Composable
fun BottomSheetLayout(
    content: @Composable () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)) {
        Spacer(modifier = Modifier.size(10.dp))
        content.invoke()
    }
}


interface Caller : Parcelable

@Parcelize
object InitialCaller : Caller

@Parcelize
object DefaultCaller : Caller
