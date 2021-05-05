package com.university.ip.ui.editor

import android.graphics.Bitmap
import com.university.ip.ui.base.BaseContract

interface EditorContract {

    interface View : BaseContract.View {
        fun setBitmap(bitmap: Bitmap)
    }

    interface Presenter {
        fun brightness(bitmap: Bitmap, value: Int): Bitmap

        fun contrast(bitmap: Bitmap, value: Int): Bitmap

        fun grayScale(bitmap: Bitmap): Bitmap

        fun sepia(bitmap: Bitmap): Bitmap

        fun binary(bitmap: Bitmap, value: Int): Bitmap

        fun medianBlur(bitmap: Bitmap, value: Int): Bitmap

        fun gaussianBlur(bitmap: Bitmap, value: Int, sigma: Int): Bitmap

        fun highPassFilter(bitmap: Bitmap): Bitmap

        fun unsharpMask(bitmap: Bitmap): Bitmap

        fun zoom(bitmap: Bitmap, value: Int): Bitmap

        fun flipPicture(bitmap: Bitmap, value: Int): Bitmap

        fun rotatePicture(bitmap: Bitmap, value: Int): Bitmap

        fun adaptiveBinary(bitmap: Bitmap, value: Int): Bitmap

        fun bilateralFilter(bitmap: Bitmap, val1: Int): Bitmap

        fun rotateAtAnyDegree(bitmap: Bitmap, val1: Int):Bitmap
    }
}