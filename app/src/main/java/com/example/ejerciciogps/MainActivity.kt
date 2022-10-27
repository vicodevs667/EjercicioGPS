package com.example.ejerciciogps

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
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
import com.example.ejerciciogps.Constantes.INTERVAL_TIME
import com.example.ejerciciogps.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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

    private var latitud: Double = 0.0
    private var longitud: Double = 0.0
    private var distancia = 0.0
    private var contador = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fabGps.setOnClickListener {
            view -> enableGPSService()
        }
        binding.fabCoordenadas.setOnClickListener {
            initGPSService()
        }
    }

    //1) Métodos para habilitar GPS en celular.
    private fun enableGPSService() {
        if (!hasEnabledGPS()) {
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
        } else
            Toast.makeText(this, "ya tienes GPS habilitado", Toast.LENGTH_SHORT).show()
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

    //2) Métodos para evaluar permisos de uso del
    //sensor GPS
    //Para ver si el valor de los permisos en su App
    //que estan evaluando son lo que se considera como
    //permiso otorgado
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
    private fun checkPermission(): Boolean = ContextCompat.checkSelfPermission(baseContext,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(baseContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED


    //3) Métodos para la gestión y el manejo
    // de la Localización
    private fun initGPSService() {
        if (hasEnabledGPS()) {
            if (checkPermission()) {
                //Si tienes GPS habilitado y tienes los permisos necesarios
                //Solo en ese momento puedes usar el objeto de localización
                fusedLocation = LocationServices.getFusedLocationProviderClient(this)
                manageLocation()
            } else
                launchPermissionsAlert()
        } else
            goToGPSSettings()
    }

    private fun launchPermissionsAlert() {
        ActivityCompat.requestPermissions(this,
            PERMISSION_GRANTED,
            PERMISSION_ID)
    }

    @SuppressLint("MissingPermission")
    private fun manageLocation() {
        //Configurar un Listener que este pendiente de obtener Localizaciones
        //exitosamente
        fusedLocation.lastLocation.addOnSuccessListener {
            location -> requestNewLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocation() {
        //Es parecido a hacer un consumo de una API REST
        //tenemos que hacer una petición o un solicitud
        // al servicio GPS para que este nos devuelva una respuesta
        //esa respuesta se maneja como un término conocido por ustedes
        //llamado Callback

        //Configuración sencilla de una petición de Localización
        //La configuración que ahora se utiliza desde la versión
        //21 en adelante
        var myLocationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            INTERVAL_TIME
        ).setMaxUpdates(10)
            .build()
        //Esta configuración es para versiones 20 o inferiores
        //de la librería de Google Services para Localización
        /*var myLocationRequest = LocationRequest.create().apply {
            priority = Priority.PRIORITY_HIGH_ACCURACY
            numUpdates = 1
            interval = 0
            fastestInterval = 0
        }*/
        //Es una petición de Locación actualizada
        fusedLocation.requestLocationUpdates(myLocationRequest, myLocationCallback, Looper.myLooper())
    }

    //Configurar el Callback y la respuesta de llamada
    private val myLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val myLocation: Location? = locationResult.lastLocation
            if (myLocation != null) {
                var lastLatitude = myLocation.latitude
                var lastLongitude = myLocation.longitude
                binding.txtLatitud.text = lastLatitude.toString()
                binding.txtLongitud.text = lastLongitude.toString()
                if (contador > 0){
                    distancia = calculateDistance(lastLatitude, lastLongitude)
                    binding.txtDistancia.text = "$distancia mts."
                }
                //dos variables capturan las coordenadas
                //obtenidas por el sensor previo a una actualización
                latitud = myLocation.latitude
                longitud = myLocation.longitude
                contador++
                resolveAddressCoordinates()
            }
        }
    }

    private fun resolveAddressCoordinates() {
        //Geocoder es una clase que trabaja
        //con información de los mapas de Google por ejemplo
        val geocoder = Geocoder(this)
        //Obtendremos direcciones y estas llegan
        // en un arreglo
        try {
            val direcciones = geocoder.getFromLocation(
                latitud,
                longitud,
                1
            )
            binding.txtDireccion.text = direcciones.get(0).getAddressLine(0)
        } catch (e: Exception) {
            binding.txtDireccion.text = "No se pudo ubicar dirección"
        }
    }

    private fun calculateDistance(lastLatitud: Double,
    lastLongitude: Double): Double {
        val radioTierra = 6371.0 // kilómetros
        val diffLatitud = Math.toRadians(lastLatitud - latitud)
        val diffLongitud = Math.toRadians(lastLongitude - longitud)
        val sinLatitud = sin(diffLatitud / 2)
        val sinLongitud = sin(diffLongitud / 2)
        val resultado1 = Math.pow(sinLatitud, 2.0) +
                (Math.pow(sinLongitud, 2.0)
                        * cos(Math.toRadians(latitud))
                        * cos(Math.toRadians(lastLatitud))
                        )
        val resultado2 = 2 * Math.atan2(sqrt(resultado1),
        sqrt(1 - resultado1))
        val distance = (resultado2 * radioTierra) * 1000.0
        //devuelven la distancia en metros
        return distance
    }

}







