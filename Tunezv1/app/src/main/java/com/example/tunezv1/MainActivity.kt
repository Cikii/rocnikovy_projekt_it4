package com.example.tunezv1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.android.AudioDispatcherFactory.fromDefaultMicrophone
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //AudioDispatcher d = AudioDispatcher.fromDefaultMicrophone(1024, 0)

    }


    }




