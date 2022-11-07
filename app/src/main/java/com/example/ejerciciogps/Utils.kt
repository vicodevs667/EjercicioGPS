package com.example.ejerciciogps

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.viewbinding.ViewBinding

object Utils {
    var binding: ViewBinding? = null

    //densidad de pixeles en pantalla
    fun dp(pixels: Int): Int {
        if (binding == null) return 0
        val scale = binding!!
            .root.resources
            .displayMetrics.density
        return (scale * pixels + 0.5f).toInt()
    }

    //Método para convertir de Vector a Mapa de Bits
    fun getBitmapFromVector(context: Context, resId: Int): Bitmap? {
        return AppCompatResources.getDrawable(context, resId)?.toBitmap()
    }
}










