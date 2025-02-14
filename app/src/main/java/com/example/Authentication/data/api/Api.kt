import android.net.Uri
import com.example.Authentication.data.models.ProfileResponse
import com.example.Authentication.data.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Api {

    @Multipart
    @POST("api/register/")
    suspend fun registerUser(
        @Part("username") username: String,
        @Part("password") password: String,
        @Part("email") email: String,
        @Part("credit") credit: Int,
        @Part image: List<Uri>
    ): Response<RegisterResponse>


    @GET("")
    suspend fun getProfile(): Response<ProfileResponse>

}
