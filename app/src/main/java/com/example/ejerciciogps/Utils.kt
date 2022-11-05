package com.example.ejerciciogps

import androidx.viewbinding.ViewBinding

object Utils {
    var binding: ViewBinding? = null

    fun dp(pixels: Int): Int {
        if (binding == null) return 0
        val scale = binding!!.root.resources.displayMetrics.density
        return (pixels * scale + 0.5f).toInt()
    }
}







