package com.university.ip.repository

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import java.util.*
import kotlin.collections.ArrayDeque

class FiltersUtils {

    var bitmapUndoStack = Stack<Bitmap>()
    var bitmapRedoStack = Stack<Bitmap>()
    lateinit var originalBitmap:Bitmap

    fun undo(filteredBitmap: Bitmap):Bitmap{
        if(bitmapUndoStack.count()==0)
            return originalBitmap

        val bitmap: Bitmap
            bitmap = bitmapUndoStack.pop()
            bitmapRedoStack.push(filteredBitmap)
        return bitmap
    }

    fun redo(filteredBitmap: Bitmap):Bitmap{
        if(bitmapRedoStack.count()==0)
            return originalBitmap

        val bitmap: Bitmap
            bitmap = bitmapRedoStack.pop()
            bitmapUndoStack.push(filteredBitmap)
        return bitmap
    }
    fun addInUndo(bitmap: Bitmap){
        bitmapUndoStack.push(bitmap)
    }
}