package com.ismail.todolist

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBarWithNavController(findNavController(R.id.fragment))

    }


    // controls backPress arrow
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
         hideVirtualKeyboardOnBackPress()
        return navController.navigateUp() || super.onSupportNavigateUp()

    }

    // Hides  keyboard when back  button is pressed
    private fun hideVirtualKeyboardOnBackPress() {
        try {
            val inputMethodManager =
                this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
