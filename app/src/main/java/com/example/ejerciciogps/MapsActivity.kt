package com.example.ejerciciogps

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
import com.example.ejerciciogps.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMarkerDragListener {

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
        //mMap.addMarker(MarkerOptions().position(univalle).title("Univalle"))
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

        /**
         * estilo de mapa personalizado
         */
        mMap.setMapStyle(MapStyleOptions
            .loadRawResourceStyle(this,
            R.raw.my_map_style))

        /**
         * Configuración y personalización
         * de marcadores
         */
        val univalleMarker = mMap.addMarker(MarkerOptions()
            .position(univalle)
            .title("Mi universidad")
        )
        univalleMarker?.run {
            isDraggable = true
            //setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            //setIcon(BitmapDescriptorFactory.defaultMarker(170f))
            //setIcon(BitmapDescriptorFactory.fromResource(R.drawable.navigation))
            Utils.getBitmapFromVector(this@MapsActivity,
                R.drawable.ic_house_48)?.let {
                    setIcon(BitmapDescriptorFactory.fromBitmap(it))
            }
            rotation = 175f
            isFlat = true // el marcador rote o no con respecto al mapa
            setAnchor(0.5f, 0.5f)
        }

        /**
         * Eventos en marcadores
         */
        mMap.setOnMarkerClickListener(this)
        //En el draggable del marcador
        mMap.setOnMarkerDragListener(this)

        /**
         * Eventos para trazar lineas y formas entre puntos
         * Ejemplo de aplicacion trazado de rutas......
         */
        //Para dibujar una linea entre puntos, debes referenciar los puntos
        //en coordenas de LatLng en un arreglo....
        val rutas = mutableListOf(
            univalle,
            triangular,
            hospitalObrero,
            stadium
        )
        //Rutas se traza lineas y a esas lineas se las conoce como Polyline
        val polyline = mMap.addPolyline(PolylineOptions()
            .color(Color.MAGENTA)
            .width(4f)
            .geodesic(true) //toma en cuenta el radio de curvatura de la tierra.
            .clickable(true)
        )
        //polyline.points = rutas
        lifecycleScope.launch {
            val runtimeRoutes = mutableListOf<LatLng>()
            for (coordenada in rutas) {
                runtimeRoutes.add(coordenada)
                polyline.points = runtimeRoutes
                mMap.animateCamera(CameraUpdateFactory.newLatLng(coordenada))
                delay(2_500)
            }
        }
        //Personalizacion de Linea Polyline
        polyline.pattern = listOf(Dot(), Gap(16f), Dash(32f), Gap(16f))
        //Tipo de union de los trazos de las lineas
        polyline.jointType = JointType.BEVEL // ROUND que es redondear las intersecciones
        //polyline.width = 40f

        /**
         * Evento de click sobre la linea trazada Polyline
         */
        polyline.tag = "Ruta de Univalle a Stadium"
        mMap.setOnPolylineClickListener {
            Toast.makeText(this, "${polyline.tag}",
                Toast.LENGTH_SHORT).show()

        }



        //Trazar el tráfico en rutas cercanas a su ubicación
        //mMap.isTrafficEnabled = true

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

    override fun onMarkerClick(marker: Marker): Boolean {
        Toast.makeText(this, "${marker.position.latitude}, " +
                "${marker.position.longitude}", Toast.LENGTH_LONG).show()
        return false
    }

    override fun onMarkerDrag(marker: Marker) {
        marker.alpha = 0.4f
    }

    override fun onMarkerDragEnd(marker: Marker) {
        marker.alpha = 1.0f
        marker.snippet = "${marker.position.latitude}, ${marker.position.longitude}"
        //Para mostrar la ventana de información
        marker.showInfoWindow()
    }

    override fun onMarkerDragStart(marker: Marker) {
        marker.hideInfoWindow()
    }
}






