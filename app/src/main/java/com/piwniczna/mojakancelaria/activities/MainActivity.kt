package com.piwniczna.mojakancelaria.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.clients.ClientsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                ClientsFragment()
        ).commit()

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