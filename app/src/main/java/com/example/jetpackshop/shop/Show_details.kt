import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Show_details : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val name = intent.getStringExtra("name") ?: ""
            val price = intent.getStringExtra("price") ?: ""
            ShowDetailsContent(name, price)
        }
    }
}

@Composable
fun ShowDetailsContent(name: String, price: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Name: $name", fontSize = 20.sp)
        Text(text = "Price: $price", fontSize = 20.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun previewShowDetails() {
    ShowDetailsContent("Product Name", "Product Price")
}
