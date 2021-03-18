package com.university.ip.ui.editor

import android.graphics.Bitmap
import com.university.ip.ui.base.BaseContract

interface EditorContract {

    interface View : BaseContract.View {
        //view functions for each change of activity
        fun setBitmap(bitmap: Bitmap)
    }

    interface Presenter {
        //functions that are going to use our library
        fun brightness(bitmap: Bitmap, value: Int)

        fun contrast(bitmap: Bitmap, value: Int)

    }
}