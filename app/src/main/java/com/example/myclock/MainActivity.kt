package com.example.myclock

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.example.myclock.view.MyClock
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var clock: MyClock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clock = findViewById(R.id.clock)
        Timber.i("setting time")
        clock.setTime(Calendar.getInstance())
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }
}