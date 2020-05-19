package com.example.weanaklie.model.remote


import com.android.friendycar.model.remote.retrofitClient
import com.example.weanaklie.model.datamodel.SuggestResponse
import io.reactivex.Single
import retrofit2.http.*

val WAKILNE_API_DEP: WakelnieApi by lazy {
    retrofitClient.create(WakelnieApi::class.java)
}

interface WakelnieApi {
//my location aswan no result found for api so you can use this 26.2716025,50.2017993
    @GET("/api/v1/GenerateFS.php?&get_param=value")
    fun getSuggestDetail(@Query("uid") uid: String = "26.2716025,50.2017993"): Single<SuggestResponse>

}


