package com.example.ejerciciogps

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.ejerciciogps.Coordenadas.hospitalObrero
import com.example.ejerciciogps.Coordenadas.lapaz
import com.example.ejerciciogps.Coordenadas.stadium
import com.example.ejerciciogps.Coordenadas.torresMall
import com.example.ejerciciogps.Coordenadas.univalle

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

    //Es el objeto que va a contener a su mapa de Google
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
        //El mapa de Google se carga asíncronamente
        //sin congelar la pantalla o el hilo principal
        mapFragment.getMapAsync(this)
        //activar listener de conjunto de botones
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
    //Aquí ocurre toda la magia
    //cuando el mapa esta listo.........
    //cuando el mapa esta listo, les devuelve
    //en un parámetro llamada googleMap, el mapa
    //y todas sus características en ese objeto
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //Las posiciones se manejan en un objeto
        //que conjunciona latitud y longitud
        //se llama LatLng
        val egipto = LatLng(29.977469715789642, 31.132504839773578)

        //Marcador
        //Tachuela roja que se posiciona en el mapa donde quieren
        //ubicarse
        mMap.addMarker(MarkerOptions()
            .title("Pirámides de Giza")
            .snippet("${egipto.latitude}, ${egipto.longitude}") //Contenido extra
            .position(egipto)
        )

        /**
         * Delimitar el zoom permitido en el mapa
         */
        mMap.apply {
            setMinZoomPreference(15f)
            setMaxZoomPreference(20f)
        }

        //Colocar la cámara virtual en la posición requerida
        //en el mapa
        //La cámara se centra o coloca tus coordenadas
        //en el centro de la pantalla
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(egipto, 17f))

        /*
        El zoom en el mapa va en un rango de 0 a 21
        se le puede asignar desde 2 a 20
        a partir de 5: continentes
        a partir de 10: ciudades o paises
        a partir de 15: se usa para calles avenidas
        20: sirve para edificios, casas, parques, domicilios
         */

        /**
         * configuración personalizada de cámara
         */
        val camaraPersonalizada = CameraPosition.Builder()
            .target(univalle)
            .zoom(17f)
            .tilt(45f) // ángulo de inclinación de la cámara
            .bearing(245f) // ángulo para cambio de orientación del norte
            .build()
        //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camaraPersonalizada))

        /**
         * Movimiento de la cámara
         * usando Corrutinas: similares a hilos o procesos en background
         */
        /*mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(univalle, 17f))
        //uso de corrutinas......
        lifecycleScope.launch {
            delay(5000)
            //transición de movimiento entre dos coordenadas
            //simular movimiento en el mapa
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(stadium, 17f))
        }*/
        /**
         * movimiento de la cámara por pixeles en pantalla
         */
        /*lifecycleScope.launch {
            delay(5000)
            for (i in 0 .. 50) {
                mMap.animateCamera(CameraUpdateFactory.scrollBy(0f, 120f))
                delay(500)
            }
        }*/

        /**
         * Limitación de área de acción del mapa
         * usando sesgos de coordenadas de acción
         * esta característica de mapear un área de acción
         * se conoce como Bounds
         */
        //Bounds necesita dos posiciones: una surOeste y otra noreste que delimitan tu área de acción
        val lapazBounds = LatLngBounds(torresMall, hospitalObrero)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lapaz, 12f))
        lifecycleScope.launch {
            delay(3_500)
            //De la área que has delimitado tu puedes acceder al punto central del rectángulo imaginario
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lapazBounds.center, 18f))
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(lapazBounds, 32))
        }
        mMap.setLatLngBoundsForCameraTarget(lapazBounds)

        /**
         * Establecer los controles de UI del mapa y los Gestures
         */
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true // Botones + - zoom in zoom out
            isCompassEnabled = true // la brújula de orientación del mapa
            isMapToolbarEnabled = true // habilita para un marcador la opción de ir a ver una ruta o verlo en la app Mapa Google
            isRotateGesturesEnabled = false // deshabilitar la opción de rotación del mapa
            isTiltGesturesEnabled = false // deshabilitar la opción de rotación de la cámara
            isZoomGesturesEnabled = false // deshabilita las acciones de zoom con los dedos en el mapa
        }

        //establecer un padding al mapa
        mMap.setPadding(0,0,0,Utils.dp(64))// densidad de pixeles en pantalla

        /**
         * Estilo personalizado de mapa
         */
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.my_map_style))

        /**
         * configuración y personalización de marcadores
         */
        val univalleMarcador = mMap.addMarker(MarkerOptions()
            .title("Mi universidad")
            .position(univalle)
        )
        univalleMarcador?.run {
            //setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))//cambiar color marcador con opciones por defecto
            //setIcon(BitmapDescriptorFactory.defaultMarker(86f))//cambiar color marcador con color personalizado
            setIcon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant))//cambiar marcador con diseño personalizado
            rotation = 145f
            isFlat = true // el marcador rote o no rote con el mapa
            setAnchor(0.5f, 0.5f)
            isDraggable = true
        }
        //Eventos en marcadores
        mMap.setOnMarkerClickListener(this)
        //cuando la interfaz a implementar tiene muchos métodos
        //mejor haganlo de la forma tradicional
        mMap.setOnMarkerDragListener(this)

        /**
         * Trazado de lineas Areas y circulos en el mapa
         * trazar una linea entre dos puntos se llama Polyline
         */
        setupPolyline()

        //Mapas tienen eventos como los botones.
        // se configura listener que escuchen esos eventos
        //y resuelvan una acción ante ese evento
        //El evento más sencillo e importante en los mapas
        // es el click a cualquier parte del mapa de google
        mMap.setOnMapClickListener {
            //it es la posicion donde haces click con tu dedo
            mMap.addMarker(MarkerOptions()
                .title("Nueva ubicación Random")
                .snippet("${it.latitude}, ${it.longitude}")
                .position(it)
                .draggable(true)
            )
        }
    }

    private fun setupPolyline() {
        //Las lineas Polyline dependen de un arreglo o lista de coordenadas
        val misRutas = mutableListOf(univalle, stadium, hospitalObrero)
        val polyline = mMap.addPolyline(PolylineOptions()
            .color(Color.YELLOW)
            .width(10f)// ancho de la linea
            .clickable(true)// la linea puede ser clickeada
            .geodesic(true)// curvatura con respecto al radio de la tierra
        )
        //polyline.points = misRutas
        //trazar rutas en tiempo real simulando movimiento
        lifecycleScope.launch {
            val misRutasEnTiempoReal = mutableListOf<LatLng>()
            for (punto in misRutas){
                misRutasEnTiempoReal.add(punto)
                polyline.points = misRutasEnTiempoReal
                delay(2_000)
            }
        }

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

    //Click al marcador
    override fun onMarkerClick(marker: Marker): Boolean {
        //marker es el marcador al que le estas haciendo click
        Toast.makeText(this, "${marker.position.latitude}, " +
                "${marker.position.longitude}", Toast.LENGTH_LONG).show()
        return false
    }

    override fun onMarkerDrag(marker: Marker) {
        //mientras arrastras el marcador
        // ocultas el menu de botones y haces transparente el marcador
        binding.toggleGroup.visibility = View.INVISIBLE
        marker.alpha = 0.4f
    }

    override fun onMarkerDragEnd(marker: Marker) {
        //Cuando sueltas el marcador
        binding.toggleGroup.visibility = View.VISIBLE
        marker.alpha = 1.0f
        //los marcadores tienen una ventana de información
        //se le llama infoWindow
        marker.showInfoWindow()
    }

    override fun onMarkerDragStart(marker: Marker) {
        //cuando empiezas a arrastrar el marcador
        marker.hideInfoWindow()//oculta la ventana de información
    }
}






