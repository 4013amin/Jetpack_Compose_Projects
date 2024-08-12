package com.example.ptoject.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                ShowScaffold()
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

data class navigationsData(
    val title: String,
    val selectedItem: ImageVector,
    val unselectedItem: ImageVector,
    val hasBag: Boolean,
    val BageNumber: Int
)


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShowScaffold(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) } // To manage the state of dropdown menu

    val items = arrayOf(
        navigationsData(
            title = "Home",
            selectedItem = Icons.Filled.Home,
            unselectedItem = Icons.Outlined.Home,
            hasBag = false,
            0
        ),
        navigationsData(
            title = "Search",
            selectedItem = Icons.Filled.Search,
            unselectedItem = Icons.Outlined.Search,
            hasBag = false,
            0
        ),
        navigationsData(
            title = "Home",
            selectedItem = Icons.Filled.Face,
            unselectedItem = Icons.Outlined.Face,
            hasBag = false,
            6
        )
    )
    val navSatate by remember {
        mutableStateOf(0)
    }

    Scaffold(
        modifier
            .fillMaxSize()
            .background(color = Color.White),
        topBar = {
            TopAppBar(
                title = { Text(text = "Top App Bar") },
                actions = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More options")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                NavigationBar {
                    items.forEachIndexed { index, navigationsData ->
                        NavigationBarItem(
                            selected = navSatate == index,
                            onClick = { navSatate == index },
                            icon = {
                                Icon(
                                    imageVector = if (navSatate == index) navigationsData.selectedItem
                                    else navigationsData.unselectedItem,
                                    contentDescription = navigationsData.title
                                )
                            })
                    }
                }

            }
        }

    ) {
        // Your screen content goes here

    }
}


@Preview(showBackground = true)
@Composable
private fun showScafffoledad() {
    ShowScaffold()
}
