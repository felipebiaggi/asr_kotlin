package br.com.cpqd.asr.asr_kotlin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.com.cpqd.asr.asr_kotlin.audio.MicAudioSource
import br.com.cpqd.asr.asr_kotlin.constant.ContentTypeConstants.Companion.TYPE_AUDIO_RAW
import br.com.cpqd.asr.asr_kotlin.constant.ContentTypeConstants.Companion.TYPE_OCTET_STREAM
import kotlinx.android.synthetic.main.activity_file_audio.*
import kotlinx.android.synthetic.main.activity_microphone_audio.*
import kotlin.concurrent.thread
import kotlin.math.log

class MicrophoneAudioActivity : AppCompatActivity(), View.OnTouchListener, SpeechRecognizerResult {

    private val PERMISSION_REQUEST_RECORD_AUDIO: Int = 1

    private var audio: MicAudioSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_microphone_audio)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSION_REQUEST_RECORD_AUDIO
            )
        }

        playMic.setOnTouchListener(this)

    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Toast.makeText(this, "Gravando...", Toast.LENGTH_SHORT).show()
                audio = MicAudioSource(8000).also {
                    it.startRecording()
                }
                return false
            }
            MotionEvent.ACTION_UP -> {
                Toast.makeText(this, "Enviando...", Toast.LENGTH_SHORT).show()
                audio?.stop()


                progressMic.show()
                playMic.isEnabled = false

                thread(start = true, isDaemon = true, name = "ResultThreadMic") {

                    Thread.sleep(1000)

                    val speech = SpeechRecognizer.Builder()
                        .serverURL("wss://speech.cpqd.com.br/asr/ws/v2/recognize/8k")
                        .credentials("felipe", "felipe.cpqd")
                        .result(this)
                        .build()

                    audio?.let {
                        speech.recognizer(it, TYPE_AUDIO_RAW)
                    }

                    /*val result = speech.waitRecognitionResult()

                    result?.let { result ->
                        if (result.alternatives.isNotEmpty()) {
                            responseMic.text = result.alternatives.first().text
                        }
                    }*/

                    //don't do it
                    runOnUiThread {
                        playMic.isEnabled = true
                        progressMic.hide()
                    }

                }

                return false
            }
        }
        return false
    }

    override fun callback(result: String) {
        runOnUiThread {
            responseMic.text = StringBuilder("${responseMic.text} $result")
        }
    }

}