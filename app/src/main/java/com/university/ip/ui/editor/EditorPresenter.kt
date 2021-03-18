package com.university.ip.ui.editor

import android.graphics.Bitmap
import com.university.ip.repository.Operators
import com.university.ip.ui.base.BasePresenter

class EditorPresenter : BasePresenter<EditorContract.View>(), EditorContract.Presenter {

    private val operators = Operators()

    override fun brightness(bitmap: Bitmap, value: Int) {
        val result = operators.increaseBrightness(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun contrast(bitmap: Bitmap, value: Int) {
        val result = operators.increaseContrast(bitmap, value)
        getView()?.setBitmap(result)
    }

}