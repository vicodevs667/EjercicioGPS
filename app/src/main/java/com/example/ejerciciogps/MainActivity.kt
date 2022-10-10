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
        //Construir una ventana modal
        //usando conceptos de la prog. funcional
        //En kotlin cuando el último parámetro del método
        //es una funcion lambda se puede poner la funcion fuera de los paréntesis
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_text_title)
            .setMessage(R.string.dialog_text_description)
            .setPositiveButton(R.string.dialog_button_acept,
            DialogInterface.OnClickListener{
                dialog, wich -> goToEnableGPS()
            })
            .setNegativeButton(R.string.dialog_button_deny) {
                dialog, wich -> cancelGPS()
            }
            .setCancelable(true)
            .show()
    }

    private fun cancelGPS() {
        TODO("Not yet implemented")
    }

    private fun goToEnableGPS() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun hasGPSEnabled():Boolean {
        //LocationManager: gestor u orquestador o director de orquesta
        //o el que lleva la batuta
        var locationManager: LocationManager
        = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}







