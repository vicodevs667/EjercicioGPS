package com.example.ejerciciogps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.ejerciciogps.Coordenadas.casaJhere
import com.example.ejerciciogps.Coordenadas.univalle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.ejerciciogps.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.CameraPosition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        //El mapa se carga asíncronamente
        //No satura tu proceso principal o de la UI
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    //Respuesta cuando el mapa esta listo para trabajar
    // el parámetro que tienen devuelve el mapa de
    // Google listo y configurado
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Delimitar el rango de Zoom de la cámara
        //para evitar que el usuario haga un
        //zoom in o out de la cámara
        mMap.apply {
            setMinZoomPreference(15f)
            setMaxZoomPreference(19f)
        }

        //define coordenadas en un objeto
        //LatLng que conjunciona latitud y longitud
        val vancouver = LatLng(49.24090600133714, -123.11394962161292)

        //Marcadores .... Tachuelas rojas
        mMap.addMarker(MarkerOptions()
            .position(vancouver)
            .title("Mi lugar favorito")
            .snippet("${vancouver.latitude}, ${vancouver.longitude}"))

        //Posicionar la cámara en la ubicación de
        //preferencia
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vancouver, 17f))

        /**
         * Configuración de su cámara personalizada
         */
        val camaraPersonalizada = CameraPosition.Builder()
            .target(univalle) // donde apunta la cámara
            .zoom(17f) // 15 y 18 calles  20 edificios
            .tilt(45f) //ángulo de inclinación de la cámara, no deberían ser agresivos con los ángulos
            .bearing(195f) //cambio de orientación de 0 a 360
            .build()
        //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camaraPersonalizada))

        /**
         * Movimiento de la cámara (animación de la cámara)
         * Plus--- uso standar de corrutinas
         */
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(univalle, 17f))
        //Corrutinas para apreciar mejor el movimiento
        lifecycleScope.launch {
            delay(5000)
            //Para mover la cámara entre puntos en el mapa
            //les recomiendo usar una animación que haga una transición
            // de movimiento... se usa el método
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(casaJhere, 17f))
        }




        //Evento de click sobre el mapa
        mMap.setOnMapClickListener {
            mMap.addMarker(MarkerOptions()
                .position(it)
                .title("Random Place")
                .snippet("${it.latitude}, ${it.longitude}")
                .draggable(true))
        }

        /**
         * Configuración de Controles de UI
         * y Gestures del mapa
         */
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true // controles de zoom
            isCompassEnabled = true // habilita el compás de la orientación
            isMapToolbarEnabled = true// botones complementarios del mapa
        }
    }



}












