package com.google.mediapipe.examples.gesturerecognizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.google.mediapipe.examples.gesturerecognizer.Phillipshueapi.HueApiViewModel

class MainLightScreen : AppCompatActivity() {
    private lateinit var viewModel: HueApiViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_light_screen)

        val lightbtn = findViewById<CardView>(R.id.light_up_card)
        val colorbtn = findViewById<CardView>(R.id.Rainbowcard)
        val usernametestbtn = findViewById<CardView>(R.id.Usernamebuttontest)
        viewModel = ViewModelProvider(this)[HueApiViewModel::class.java]

        lightbtn.setOnClickListener { view ->
            val intent = Intent(this@MainLightScreen,MainActivity::class.java)
            startActivity(intent)
        }

        colorbtn.setOnClickListener { view ->
            TODO()

        }

        usernametestbtn.setOnClickListener { view ->
            viewModel.getusername("LightGestureControl#AndroidPhone")
        }





    }
}