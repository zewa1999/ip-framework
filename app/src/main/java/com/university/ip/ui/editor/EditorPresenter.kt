package com.university.ip.ui.editor

import android.graphics.Bitmap
import com.university.ip.repository.Operators
import com.university.ip.ui.base.BasePresenter

class EditorPresenter : BasePresenter<EditorContract.View>(), EditorContract.Presenter {

    private val operators = Operators()

    override fun brightness(bitmap: Bitmap, value: Int): Bitmap {
        val result = operators.increaseBrightness(bitmap, value)
        getView()?.setBitmap(result)
        return result;
    }

    override fun contrast(bitmap: Bitmap, value: Int): Bitmap {
        val result = operators.increaseContrast(bitmap, value)
        getView()?.setBitmap(result)
        return result;
    }

    override fun grayScale(bitmap: Bitmap): Bitmap {
        val result = operators.grayScale(bitmap)
        getView()?.setBitmap(result)
        return result;
    }

    override fun sepia(bitmap: Bitmap): Bitmap {
        val result = operators.sepia(bitmap)
        getView()?.setBitmap(result)
        return result;
    }

    override fun binary(bitmap: Bitmap, value: Int): Bitmap {
        val result = operators.binary(bitmap, value)
        getView()?.setBitmap(result)
        return result;
    }

    override fun medianBlur(bitmap: Bitmap, value: Int): Bitmap {
        val result = operators.medianBlur(bitmap, value)
        getView()?.setBitmap(result)
        return result;
    }

    override fun gaussianBlur(bitmap: Bitmap, value: Int, sigma: Int): Bitmap {
        val result = operators.gaussianBlur(bitmap, value, sigma)
        getView()?.setBitmap(result)
        return result;
    }

    override fun highPassFilter(bitmap: Bitmap): Bitmap {
        val result = operators.Laplacian(bitmap)
        getView()?.setBitmap(result)
        return result;
    }

    override fun unsharpMask(bitmap: Bitmap): Bitmap {
        val result = operators.unsharpMask(bitmap)
        getView()?.setBitmap(result)
        return result;
    }

    override fun zoom(bitmap: Bitmap, value: Int): Bitmap{
        val result = operators.zoomIn(bitmap, value)
        getView()?.setBitmap(result)
        return result;
    }

    override fun flipPicture(bitmap: Bitmap, value: Int): Bitmap {
        val result = operators.flip(bitmap, value)
        getView()?.setBitmap(result)
        return result;
    }

    override fun rotatePicture(bitmap: Bitmap, value: Int): Bitmap {
        val result = operators.rotate(bitmap, value)
        getView()?.setBitmap(result)
        return result
    }

    override fun adaptiveBinary(bitmap: Bitmap, value: Int): Bitmap {
        val result = operators.adaptiveBinary(bitmap, value)
        getView()?.setBitmap(result)
        return result;
    }

    override fun bilateralFilter(bitmap: Bitmap, val1: Int): Bitmap {
        val result = operators.bilateralFilter(bitmap, val1)
        getView()?.setBitmap(result)
        return result;
    }

    override fun rotateAtAnyDegree(bitmap: Bitmap, val1: Int): Bitmap {
        val result = operators.rotateAtAnyAngle(bitmap, val1)
        getView()?.setBitmap(result)
        return result;
    }
}