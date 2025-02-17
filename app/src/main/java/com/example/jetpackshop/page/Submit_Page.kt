import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class Submit_Page : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VerificationCodeScreen()
        }
    }
}
    
@Composable
fun VerificationCodeScreen() {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly // یا Arrangement.CenterHorizontally
    ) {
        repeat(4) {
            RoundedButton(
                text = "Button $it",
                onClick = { /* اقدامی که انجام شود */ },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}


@Composable
fun RoundedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier.size(100.dp)
    ) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSubmitPage() {
    VerificationCodeScreen()
}
