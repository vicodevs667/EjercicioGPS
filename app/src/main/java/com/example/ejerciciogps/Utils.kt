package com.example.ejerciciogps

import androidx.viewbinding.ViewBinding

object Utils {
    //Pantalla necesitan referencia
    //de la pantalla representada en código....
    var binding: ViewBinding? = null

    //Función para estimar los pixeles en base a
    //la densidad de pixeles por pantalla
    fun dp(pixeles: Int): Int {
        if (binding == null) return 0
        val escala = binding!!.root.resources.displayMetrics.density
        return (escala * pixeles + 0.5f).toInt()
    }
}







