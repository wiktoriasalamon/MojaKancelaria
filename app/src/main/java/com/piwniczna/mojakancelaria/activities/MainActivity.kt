package com.piwniczna.mojakancelaria.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.piwniczna.mojakancelaria.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}