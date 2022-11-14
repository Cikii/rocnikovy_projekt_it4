package com.example.tunezv1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm


class MainActivity : AppCompatActivity() {

    val frequencyTxt = findViewById<TextView>(R.id.textF)
    val noteText = findViewById<TextView>(R.id.textN)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)
        //val frequencyTxt = findViewById<TextView>(R.id.textF)

        val pdh = PitchDetectionHandler { res, e ->
            val pitchInHz = res.pitch
            runOnUiThread { processPitch(pitchInHz) }
        }
        val pitchProcessor: AudioProcessor =
            PitchProcessor(PitchEstimationAlgorithm.FFT_YIN, 22050F, 1024, pdh)
        dispatcher.addAudioProcessor(pitchProcessor)

        val audioThread: Thread = Thread(dispatcher, "Audio Thread")
        audioThread.start()


    }
    fun processPitch(pitchInHz: Float) {

        frequencyTxt.setText("" + pitchInHz)

       if (pitchInHz >= 100 && pitchInHz < 123.81){
           noteText.text = "A"
       }
        else{
           noteText.text = "B"
       }
    }

    }




