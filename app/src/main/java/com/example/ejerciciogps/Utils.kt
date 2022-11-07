package com.example.ejerciciogps

import androidx.viewbinding.ViewBinding

object Utils {
    var binding: ViewBinding? = null

    fun dp(pixeles: Int): Int {
        if (binding == null) return 0
        val escala = binding!!.root.resources.displayMetrics.density
        return (escala * pixeles + 0.5f).toInt()
    }
}