package com.university.ip.ui.main

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.university.ip.R
import com.university.ip.model.Photo


class MainActivity : AppCompatActivity(), MainContract.View, View.OnClickListener,
    PhotosDisplayAdapter.ItemClickListener {

    override fun appContext(): Context = applicationContext

    private lateinit var photosRecycler: RecyclerView
    private lateinit var noPhotosText: TextView
    private lateinit var addButton: FloatingActionButton
    private lateinit var presenter: MainPresenter
    private lateinit var adapter: PhotosDisplayAdapter
    private var photoList: List<Photo>? = null

    /** Called when the activity is first created. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        photosRecycler = findViewById(R.id.photos_display)
        photosRecycler.layoutManager = GridLayoutManager(appContext(), 2)


        noPhotosText = findViewById(R.id.no_photos_text)
        addButton = findViewById(R.id.add_button)
        addButton.setOnClickListener(this)

        presenter = MainPresenter()
        presenter.bindView(this)

        adapter = PhotosDisplayAdapter(appContext(), this)
        photosRecycler.adapter = adapter

//        photoList = presenter.getAllPicturesFromFolder(appContext()) - this is where we initialize the photoList
        photoList = listOf()
        adapter.setMediaList(photoList!!)

        toggleVisibility()
    }

    private fun toggleVisibility() {
        if (photoList!!.isEmpty()) {
            noPhotosText.visibility = View.VISIBLE
            photosRecycler.visibility = View.GONE
        } else {
            noPhotosText.visibility = View.GONE
            photosRecycler.visibility = View.VISIBLE
        }
    }

    override fun onItemClick(photo: Photo) {
        println(photo.path)
        println(photo.name)
    }

    override fun onClick(v: View?) {
        makeToast("Ana are mere", appContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }

}