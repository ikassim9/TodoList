package com.ismail.todolist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.replace
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBarWithNavController(findNavController(R.id.fragment))
        val extra = intent.extras
        if(extra != null){
            if(extra.containsKey("key")){
                val fragmentTrasaction =supportFragmentManager.beginTransaction()
                fragmentTrasaction.replace(R.id.fragment, DetailFragment()
                )

            }
        }

        }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)

        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
