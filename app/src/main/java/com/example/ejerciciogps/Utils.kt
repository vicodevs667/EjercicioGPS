package com.example.ejerciciogps

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
}










