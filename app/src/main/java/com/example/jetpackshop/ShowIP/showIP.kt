package com.example.jetpackshop.ShowIP

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.example.jetpackshop.ui.theme.JetPackShopTheme
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections

class ShowIP : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackShopTheme {
                VpnScreen()
            }
        }
    }
}


class MyVpnService : VpnService() {
    private var currentIP: String? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // شروع VPN
        val builder = Builder()

        builder.setSession("MyVPN")
        builder.addAddress("10.0.0.2", 24) // تنظیم آدرس داخلی
        builder.addDnsServer("8.8.8.8")    // تنظیم سرور DNS
        builder.addRoute("0.0.0.0", 0)     // ارسال کل ترافیک

        val vpnInterface = builder.establish() // شروع VPN

        // بررسی آی‌پی و نمایش تغییرات
        checkAndUpdateIP()

        return START_STICKY
    }

    private fun checkAndUpdateIP() {
        val newIP = getDeviceIP()
        if (newIP != null && newIP != currentIP) {
            currentIP = newIP
            Log.d("MyVpnService", "IP تغییر کرد: $currentIP")
        }
    }

    private fun getDeviceIP(): String? {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            for (networkInterface in Collections.list(interfaces)) {
                val addresses = networkInterface.inetAddresses
                for (inetAddress in Collections.list(addresses)) {
                    if (!inetAddress.isLoopbackAddress && inetAddress is InetAddress) {
                        val ip = inetAddress.hostAddress
                        if (ip.contains(".")) { // IPv4
                            return ip
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            Log.e("MyVpnService", "خطا در دریافت آی‌پی: ${ex.message}")
        }
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        currentIP = null
    }
}


@Composable
fun VpnScreen() {
    val context = LocalContext.current

    Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize()
    ) {
        Button(onClick = {
            val intent = VpnService.prepare(context)
            if (intent != null) {
                // هدایت به صفحه تنظیمات VPN
                (context as FragmentActivity).startActivityForResult(intent, 0)
            } else {
                // شروع VPN
                val vpnIntent = Intent(context, MyVpnService::class.java)
                context.startService(vpnIntent)
                Toast.makeText(context, "متصل شد", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "فعال کردن VPN")
        }

        Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

        Button(onClick = {
            val vpnIntent = Intent(context, MyVpnService::class.java)
            context.stopService(vpnIntent)
            Toast.makeText(context, "خارج شدید", Toast.LENGTH_SHORT).show()
        }) {
            Text(text = "خاموش کردن VPN")
        }
    }
}


