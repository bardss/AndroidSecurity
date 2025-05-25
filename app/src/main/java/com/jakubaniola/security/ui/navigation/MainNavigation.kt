package com.jakubaniola.security.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.jakubaniola.security.ui.cryptography.CryptographyScreen
import com.jakubaniola.security.ui.home.HomeScreen
import java.io.File


@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun MainNavigation(filesDir: File) {
    val backStack = rememberMutableStateListOf<NavigationRoute>(Home)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Home> { entry ->
                HomeScreen {
                    backStack.add(Cryptography)
                }
            }
            entry<Cryptography> { entry ->
                CryptographyScreen(filesDir) {
                    backStack.removeLastOrNull()
                }
            }
        }
    )
}
