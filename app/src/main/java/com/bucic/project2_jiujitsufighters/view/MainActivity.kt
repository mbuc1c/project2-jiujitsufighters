package com.bucic.project2_jiujitsufighters.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bucic.project2_jiujitsufighters.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}