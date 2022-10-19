package com.example.ejerciciogps

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ejerciciogps.Constantes.KEY_NAME
import com.example.ejerciciogps.databinding.ActivityMainBinding
import com.example.ejerciciogps.databinding.ActivityPersistenciaBinding

class PersistenciaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersistenciaBinding
    //Variables de tipo SharedPreferences y variable Editor para trabajar en el documento
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersistenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Inicializar el trabajo de SharedPreferences.
        initializeSharedPreferences()
        loadData()
        binding.btnGuardar.setOnClickListener {
            saveData()
        }
    }

    private fun initializeSharedPreferences() {
        // si es la primera vez va a crear el archivo, si el archivo
        //ya existe, pues va a traer ese archivo
        sharedPreferences = getSharedPreferences("persistencia", MODE_PRIVATE)
        editor = sharedPreferences.edit() // abrieran el archivo en modo editable
    }

    private fun saveData() {
        val nombreCompleto = binding.etNombreCompleto.text.toString()
        //Guardar datos es escribir en el archivo asi que necesitas
        //el archivo en modo de escritura y eso es el editor
        editor.apply {
            //aca actualizas o escribes datos
            //en formato de clave valor
            putString(KEY_NAME, nombreCompleto)
        }.apply()// como guardar asincronamente
    }

    private fun loadData() {
        binding.txtNombre.text =
            sharedPreferences.getString(KEY_NAME, "vacio")
    }
}












