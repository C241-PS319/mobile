package com.c241ps319.patera.view

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.c241ps319.patera.R
import com.c241ps319.patera.view.history.HistoryActivity

open class BaseClass: AppCompatActivity() {

    fun setActionBar(){
        supportActionBar?.setCustomView(R.layout.app_bar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.green)))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val historyIntent = Intent(this, HistoryActivity::class.java)
        startActivity(historyIntent)
        return super.onOptionsItemSelected(item)
    }

}