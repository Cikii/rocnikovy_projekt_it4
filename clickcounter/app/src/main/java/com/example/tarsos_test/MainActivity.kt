package com.example.tarsos_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.test.platform.app.InstrumentationRegistry

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    val text1 = findViewById<TextView>(R.id.textView)
    val btnClickMe = findViewById<Button>(R.id.mybutton)
    val rst = findViewById<Button>(R.id.button2)
    var timesClicked = 0
        btnClickMe.setOnClickListener{
            timesClicked++
            //timesClicked += 1
            text1.text = timesClicked.toString()
            //Toast.makeText(this, "Správně jsi kliknul!", Toast.LENGTH_SHORT).show()
        rst.setOnClickListener {
            timesClicked=0
            text1.text = timesClicked.toString()
            Toast.makeText(this, "Resetováno", Toast.LENGTH_LONG).show()
         }
        }
    }
}
