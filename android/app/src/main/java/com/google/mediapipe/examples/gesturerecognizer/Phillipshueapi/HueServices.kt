package com.google.mediapipe.examples.gesturerecognizer.Phillipshueapi

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

//Get the Phillips Hue Bridge and light using retrofit
interface HueServices {

    //Get the username for the hue
    @POST("/api")
    fun createUser(@Body devicetype: Devicetype): Call<List<SuccessResponse>>
    @GET("api/{username}/lights")
    fun getLight(@Path("username") username:String?): Call<Map<String,HueLight>>

    //Change the actual light state to on or off
    @PUT("api/{username}/lights/{id}/state")
    fun updatelightState(
        @Path("username") username: String?,
        @Path("id") Lightid: String?,
        @Body lightstate: LightState?,

        ):Call<Void>
}




