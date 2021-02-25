package com.university.ip.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.university.ip.R


class MainActivity : AppCompatActivity(), MainContract.View {

    override fun appContext(): Context = applicationContext

    private lateinit var presenter: MainPresenter

    /** Called when the activity is first created. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter()
        presenter.bindView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }

}