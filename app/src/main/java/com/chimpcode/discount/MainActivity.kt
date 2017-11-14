package com.chimpcode.discount

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)

//        for sampling
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
