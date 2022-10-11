package com.example.ejerciciogps

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.example.ejerciciogps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fabGps.setOnClickListener {
            view -> enableGPSService()
        }
    }

    private fun enableGPSService() {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_text_title)
            .setMessage(R.string.dialog_text_description)
            .setPositiveButton(R.string.dialog_button_accept,
            DialogInterface.OnClickListener{
                dialog, wich -> goToGPSSettings()
            })
            .setNegativeButton(R.string.dialog_button_denied) {
                dialog, wich -> cancelGPS()
            }
    }

    private fun cancelGPS() {
        TODO("Not yet implemented")
    }

    private fun goToGPSSettings() {
        TODO("Not yet implemented")
    }
}







