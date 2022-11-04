package com.example.ejerciciogps

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.ejerciciogps.Coordenadas.hospitalObrero
import com.example.ejerciciogps.Coordenadas.isabelCatolica
import com.example.ejerciciogps.Coordenadas.lapaz
import com.example.ejerciciogps.Coordenadas.maternoInfantil
import com.example.ejerciciogps.Coordenadas.megacenter
import com.example.ejerciciogps.Coordenadas.stadium
import com.example.ejerciciogps.Coordenadas.triangular
import com.example.ejerciciogps.Coordenadas.univalle
import com.example.ejerciciogps.Coordenadas.valleLuna

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.ejerciciogps.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Utils.binding = binding

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        enableToggleButtons()
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
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // el rango de Zoom va de 0 a 21 donde 20 el tope
        // 20: se usa para edificios, construcciones, parques, casas
        // 15: es bueno para ver calles
        // 10: ciudad....
        // 5: pais y continente
        //es que ustedes pueden delimitar al usuario
        // el acercamiento y alejamiento del Zoom
        mMap.apply {
            setMinZoomPreference(14f)
            setMaxZoomPreference(18f)
        }

        //Agregar marcador (tachuela roja)
        mMap.addMarker(MarkerOptions().position(univalle).title("Univalle"))
        /*mMap.addMarker(MarkerOptions()
            .position(stadium)
            .title("Hernando Siles")
            .draggable(true)
        )*/
        //metodo que valida el movimiento o animación de la cámara
        //virtual del mapa
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(univalle))
        //Configurar una cámara personalizada
        /*val cameraUnivalle = CameraPosition.Builder()
            .bearing(0f) // nueva orientación nuevo norte
            .tilt(0f) //ángulo superior de la cámara
            .zoom(16f)
            .target(univalle)
            .build()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraUnivalle))*/
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lapaz, 12f))
        //Movimiento de la cámara usando corrutinas.
        /*lifecycleScope.launch {
            //pasa en segundo plano
            //Similar a los hilos que conocen
            delay(5_000)
            //Método para animar la transición de la cámara
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(valleLuna, 16f))
            delay(3_500)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(megacenter, 16f))
        }*/
        /*val cameraLaPaz = CameraPosition.Builder()
            .bearing(120f)
            .tilt(20f)
            .target(univalle)
            .zoom(15f)
            .build()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraLaPaz))
        lifecycleScope.launch {
            delay(3_500)
            for (i in 0 .. 50) {
                mMap.animateCamera(CameraUpdateFactory.scrollBy(0f, 400f))
                delay(1_500)
            }
        }*/

        //Configuracion de Bounds en el mapa
        //Posterior sesgo de movilidad en el mapa
        //Bounds: con respecto a suroeste y noreste
        val lapazBounds = LatLngBounds(isabelCatolica, hospitalObrero)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lapaz, 12f))
        lifecycleScope.launch {
            delay(3_500)
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(univalle, 17f))
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(lapazBounds, Utils.dp(32)))
            //lapazBounds.center // es la posición central de la zona
        }
        mMap.setLatLngBoundsForCameraTarget(lapazBounds)

        //Como habilitar el punto de indicación azul
        //de mi coordenada en tiempo real en el mapa
        mMap.isMyLocationEnabled = true

        //Configuración de Controles en UI y gestures en el mapa
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true//habilitar botones zoom_in y zoom_out
            isCompassEnabled = true// habilitar brujula
            isRotateGesturesEnabled = false // habilita o deshabilita la propiedad de rotación del mapa
            isMapToolbarEnabled = true // habilitarse acceso al mapa de google para rutas y otros
            isMyLocationButtonEnabled = true // habilita el boton para centrar tu posición
        }

        //Padding al mapa para que este no se solape con componentes que incorporan al diseño
        mMap.setPadding(0, 0, 0, Utils.dp(64))

        //Trazar el tráfico en rutas cercanas a su ubicación
        mMap.isTrafficEnabled = true

        //Evento sobre el mapa
        //Evento click sobre el mapa en cualquier posición
        mMap.setOnMapClickListener {
                mMap.addMarker(MarkerOptions()
                    .position(it)
                    .title("mi nueva posicion")
                    .snippet("${it.latitude}, ${it.longitude}"))
            mMap.moveCamera(CameraUpdateFactory
                .newLatLng(it))
        }
    }

    private fun enableToggleButtons() {
        binding.toggleGroup.addOnButtonCheckedListener {
                group, checkedId, isChecked ->
            if (isChecked) {
                mMap.mapType = when(checkedId) {
                    R.id.btnNormal -> GoogleMap.MAP_TYPE_NORMAL
                    R.id.btnHibrido -> GoogleMap.MAP_TYPE_HYBRID
                    R.id.btnSatelital -> GoogleMap.MAP_TYPE_SATELLITE
                    R.id.btnTerreno -> GoogleMap.MAP_TYPE_TERRAIN
                    else -> GoogleMap.MAP_TYPE_NONE
                }
            }
        }
    }
}






