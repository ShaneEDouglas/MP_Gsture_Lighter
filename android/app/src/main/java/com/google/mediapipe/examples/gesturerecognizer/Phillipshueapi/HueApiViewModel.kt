package com.google.mediapipe.examples.gesturerecognizer.Phillipshueapi

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Viewmodel to handle the api call
class HueApiViewModel(val context:Context): ViewModel() {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.0.250/") // Bridge ip address
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val hueservices = retrofit.create(HueServices::class.java)
    var lightid: String? = null

    private val sharedPref = context.getSharedPreferences("hue_pref",Context.MODE_PRIVATE)

    // Will retrieve the username from the shared preferences which allows it to keep the same username for the bridge
    var userName: String? = sharedPref.getString("USERNAME_KEY",null)

    init {
        if (userName == null){
           Log.d("Username Warning", "Press the button on the hue bridge")
        } else {
            fetchlights()
        }
        //immediately will get the light info
    }

    fun getusername(devicetype: String?) {
        hueservices.createUser(Devicetype("HomeBridge")).enqueue(object: Callback<List<SuccessResponse>>{
            override fun onResponse(
                call: Call<List<SuccessResponse>>,
                response: Response<List<SuccessResponse>>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null && responseBody.isNotEmpty()) {
                    userName = responseBody[0].success.username
                    with(sharedPref.edit()){
                        putString("USERNAME_KEY", userName)
                        apply()
                    }
                }

                if (userName != null) {
                    Log.d("Username", "username found: $userName")
                } else {
                    Log.e("Username", "Username not found")
                }
            }

            override fun onFailure(call: Call<List<SuccessResponse>>, t: Throwable) {
                Log.e("Username Failure", "Username failed to be retrieved: $t")
            }

        })
    }

    fun fetchlights() {
        hueservices.getLight("$userName").enqueue(object: Callback<Map<String,HueLight>>{
            override fun onResponse(
                call: Call<Map<String, HueLight>>, response: Response<Map<String, HueLight>>) {
                if (response.isSuccessful){
                    val lights = response.body()

                    if (lights != null && !lights.isNullOrEmpty()) {

                        lightid = lights.keys.first()
                        Log.e( "light data", "Light ID retrieved: $response")
                    } else {
                        Log.e("No light data", "Failed to get light id: $response")
                    }
                }
            }

            override fun onFailure(call: Call<Map<String, HueLight>>, t: Throwable) {
                Log.e("Failed to make light Call", "Light network call failed: $t")
            }
        })
    }

    fun togglelight(isOn: Boolean?,Bri: Int?){
        if (lightid == null) {
            return
        }

        val lightstates = LightState(isOn)


        hueservices.updatelightState("$userName",lightid,lightstates).enqueue(object: Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful) {
                    // Successfully updated light state
                    Log.d("HueActivity Success", "Successfully updated light state")
                } else {
                    // Handle error...
                    Log.e("HueActivity Success", "Failed to update light state: ${response.errorBody()?.string()}")

                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("HueActivity Failure", "Network error occurred: ${t.message}")
            }

        })

    }

}