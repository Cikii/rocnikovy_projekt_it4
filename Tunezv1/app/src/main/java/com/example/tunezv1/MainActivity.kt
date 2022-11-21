package com.example.tunezv1

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor


const val REQUEST_CODE = 200

class MainActivity : AppCompatActivity() {
    private var permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //permisse
        permissionGranted = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        if(!permissionGranted){
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE )
        }

        val frequencyTxt = findViewById<TextView>(R.id.textF)
        val noteText = findViewById<TextView>(R.id.noteText)

        //mic
        val dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)

        //zjisteni frekvence
        val pdh = PitchDetectionHandler { res, _ ->
            val pitchInHz = res.pitch
            runOnUiThread { processPitch(pitchInHz, frequencyTxt, noteText) }
        }
        val pitchProcessor: AudioProcessor =
            PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050F, 1024, pdh)
        dispatcher.addAudioProcessor(pitchProcessor)

        val audioThread = Thread(dispatcher, "Audio Thread")
        audioThread.start()


    }

    private fun processPitch(pitchInHz: Float, frequencyTxt: TextView, noteText: TextView) {
        val sharp = arrayListOf<String>("A", "A#", "B", "C", "C#" ,"D", "D#", "E", "F", "F#", "G", "G#" )
        val flat = arrayListOf<String>("A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab" )

        var flatToSharp = findViewById<Button>(R.id.flatToSharp)
        var switcher = 0

        flatToSharp.setOnClickListener {
            println(switcher)
        }


        frequencyTxt.text = "$pitchInHz Hz"

        if (pitchInHz >= 100 && pitchInHz < 123.81){
            noteText.text = "${sharp[0]}"
        }
        else if(pitchInHz >= 123.47 && pitchInHz < 130.81) {
            //B
            noteText.text = "B"
        }
        else if(pitchInHz >= 130.81 && pitchInHz < 146.83) {
            //C
            noteText.text = "C"
        }
        else if(pitchInHz >= 146.83 && pitchInHz < 164.81) {
            //D
            noteText.text = "D"
        }
        else if(pitchInHz >= 164.81 && pitchInHz <= 174.61) {
            //E
            noteText.text = "E"
        }
        else if(pitchInHz >= 174.61 && pitchInHz < 185) {
            //F
            noteText.text = "F"
        }
        else if(pitchInHz >= 185 && pitchInHz < 196) {
            //G
            noteText.text = "G";
        }
    }

    //permisse
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==REQUEST_CODE  ){
            permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED
        }
    }
}