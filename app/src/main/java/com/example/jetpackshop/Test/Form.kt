import android.annotation.SuppressLint
import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.jetpackshop.R
import com.example.jetpackshop.Tamrini.data.api.api_inter
import com.example.jetpackshop.Test.Api_Testi
import com.example.jetpackshop.Test.Model_Testi
import com.example.jetpackshop.Test.Utils_testi
import com.example.jetpackshop.shop.MyForm
import com.example.jetpackshop.shop.sendRequest
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class Form : androidx.activity.ComponentActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
                form()
            }
        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun form(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .padding(15.dp),
) {

    var input_name by remember { mutableStateOf("") }
    var input_number1 by remember { mutableStateOf("") }
    var input_number2 by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .background(
                color = colorResource(id = R.color.white),
                shape = RoundedCornerShape(10.dp),
            ),
        Alignment.Center,

        ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(CutCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedTextField(
                    value = input_name,
                    onValueChange = { input_name = it },
                    label = { Text("نام") }
                )
                OutlinedTextField(
                    value = input_number1,
                    onValueChange = { input_number1 = it },
                    label = { Text("یک عدد وارد کنید ") }
                )
                OutlinedTextField(
                    value = input_number2,
                    onValueChange = { input_number2 = it },
                    label = { Text("عدد دوم رو وارد کنید ") }
                )

                OutlinedButton(
                    onClick = {
                        senRequest(
                            input_name,
                            input_number1,
                            input_number2
                        )

                    },

                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("ثبت کردن")
                }
            }
        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
fun senRequest(
    name: String,
    number1: String,
    number2: String,
) {


}


//GlobalScope.launch(Dispatchers.IO) {
//    var response = try {
//        Utils_testi.api.send_request(
//            Model_Testi(
//                name = name,
//                number1 = number1,
//                number2 = number2
//            )
//        )
//    } catch (e: IOException) {
//        return@launch
//    } catch (e: HttpException) {
//        return@launch
//    }
//    if (response.isSuccessful && response.body() != null) {
//        Log.i("Amin_class_testi", "Request successful: ${response.message()}")
//    }
//}