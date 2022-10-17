package com.example.ejerciciogps

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.ejerciciogps.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient

class MainActivity : AppCompatActivity() {
    //Definir valores globales de tu clase que no necesitan
    //inicialización y representan lo mismo como constantes
    //para todas las instancias de tu clase
    //Definir como constantes globales de esta clase
    companion object {
        val PERMISSION_GRANTED = arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    }


    private lateinit var binding : ActivityMainBinding

    //Nos indica si el GPS esta habilitado o no
    private var enabledGPS = false
    //Variable fundamental para el tratamiento de la información
    //que refiere a las coordenadas que nos va a devolver el servicio
    //del GPS.
    //Este objeto conjunciona todos los datos referentes a localización.
    //Fusiona toda la data referida a GPS. y la usaremos a partir
    // de una librería de los servicios de Google.
    private lateinit var fusedLocation: FusedLocationProviderClient
    //Vamos a necesitar un código de validación de permisos otorgados
    private val PERMISSION_ID = 42

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
                dialog, wich -> enabledGPS = false
            }
            .setCancelable(true)
            .show()
    }

    private fun goToGPSSettings() {
        enabledGPS = true
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    //Método para validar si tenemos o no habilitado el servicio GPS
    private fun hasEnabledGPS(): Boolean {
        //Gestor u orquestador de lo referente a tratamiento de
        //el GPS.......
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun evaluatePermissionsGranted(): Boolean {
        /*
        PackageManager.PERMISSION_GRANTED = a un valor que android considera para indicar
        si tu respectivo permiso en la app tiene o no habilitada esa opción
        Lo que android considera como permiso otorgado 0
        checkSelfPermission = evalua de acuerdo que al permiso que le pasas
        que valor numérico tiene ese permiso y esos valores numéricos
        representan si el permiso esta dado o no
         */
        return PERMISSION_GRANTED.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }
    }
    //Evaluar ambos permisos tanto COARSE como FINE
    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(baseContext,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(baseContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun manageLocation() {
        fusedLocation.lastLocation.addOnSuccessListener {
            location -> requestNewLocation()
        }
    }

}







