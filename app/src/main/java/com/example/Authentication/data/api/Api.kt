import com.example.Authentication.data.models.ProfileResponse
import com.example.Authentication.data.models.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Api {

    @Multipart
    @POST("site/register/")
    suspend fun registerUser(
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody,
        @Part("email") email: RequestBody,
        @Part("credit") credit: RequestBody,
        @Part images: MultipartBody.Part
    ): Response<RegisterResponse>


    @GET("site/users/")
    suspend fun getProfile(): Response<ProfileResponse>

}
