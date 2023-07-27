package com.example.secondproject.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.secondproject.R

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // get email
        var text = intent.extras?.getString("email")
        Toast.makeText(this, "You have successfully signed in with email address: ${text}", Toast.LENGTH_LONG).show()

    }

}