package com.university.ip.ui.editor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.university.ip.R
import com.university.ip.repository.FiltersUtils
import com.university.ip.ui.main.MainActivity
import com.university.ip.util.files.FileSaver.Companion.IMAGE_MIME_TYPE
import com.university.ip.util.files.FileSaverLegacy
import org.opencv.android.OpenCVLoader

class EditorActivity : AppCompatActivity(), EditorContract.View, View.OnClickListener,
    FiltersAdapter.ItemClickListener, SeekBar.OnSeekBarChangeListener {

    override fun appContext(): Context = applicationContext
    private val TAG = "EditorActivity"

    private lateinit var backButton: ImageView
    private lateinit var saveButton: ImageView
    private lateinit var undoButton: ImageView
    private lateinit var redoButton: ImageView
    private lateinit var resetButton: ImageView

    private lateinit var imageView: ImageView
    private lateinit var filterList: RecyclerView
    private lateinit var seekBar1: SeekBar
    private lateinit var seekBar2: SeekBar
    private lateinit var seekBar3: SeekBar
    private lateinit var filterMapper: FiltersUtils

    private lateinit var adapter: FiltersAdapter

    private lateinit var fileSaver: FileSaverLegacy
    private lateinit var temporaryBitmap: Bitmap
    private lateinit var finalBitmap: Bitmap
    private lateinit var originalBitmap: Bitmap
    private lateinit var selectedFilter: String
    private lateinit var presenter: EditorPresenter

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        val layoutManager = LinearLayoutManager(appContext(), LinearLayoutManager.HORIZONTAL, false)
        filterList = findViewById(R.id.filters_list)
        filterList.layoutManager = layoutManager
        adapter = FiltersAdapter(appContext(), this)
        adapter.setMediaList(FILTERS_ARRAY);
        filterList.adapter = adapter
        filterMapper = FiltersUtils()

        seekBar1 = findViewById(R.id.seek_bar_editor1)
        seekBar2 = findViewById(R.id.seek_bar_editor2)
        seekBar3 = findViewById(R.id.seek_bar_editor3)

        undoButton = findViewById(R.id.undo_button)
        undoButton.setOnClickListener(this)

        redoButton = findViewById(R.id.redo_button)
        redoButton.setOnClickListener(this)

        resetButton = findViewById(R.id.reset_button)
        resetButton.setOnClickListener(this)

        backButton = findViewById(R.id.back_editor)
        backButton.setOnClickListener(this)

        imageView = findViewById(R.id.image_edited)

        fileSaver = FileSaverLegacy(appContext())
        saveButton = findViewById(R.id.save_button)
        saveButton.setOnClickListener(this)
        presenter = EditorPresenter()
        presenter.bindView(this)
        loadImage()
        openCvInit()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
    }

    private fun openCvInit() {
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCV not loaded");
        } else {
            Log.e(TAG, "OpenCV loaded");
        }
    }

    private fun loadImage() {
        val data = intent.getBundleExtra(INTENT_EXTRAS)
        val requestCode = intent.getIntExtra(REQUEST_CODE, 2)
        val resultCode = intent.getIntExtra(RESULT_CODE, Activity.RESULT_CANCELED)

        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == Activity.RESULT_OK && intent != null) {
                    val selectedImage = data.get("data") as Bitmap
                    temporaryBitmap = selectedImage
                    imageView.setImageBitmap(selectedImage)
                }
                1 -> if (resultCode == Activity.RESULT_OK && intent != null) {
                    val selectedImage = intent.data!!
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(
                        selectedImage,
                        filePathColumn, null, null, null
                    )
                    if (cursor != null) {
                        cursor.moveToFirst()

                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val picturePath = cursor.getString(columnIndex)
                        temporaryBitmap = BitmapFactory.decodeFile(picturePath)
                        originalBitmap = temporaryBitmap
                        finalBitmap = temporaryBitmap
                        filterMapper.originalBitmap = finalBitmap
                        imageView.setImageBitmap(temporaryBitmap)
                        cursor.close()
                    }
                }
            }
        }
    }

    companion object {
        const val INTENT_EXTRAS: String = "INTENT_EXTRAS"
        const val REQUEST_CODE: String = "REQUEST_CODE"
        const val RESULT_CODE: String = "RESULT_CODE"
        val FILTERS_ARRAY: List<String> = listOf("Brightness", "Contrast","Grayscale", "Sepia","Binary"," Median Blur","Gaussian Blur","High Pass Blur", "Unsharp Mask",
                "Zoom","Flip Vertically","Flip Horizontally","Flip both", "Rotate Left","Rotate Right","Adaptive Binarization","Bilateral Filter","Rotate at any angle")
        val FILTERS_SLIDER_ARRAY: List<String> = listOf("Brightness", "Contrast", "Binary"," Median Blur","Gaussian Blur", "Zoom", "Adaptive Binarization", "Bilateral Filter","RGB Contrast", "RGB Brightness"
                ,"Rotate at any angle")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_editor -> {
                finish()
            }
            R.id.save_button -> {
                val uri = fileSaver.getFileUri(IMAGE_MIME_TYPE) ?: return
                appContext().contentResolver.openOutputStream(uri)?.use { stream ->
                    finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                }
                startActivity(Intent(appContext(), MainActivity::class.java))
            }
            R.id.undo_button -> {
                    val beforeFilter = filterMapper.undo(finalBitmap)
                finalBitmap = beforeFilter
                    setBitmap(beforeFilter)
            }
            R.id.redo_button -> {
                filterMapper.addInUndo(finalBitmap)
                val beforeFilter = filterMapper.redo(finalBitmap)
                finalBitmap = beforeFilter
                setBitmap(beforeFilter)
            }
            R.id.reset_button -> {
                setBitmap(finalBitmap)
                finalBitmap = originalBitmap
                filterMapper.bitmapUndoStack.clear()
                filterMapper.bitmapRedoStack.clear()
            }
        }
    }

    override fun onItemClick(filter: String) {
        temporaryBitmap = finalBitmap
        selectedFilter = filter
        seekBar1.visibility = View.GONE
        seekBar2.visibility = View.GONE
        seekBar3.visibility = View.GONE

        if (FILTERS_SLIDER_ARRAY.indexOf(selectedFilter) >= 0) {
            seekBar1.visibility = View.VISIBLE

            seekBar1.setOnSeekBarChangeListener(this)
        } else {
            seekBar1.visibility = View.GONE
            seekBar2.visibility = View.GONE
            seekBar3.visibility = View.GONE
            when (FILTERS_ARRAY.indexOf(selectedFilter)) {
                2 -> {
                    filterMapper.addInUndo(finalBitmap)
                    finalBitmap = presenter.grayScale(temporaryBitmap)
                    return
                }
                3 -> {
                    filterMapper.addInUndo(finalBitmap)
                    finalBitmap = presenter.sepia(temporaryBitmap)
                    return
                }
                7 -> {
                    filterMapper.addInUndo(finalBitmap)
                    finalBitmap = presenter.highPassFilter(temporaryBitmap)
                    return
                }
                8 -> {
                    filterMapper.addInUndo(finalBitmap)
                    finalBitmap = presenter.unsharpMask(temporaryBitmap)
                    return
                }

                10 -> {
                    filterMapper.addInUndo(finalBitmap)
                    finalBitmap = presenter.flipPicture(temporaryBitmap, 0)
                    return
                }
                11 -> {
                    filterMapper.addInUndo(finalBitmap)
                    finalBitmap = presenter.flipPicture(temporaryBitmap, 1)
                    return
                }
                12 -> {
                    filterMapper.addInUndo(finalBitmap)
                    finalBitmap = presenter.flipPicture(temporaryBitmap, -1)
                    return
                }
                13 -> {
                    filterMapper.addInUndo(finalBitmap)
                    finalBitmap = presenter.rotatePicture(temporaryBitmap, 0)
                    return
                }
                14 -> {
                    filterMapper.addInUndo(finalBitmap)
                    finalBitmap = presenter.rotatePicture(temporaryBitmap, 1)
                    return
                }
                else -> return
            }
        }
        println(FILTERS_SLIDER_ARRAY.indexOf(selectedFilter))
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        println(progress)
        println(selectedFilter)
        seekBar2.visibility = View.GONE
        seekBar3.visibility = View.GONE
        seekBar1.max = 255
        seekBar2.max = 255
        seekBar3.max = 255

        when (FILTERS_SLIDER_ARRAY.indexOf(selectedFilter)) {
            0 -> {
                finalBitmap = presenter.brightness(temporaryBitmap, progress)
                filterMapper.addInUndo(finalBitmap)
                return
            }
            1 -> {
                finalBitmap = presenter.contrast(temporaryBitmap, progress)
                filterMapper.addInUndo(finalBitmap)
                return
            }
            2->{
                finalBitmap = presenter.binary(temporaryBitmap, progress)
                filterMapper.addInUndo(finalBitmap)
                return
            }
            3->{
                finalBitmap = presenter.medianBlur(temporaryBitmap, progress)
                filterMapper.addInUndo(finalBitmap)
                return
            }
            4->{
                seekBar1.visibility = View.VISIBLE
                seekBar2.visibility = View.VISIBLE
                finalBitmap = presenter.gaussianBlur(temporaryBitmap, progress, seekBar2.progress)
                filterMapper.addInUndo(finalBitmap)
                return
            }
            5->{
                finalBitmap = presenter.zoom(temporaryBitmap, progress)
                filterMapper.addInUndo(finalBitmap)
                return
            }
            6->{
                finalBitmap = presenter.adaptiveBinary(temporaryBitmap, progress)
                filterMapper.addInUndo(finalBitmap)
                return
            }
            7->{
                seekBar1.visibility = View.VISIBLE
                finalBitmap = presenter.bilateralFilter(temporaryBitmap, seekBar1.progress)
                filterMapper.addInUndo(finalBitmap)
                return
            }
            10->{
                seekBar1.visibility = View.VISIBLE
                seekBar2.visibility = View.VISIBLE
                seekBar3.visibility = View.VISIBLE
                finalBitmap = presenter.rotateAtAnyDegree(temporaryBitmap, seekBar1.progress)
                filterMapper.addInUndo(finalBitmap)
                return
            }
            else ->
            {
                seekBar1.visibility = View.GONE
                seekBar2.visibility = View.GONE
                seekBar3.visibility = View.GONE
                return
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {}

    override fun setBitmap(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }
}