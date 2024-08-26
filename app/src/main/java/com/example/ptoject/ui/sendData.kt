package com.example.ptoject.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.jetpackshop.R
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import com.example.ptoject.data.ViewModles.ViewModelsProject
import generatePdf
import openPdf

class SendData : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetPackShopTheme {
//                val viewModel: ViewModelsProject = viewModel()
//                GetData(viewModel, this)
                MainUi()
            }
        }
    }
}


@Composable
fun GetData(viewModel: ViewModelsProject, context: Context) {
    var id by remember { mutableStateOf("") }
    val modelProject by viewModel.modelProject
    val registerText by viewModel.registerText

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = id,
            onValueChange = { id = it },
            label = { Text("Enter Post ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            id.toIntOrNull()?.let { viewModel.getData(it) }
        }) {
            Text("Get Data")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            modelProject?.let {
                val file = generatePdf(context, it, "report.pdf")
                Toast.makeText(context, "PDF saved to ${file.absolutePath}", Toast.LENGTH_LONG)
                    .show()
                openPdf(context, file)
            }
        }) {
            Text("Download PDF")
        }

        Text(text = registerText)

        modelProject?.let {
            Column {
                Text("ID: ${it.id}")
                Text("Title: ${it.title}")
                Text("Body: ${it.body}")
                Text("User ID: ${it.userId}")
            }
        }
    }
}


@Composable
fun AnimationScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress }
        )
    }
}

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShowScaffold(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf("home") }

    val items = listOf(
        NavigationItem("Home", Icons.Filled.Home, "home"),
        NavigationItem("Search", Icons.Filled.Search, "search"),
        NavigationItem("Profile", Icons.Filled.Face, "profile")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Top App Bar") },
                actions = {
                    IconButton(onClick = { /* Handle more options */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More options")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Handle FAB click */ },
                containerColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedItem == item.route,
                        onClick = {
                            selectedItem = item.route
                            navController.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            Modifier.padding(innerPadding)
        ) {
            composable("home") { ScreenHome() }
            composable("search") { ScreenSearch() }
            composable("profile") { ScreenFace() }
        }
    }
}


@Composable
fun ScreenHome() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .background(color = Color.White)
    ) {

        Text(
            text = "This is Home Screen ",
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )
    }
}

@Composable
fun ScreenSearch() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .background(color = Color.White)
    ) {

        Text(
            text = "This is Search Screen ",
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )

    }
}

@Composable
fun ScreenFace() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .background(color = Color.White)
    ) {

        Text(
            text = "This is Face Screen ",
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )

    }
}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainUi() {
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
                label = { com.itextpdf.layout.element.Text(menuItem.title) },
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
