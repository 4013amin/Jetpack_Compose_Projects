import android.content.Context
import androidx.core.content.edit

class SharedPrefsManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    var username: String?
        get() = sharedPreferences.getString("username", null)
        set(value) = sharedPreferences.edit { putString("username", value) }


    var Email: String?
        get() = sharedPreferences.getString("Email", null)
        set(value) = sharedPreferences.edit { putString("Email", value) }

}