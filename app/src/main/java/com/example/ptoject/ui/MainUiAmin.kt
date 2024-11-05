import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackshop.shop.Screen_Form
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import com.example.ptoject.ui.ShowScaffold

class MainUiAmin : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
                MainUi()
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainUi() {
    // استفاده از SnackbarHostState برای مدیریت اسنک‌بارها
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = { MyBottomBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                containerColor = Color(0xFFFE5B52),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
                .padding(paddingValues = paddingValues)
        ) {

        }
    }
}

@Composable
fun MyBottomBar() {

    val bottomMenuItemsList = prepareBottomMenu()

    var selected by remember { mutableStateOf("profile") }

    NavigationBar {
        bottomMenuItemsList.forEach { menuItem ->
            NavigationBarItem(
                icon = { Icon(menuItem.icon, contentDescription = menuItem.title) },
                label = { Text(menuItem.title) },
                selected = selected == menuItem.route,
                onClick = {
                    selected = menuItem.route
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}

data class BottomMenuItem(val title: String, val icon: ImageVector, val route: String)

@Composable
fun prepareBottomMenu(): List<BottomMenuItem> {
    return listOf(
        BottomMenuItem("Home", Icons.Default.Home, "home"),
        BottomMenuItem("Profile", Icons.Default.Person, "profile"),
        BottomMenuItem("Settings", Icons.Default.Settings, "settings")
    )
}

@Preview(showBackground = true)
@Composable
fun ShowPreview() {
    JetPackShopTheme {
        MainUi()
    }
}
