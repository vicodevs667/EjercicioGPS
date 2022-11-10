package com.example.ejerciciogps

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.viewbinding.ViewBinding

object Utils {
    var binding: ViewBinding? = null

    fun dp(pixels: Int): Int {
        if (binding == null) return 0
        val scale = binding!!.root.resources.displayMetrics.density
        return (pixels * scale + 0.5f).toInt()
    }

    fun getBitmapFromVector(context: Context, resId: Int): Bitmap? {
        return AppCompatResources.getDrawable(context, resId)?.toBitmap()
    }
}







