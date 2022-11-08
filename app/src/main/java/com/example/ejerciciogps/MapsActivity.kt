package com.example.ejerciciogps

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.ejerciciogps.Coordenadas.casaJhere
import com.example.ejerciciogps.Coordenadas.cementerioJudios
import com.example.ejerciciogps.Coordenadas.lapaz
import com.example.ejerciciogps.Coordenadas.plazaAvaroa
import com.example.ejerciciogps.Coordenadas.univalle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.ejerciciogps.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

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
        //El mapa se carga asíncronamente
        //No satura tu proceso principal o de la UI
        mapFragment.getMapAsync(this)
        //activar evento listener de conjunto de botones
        setupToggleButtons()
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
    @SuppressLint("MissingPermission")
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
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(univalle, 17f))
        //Corrutinas para apreciar mejor el movimiento
        /*lifecycleScope.launch {
            delay(5000)
            //Para mover la cámara entre puntos en el mapa
            //les recomiendo usar una animación que haga una transición
            // de movimiento... se usa el método
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(casaJhere, 17f))
        }*/

        /**
         * Movmiento de cámara por pixeles
         * que puede ser horizontal, vertical o combinado
         */
        /*lifecycleScope.launch {
            delay(5_000)
            for (i in 0 .. 100) {
                mMap.animateCamera(CameraUpdateFactory.scrollBy(0f, 140f))
                delay(500)
            }
        }*/

        /**
         * Bounds para delimitar áreas de acción
         * en el mapa, armar sesgos.
         */
        val univalleBounds = LatLngBounds(plazaAvaroa, cementerioJudios)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lapaz, 15f))
        lifecycleScope.launch {
            delay(3_500)
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(univalleBounds, Utils.dp(32)))
            //Punto central del cuadrante definido
            delay(2000)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(univalleBounds.center, 18f))
        }
        // Como delimitar el área
        mMap.setLatLngBoundsForCameraTarget(univalleBounds)

        //Activar la posición actual en el mapa
        //Evaluar permisos de GPS.......
        mMap.isMyLocationEnabled = true




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
            isMyLocationButtonEnabled = true // activa el boton para posicionarte al centro del mapa
            isZoomControlsEnabled = true // controles de zoom
            isCompassEnabled = true // habilita el compás de la orientación
            isMapToolbarEnabled = true// botones complementarios del mapa
            isRotateGesturesEnabled = false //ya no pueden rotar el mapa
            isTiltGesturesEnabled = false //ya no pueden inclinar la cámara
            isZoomGesturesEnabled = true // habilitar o deshabilitar gesture de zoom
        }

        /**
         * configuración, personalización, estilos de mapa
         */
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))

        /**
         * Configuración y personalización de Marcadores
         * Estilos, formas y eventos
         */
        val univalleMarcador = mMap.addMarker(
            MarkerOptions().title("Mi universidad")
                .position(univalle)
        )
        univalleMarcador?.run {
            //setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))// cambio de color en base a tonos definidos por android
            //setIcon(BitmapDescriptorFactory.defaultMarker(74f))//cambio de color, con color Hue personalizado
            Utils.getBitmapFromVector(this@MapsActivity, R.drawable.ic_taxi_alert_64)?.let {
                setIcon(BitmapDescriptorFactory.fromBitmap(it))
            } //marcador personalizado a partir de imagenes vectoriales de la libreria de Android
            setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker))//uso de marcador personalizado
            rotation = 145f
            setAnchor(0.5f, 0.5f)// punto de rotación central
            isFlat = true // el marcador rota o no con el mapa
            isDraggable = true // se puede arrastrar el marcador
            snippet = "Texto alternativo"
        }
        //Eventos en markers
        mMap.setOnMarkerClickListener(this)
    }


    private fun setupToggleButtons() {
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

    override fun onMarkerClick(marker: Marker): Boolean {
        //marker es el marcador al que le has hecho click
        Toast.makeText(this, "${marker.position.latitude}, " +
                "${marker.position.longitude}",
        Toast.LENGTH_LONG).show()
        return false
    }


}












