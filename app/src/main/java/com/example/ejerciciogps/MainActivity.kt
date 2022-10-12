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
import androidx.core.location.LocationCompat
import com.example.ejerciciogps.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    //Constante global que no necesita una inicialización y que todas
    //las instancias de esta clase lo pueden acceder.....
    //Aca deben definir constantes globables para su clase
    companion object {
        val PERMISSIONS_ENABLED = arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
    private lateinit var binding : ActivityMainBinding

    //Indicar si tenemos GPS habilitado
    private var enabledGPS = false
    //Constante para validar si los permisos han sido otorgados
    private val PERMISSION_ID = 42
    //esta clase les va a permitir tener la data sobre localización
    //conjuncionada en un solo objeto, es un cliente
    //que te va a ofrecer toda la información posible del sensor o del servicio
    //sobre datos de gps.
    private lateinit var fusedLocation: FusedLocationProviderClient

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
                dialog, wich -> enabledGPS = false
            }
            .setCancelable(true)
            .show()
    }


    private fun goToEnableGPS() {
        enabledGPS = true
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

    //Método para verificar si tienes los permisos de geolocalización
    //otorgados por el usuario.......
    private fun hasPermissionsGranted(): Boolean {
        return PERMISSIONS_ENABLED.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }
        //PackageManager.PERMISSION_GRANTED = 0
        // en el celular coarse gps aprox => permiso = 0 ------ Si en Android 0 = Otorgado
        // poscion 0: Coarse Location permission = 0
        // elemento 0 y compara con el valor de Permiso otorgado
        // 0 == 0 -> True
        //all agarra y aplica la evaluación a cada elemento de tu array
        // posicion 1: Fine Location permission = 0
        // elemento 1 lo compara con valor de permiso otorgado
        // 0 == 0 -> True
    }
}







