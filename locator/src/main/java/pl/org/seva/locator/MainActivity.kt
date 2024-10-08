package pl.org.seva.locator

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pl.org.seva.locator.ui.theme.VictorLocatorTheme
import kotlinx.serialization.Serializable
import pl.org.seva.locator.presentation.CoordinatesPresentation
import pl.org.seva.locator.presentation.LocatorPresentation
import pl.org.seva.locator.presentation.ScannerPresentation
import pl.org.seva.locator.screen.CoordinatesScreen
import pl.org.seva.locator.screen.LocatorScreen
import pl.org.seva.locator.screen.ScannerScreen
import javax.inject.Inject

@Serializable
object Locator
@Serializable
object Coordinates
@Serializable
object Scanner

enum class Destination {
    Greetings,
    Coordinates,
    Scanner,
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var scannerPresentation: ScannerPresentation

    @Inject
    lateinit var coordinatesPresentation: CoordinatesPresentation

    @Inject
    lateinit var locatorPresentation: LocatorPresentation

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.containsValue(false)) {
                Toast.makeText(
                    this,
                    "Permissions not granted.\nProgram will not work.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            var selected by remember { mutableStateOf(Destination.Greetings) }

            navController.addOnDestinationChangedListener { controller, destination, bundle ->
                when (controller.previousBackStackEntry?.destination?.route) {
                    Locator::class.qualifiedName -> locatorPresentation.stopScan(scope)
                    Scanner::class.qualifiedName -> scannerPresentation.stopScan(scope)
                }
                when(controller.currentBackStackEntry?.destination?.route) {
                    Coordinates::class.qualifiedName -> coordinatesPresentation.load(scope)
                    Locator::class.qualifiedName -> {
                        locatorPresentation.load(scope)
                        locatorPresentation.startContinuousScanning(scope)
                    }
                    Scanner::class.qualifiedName -> {
                        scannerPresentation.clearTags()
                        if (
                            ContextCompat.checkSelfPermission(
                                this,
                                android.Manifest.permission.BLUETOOTH_SCAN
                            ) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(
                                    this,
                                    android.Manifest.permission.BLUETOOTH_CONNECT
                                ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            requestPermissionLauncher.launch(
                                arrayOf(
                                    android.Manifest.permission.BLUETOOTH_SCAN,
                                    android.Manifest.permission.BLUETOOTH_CONNECT,
                                ),
                            )
                        }
                    }
                }
            }

            VictorLocatorTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Victor Locator") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = null,
                                    )
                                }

                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        modifier = Modifier.padding(innerPadding),
                        drawerContent = {
                            ModalDrawerSheet {
                                NavigationDrawerItem(
                                    label = { Text(text = "Locator") },
                                    selected = selected == Destination.Greetings,
                                    onClick = {
                                        selected = Destination.Greetings
                                        scope.launch {
                                            drawerState.close()
                                        }
                                        navController.navigate(route = Locator)
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text("Coordinates") },
                                    selected = selected == Destination.Coordinates,
                                    onClick = {
                                        selected = Destination.Coordinates
                                        scope.launch {
                                            drawerState.close()
                                        }
                                        navController.navigate(route = Coordinates)
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text(text = "Scanner") },
                                    selected = selected == Destination.Scanner,
                                    onClick = {
                                        selected = Destination.Scanner
                                        scope.launch {
                                            drawerState.close()
                                        }
                                        navController.navigate(route = Scanner)
                                    }
                                )
                            }
                        }
                    ) {
                        NavHost(navController = navController, startDestination = Locator) {
                            composable<Locator> {
                                LocatorScreen(locatorPresentation)
                            }
                            composable<Coordinates> {
                                CoordinatesScreen(coordinatesPresentation)
                            }
                            composable<Scanner> {
                                ScannerScreen(scannerPresentation)
                            }
                        }
                    }
                }
            }
        }
    }

}
