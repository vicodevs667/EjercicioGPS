package com.example.ejerciciogps

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationCompat
import com.example.ejerciciogps.databinding.ActivityMainBinding
import com.google.android.gms.location.*
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
        binding.fabCoordenadas.setOnClickListener {
            initGpsService()
        }
    }

    @SuppressLint("MissingPermission")
    private fun initGpsService() {
        if (hasGPSEnabled()) {
            if (hasPermissionsGranted()) {
                //vamos a inicializar la variable
                //que contiene toda la información en tiempo real
                //de nuestra localización si y si solo si
                //tenemos GPS habilitado y hay permisos otorgados
                //fusedLocation = FusedLocationProviderClient(this)
                fusedLocation = LocationServices.getFusedLocationProviderClient(this)
                fusedLocation.lastLocation.addOnSuccessListener{
                    location -> manageLocationData()
                }
            } else
                requestPermissionLocation()
        } else
            goToEnableGPS()
    }

    private fun requestPermissionLocation() {
        ActivityCompat.requestPermissions(this, PERMISSIONS_ENABLED, PERMISSION_ID)
    }

    //@SuppressLint("MissingPermission")
    //solo usar cuando tu estas 100% seguro de que ya
    //hiciste las correspondientes evaluaciones de permisos
    @SuppressLint("MissingPermission")
    private fun manageLocationData() {
        //Configurar una petición de Localización para obtener datos de Coordenadas
        var myLocationRequest = LocationRequest.create().apply {
            priority = Priority.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
        }
        //fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        fusedLocation.requestLocationUpdates(myLocationRequest, myLocationCallback, Looper.myLooper())
    }

    //Callback o funcion de respuesta a tu petición de solicitud
    //de coordenadas, devolviendote la última Localización posible desde
    //el servicio o el sensor
    var myLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            var myLastLocation: Location? = locationResult.lastLocation
            if (myLastLocation != null) {
                binding.txtLatitud.text = myLastLocation.latitude.toString()
                binding.txtLongitud.text = myLastLocation.longitude.toString()
            } else
                Toast.makeText(applicationContext, "No se pudo capturar coordendas", Toast.LENGTH_SHORT).show()
        }
    }

    //Métodos para habilitar GPS en dispositivo
    private fun enableGPSService() {
        //Construir una ventana modal
        //usando conceptos de la prog. funcional
        //En kotlin cuando el último parámetro del método
        //es una funcion lambda se puede poner la funcion fuera de los paréntesis
        if (!hasGPSEnabled()) {
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

    //Métodos para evaluar y habilitar permisos de GPS en la APP

    //Método para verificar si tienes los permisos de geolocalización
    //otorgados por el usuario.......
    private fun isGPSPermissionsEnabled(): Boolean = ContextCompat.checkSelfPermission(this,
        android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this,
        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

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







