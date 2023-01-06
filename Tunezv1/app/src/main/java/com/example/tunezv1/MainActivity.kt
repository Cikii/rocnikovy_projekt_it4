package com.example.tunezv1

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import com.ekn.gruzer.gaugelibrary.HalfGauge
import com.ekn.gruzer.gaugelibrary.Range
import kotlin.math.*


const val REQUEST_CODE = 200

class MainActivity : AppCompatActivity() {
    private var permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false
    private val c0 = 16.35160 // frekvence c0
    var isSharp = true
    private var chunked = chunk(recursiveNoteCounting(mutableListOf(c0)), 3, 0)  // 3 = zacina od "c"

    data class centsFrAndOct(val freq: Double, val oct: String, val rCent: Double)

   // notovy zapis
    fun chunk(frequencies: List<Double>, shift: Int, startingOctaveIndex: Int): List<centsFrAndOct> {
        val sharp = listOf("A", "A♯", "B", "C", "C♯" ,"D", "D♯", "E", "F", "F♯", "G", "G♯" )
        val flat = listOf("A", "B♭", "B", "C", "D♭", "D", "E♭", "E", "F", "G♭", "G", "A♭" )

        val listsf = if (isSharp) sharp else flat

        var at = shift - 1
        var atOctave = startingOctaveIndex - 1
        return frequencies.map {
            if (++at >= listsf.size) at = 0
            if (listsf[at] == listsf[3]) atOctave += 1
             centsFrAndOct(it,"${listsf[at]}$atOctave",pitchInHzToCents(it.toFloat()))
        }
    }

    // urceni nejblizzsi noty
    fun minimalDistance(chunkedList: List<centsFrAndOct>, target: Double) = chunkedList.minBy { kotlin.math.abs(it.freq - target) }

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
        val HalfGauge = findViewById<HalfGauge>(R.id.halfGauge)

        //zjisteni frekvence
        val pdh = PitchDetectionHandler { res, _ ->
            val pitchInHz = res.pitch
            runOnUiThread { processPitch(pitchInHz, frequencyTxt, noteText, HalfGauge) }
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




        HalfGauge.minValue = -40.0
        HalfGauge.maxValue = 40.0

        val range = Range()
        range.color = Color.parseColor("#d14747")
        range.from = -40.0
        range.to = -20.0


        val range2 = Range()
        range2.color = Color.parseColor("#ffbf47")
        range2.from = -20.0
        range2.to = -10.0


        val range3 = Range()
        range3.color = Color.parseColor("#47d14c")
        range3.from = -10.0
        range3.to = 10.0

        val range5 = Range()
        range5.color = Color.parseColor("#ffbf47")
        range5.from = 10.0
        range5.to = 20.0

        val range6 = Range()
        range6.color = Color.parseColor("#d14747")
        range6.from = 20.0
        range6.to = 40.0


        HalfGauge.addRange(range)
        HalfGauge.addRange(range2)
        HalfGauge.addRange(range3)
        HalfGauge.addRange(range5)
        HalfGauge.addRange(range6)

       // HalfGauge.value = pitchInHzToCents(pitchInHz)




    }

    fun pitchInHzToCents(pitchInHz: Float): Double {
        val semitonesPerOctave = 12.0
        val centsPerSemitone = 100.0
        val pitchInCents = (Math.log(pitchInHz / c0) / Math.log(2.0) * semitonesPerOctave) * centsPerSemitone
        return pitchInCents
    }





    // zapsani do textview
    private fun processPitch(pitchInHz: Float, frequencyTxt: TextView, noteText: TextView, halfGauge: HalfGauge) {
        var note  = minimalDistance(chunked, pitchInHz.toDouble())


        if(pitchInHz <= 0){
            frequencyTxt.text= "0 Hz"
            noteText.text = " "
        }else{
            frequencyTxt.text = "$pitchInHz Hz"
        }
            noteText.text = note.oct

        halfGauge.value = pitchInHzToCents(pitchInHz) - note.rCent

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