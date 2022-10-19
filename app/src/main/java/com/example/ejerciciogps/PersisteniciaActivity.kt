package com.example.ejerciciogps

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ejerciciogps.Constantes.KEY_MONEY
import com.example.ejerciciogps.Constantes.KEY_NAME
import com.example.ejerciciogps.databinding.ActivityPersisteniciaBinding

/**
 * Clase para manipular persistencia de datos
 * utilizando la clase SHARED PREFERENCES
 * esta clase persiste datos sencillos, de manera
 * rápida en un archivo de texto plano al estilo Android
 * se caracteriza por guardar datos en formato:
 *      llave -> valor
 * OJITO: no sirve cuando el volumen de datos es muy amplio
 * generalmente se usa para: guardar preferencias de un usuario,
 * personalizaciones del usuario en la app, datos personales
 * de un usuario, como ser un perfil, settings de controles
 * de juego, o configuraciones in game..
 */
class PersisteniciaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersisteniciaBinding
    //Variables necesarias para usar SharedPreferences
    //1) la clase SharedPreference que va a crear o abrir el archivo
    //2) el archivo en formato de escritura que se llama editor
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersisteniciaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeSharedPreferences()
        loadData()
        binding.btnGuardar.setOnClickListener {
            saveData()
        }
    }

    private fun initializeSharedPreferences() {
        //La primera vez se crea el archivo en el celular
        //luego cada vez que ingresas busca ese archivo
        //si hay lo trae para su uso, sino lo crea
        sharedPreferences = getSharedPreferences("persistencia", MODE_PRIVATE)
        //es tener una variable donde este el archivo en formato de escritura
        editor = sharedPreferences.edit()
    }

    private fun saveData() {
       val nombreCompleto = binding.etNombreCompleto.text.toString()
        //cuando hay varios datos que guardar al archivo, les sugiero
        //usar funciones de scope.
        //no recomiendo guardar cadenas de datos string, conjuncionadas
        editor.apply {
            putString(KEY_NAME, nombreCompleto)
            putInt(KEY_MONEY, 100)
        }.apply()
        loadData()
    }

    private fun loadData() {
        binding.txtNombre.text =
            sharedPreferences.getString(KEY_NAME, "vacio")
    }
}












