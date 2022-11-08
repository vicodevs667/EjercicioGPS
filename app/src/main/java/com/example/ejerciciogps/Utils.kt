package com.example.ejerciciogps

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
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

    //Función para convertir vectores a mapa de bits
    fun getBitmapFromVector(context: Context, resId: Int): Bitmap? {
        return AppCompatResources.getDrawable(context, resId)?.toBitmap()
    }
}







