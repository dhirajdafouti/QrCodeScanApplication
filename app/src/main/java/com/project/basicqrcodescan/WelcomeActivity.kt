package com.project.basicqrcodescan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.project.basicqrcodescan.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var text: TextView
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        text = findViewById(R.id.text)
        button = findViewById(R.id.button_click)
        button.setOnClickListener {
            startScanningActivity()
        }

    }

    private fun startScanningActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}