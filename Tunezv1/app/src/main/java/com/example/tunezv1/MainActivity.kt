package com.example.tunezv1

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import kotlin.math.*


const val REQUEST_CODE = 200

class MainActivity : AppCompatActivity() {
    private var permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false
    private val c0 = 16.35160 // frekvence c0
    var isSharp = true
    private var chunked = chunk(recursiveNoteCounting(mutableListOf(c0)), 3, 0)  // 3 = zacina od "c"


   // notovy zapis
    fun chunk(frequencies: List<Double>, shift: Int, startingOctaveIndex: Int): List<Pair<Double, String>> {
        val sharp = listOf("A", "A♯", "B", "C", "C♯" ,"D", "D♯", "E", "F", "F♯", "G", "G♯" )
        val flat = listOf("A", "B♭", "B", "C", "D♭", "D", "E♭", "E", "F", "G♭", "G", "A♭" )

        val listsf = if (isSharp) sharp else flat

        var at = shift - 1
        var atOctave = startingOctaveIndex - 1
        return frequencies.map {
            if (++at >= listsf.size) at = 0
            if (listsf[at] == listsf[3]) atOctave += 1
            it to ("${listsf[at]}$atOctave")
        }
    }

    // urceni nejblizzsi noty
    fun minimalDistance(chunkedList: List<Pair<Double, String>>, target: Double) = chunkedList.minBy { kotlin.math.abs(it.first - target) }

    //vypocet spravnych frekvenci
    fun recursiveNoteCounting(history: MutableList<Double>): List<Double> {
        if (history.last() > 1500) return history
        val xd = history.last() * (2.0).pow(1.0/12)
        history.add(xd)
        return recursiveNoteCounting(history)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
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

        var flatToSharp = findViewById<Switch>(R.id.flatToSharp)
        // switch f/s
        flatToSharp.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            isSharp = !isChecked
            chunked = chunk(recursiveNoteCounting(mutableListOf(c0)), 3, 0)
            println(isSharp)
        })

    }


    // zapsani do textview
    private fun processPitch(pitchInHz: Float, frequencyTxt: TextView, noteText: TextView) {
        var note  = minimalDistance(chunked, pitchInHz.toDouble())

        frequencyTxt.text = "$pitchInHz Hz"
        noteText.text = note.second

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