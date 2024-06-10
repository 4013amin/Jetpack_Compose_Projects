import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackshop.R // مطمئن شوید که مسیر پکیج درست است

@Composable
fun UserProfile() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFE3F2FD)) // Background color for the entire screen
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.images),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape) // Circular image
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Alex Flores",
            style = TextStyle(fontSize = 24.sp, color = Color.Black, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color(0xFF1E88E5))
            Icon(
                Icons.Filled.Notifications,
                contentDescription = "Notifications",
                tint = Color(0xFF1E88E5)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .background(color = Color.White)
                .padding(16.dp), // Padding inside the notifications area
            verticalArrangement = Arrangement.spacedBy(8.dp) // Space between notifications
        ) {
            repeat(4) {
                NotificationItem()
            }
        }
    }
}

@Composable
fun NotificationItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.Notifications,
            contentDescription = "Notification Icon",
            tint = Color(0xFF1E88E5)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "This is a notification",
            style = TextStyle(fontSize = 16.sp, color = Color.Gray),
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun showUserProfilePreview() {
    UserProfile()
}
