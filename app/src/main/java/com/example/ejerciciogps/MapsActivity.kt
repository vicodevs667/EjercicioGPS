package com.example.ejerciciogps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.ejerciciogps.Coordenadas.avaroa
import com.example.ejerciciogps.Coordenadas.hospitalObrero
import com.example.ejerciciogps.Coordenadas.jardinJapones
import com.example.ejerciciogps.Coordenadas.lapaz
import com.example.ejerciciogps.Coordenadas.plazaIsabelCatolica
import com.example.ejerciciogps.Coordenadas.univalle

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

    //Variable global que representa su mapa de Google
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Utils.binding = binding

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //Concepto clave y avanzado de Android.
        //Fragmentos: seccionar en piezas el diseño de UI
        //Fragmento que contiene el mapa de Google
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        //El mapa de Google se obtine de manera asíncrona
        //busca cargar el mapa sin congelar tu pantalla
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
    //Lo que contiene se ejecuta cuando el mapa
    //esta listo y cargado en tu pantalla
    //como parámetro recibes un objeto que contiene
    //el mapa listo para trabajar........
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //una posición.
        //se maneja un objeto que conjunciona latitud y longitud
        //ese objeto se llama LatLng
        val egipto = LatLng(29.977704826716288, 31.13258127087077)

        //Definir algunas configuraciones de su mapa
        //Los mapas manejan un zoom y este va de 0 a 21
        //donde jamas vas a poder usar el valor 0 o 1
        //el zoom minimo es de 2.....
        //el zoom máximo es de 20
        /*
            zoom de 20: se usa para edificios, construcciones
                        casas, parques, detalles muy finos.
            a partir de 15: se suele usar para calles
            a partir de 10: para ciudades
            a partir de 5: para paises y continente
            a partir de 2: continente y su masa continental
         */
        mMap.apply {
            //configurar el zoom mínimo que pueden usar en su mapa
            setMinZoomPreference(15f)
            //El máximo zoom que puede hacer el usuario
            setMaxZoomPreference(20f)
        }

        //Uso de marcadores o las famosas tachuelas rojas
        mMap.addMarker(MarkerOptions()
            .title("Mi viaje de ensueño")
            .snippet("${egipto.latitude}, ${egipto.longitude}")
            .position(egipto)
        )

        //Realizando el movimiento de la cámara a la posición
        //deseada por usted
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(egipto))
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(egipto, 18f))

        //Configurar una cámara personalizada
        /*val cameraUnivalle = CameraPosition.Builder()
            .target(univalle)//ubicación donde va a centrarse la cámara
            .zoom(16f)
            .tilt(45f)//ángulo de inclinación de la cámara
            .bearing(245f)// ángulo para cambiar orientación de vista del mapa 0-360
            .build()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraUnivalle))
         */
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(univalle, 18f))

        //Movimiento de cámara utilizando procesos en background
        //utilizando Corrutinas
        /*lifecycleScope.launch {
            delay(5000)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(avaroa,18f))
            delay(3_500)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(jardinJapones, 18f))
        }*/
        //Corrutina movimiento en la cámara pixel por pixel
        /*lifecycleScope.launch {
            delay(3_500)
            for (i in 0 .. 50) {
                mMap.animateCamera(
                    CameraUpdateFactory.scrollBy(0f, 150f)
                )
                delay(1_000)
            }
        }*/


        /**
         * Sesgo de mapas
         * Delimitar o concentrar al mapa en una zona determinada
         * Bounds
         */
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lapaz, 12f))
        //se basa en las coordenadas noreste y suroeste de la región donde
        //quieren trabajar
        val lapazBounds = LatLngBounds(plazaIsabelCatolica, hospitalObrero)
        lifecycleScope.launch {
            delay(4_000)
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(lapazBounds, 32))
            //punto central del cuadrante
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lapazBounds.center, 18f))
        }
        //Sesgando el mapa a solo interacción en esa región Bounds
        mMap.setLatLngBoundsForCameraTarget(lapazBounds)

        /**
         * Controles de UI del mapa y Gestures del mapa
         */
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true // botones de Zoom_in y Zoom_out
            isCompassEnabled = true // ver la brujula y la orientación del mapa
            isRotateGesturesEnabled = false //impide la rotación del mapa
            isMapToolbarEnabled = true// habilitar ir al mapa para ver rutas o tu marcador
        }

        //Darle un padding al mapa para hacer
        //que sus controles no se solapen con nuestro
        //menù de botones
        mMap.setPadding(0,0,0, Utils.dp(64))

        //Para ver trafico de la ciudad
        //mMap.isTrafficEnabled = true



        //Vamos a configurar el evento más simple
        //y útil de los mapas de google
        // el click en cualquier lugar del mapa
        mMap.setOnMapClickListener {
            mMap.addMarker(MarkerOptions()
                .title("Posicion Random")
                .snippet("${it.latitude}, ${it.longitude}")
                .draggable(true)
                .position(it)
            )
        }
    }
}








