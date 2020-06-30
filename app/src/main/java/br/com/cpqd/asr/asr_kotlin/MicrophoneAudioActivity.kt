package br.com.cpqd.asr.asr_kotlin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.com.cpqd.asr.asr_kotlin.audio.MicAudioSource
import br.com.cpqd.asr.asr_kotlin.constant.ContentTypeConstants.Companion.TYPE_AUDIO_RAW
import br.com.cpqd.asr.asr_kotlin.constant.ContentTypeConstants.Companion.TYPE_JSON
import br.com.cpqd.asr.asr_kotlin.constant.ContentTypeConstants.Companion.TYPE_URI_LIST
import br.com.cpqd.asr.asr_kotlin.model.RecognitionConfig
import kotlinx.android.synthetic.main.activity_microphone_audio.*

class MicrophoneAudioActivity : AppCompatActivity(), SpeechRecognizerResult, View.OnClickListener {

    private val PERMISSION_REQUEST_RECORD_AUDIO: Int = 1
    private var isRecording = false

    private val recognitionConfig: RecognitionConfig = RecognitionConfig.Builder()
        .accept(TYPE_JSON)
        .contentType(TYPE_URI_LIST)
        .waitEndMilis(2000)
        .noInputTimeoutMilis(20000)
        .build()

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

        playMic.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when(isRecording) {
            false -> {
                Toast.makeText(this, "Gravando...", Toast.LENGTH_SHORT).show()
                audio = MicAudioSource(8000)
                audio?.startRecording()
                isRecording = true
            }
            true -> {
                Toast.makeText(this, "Enviando...", Toast.LENGTH_SHORT).show()
                audio?.stopRecording()

                progressMic.show()
                playMic.isEnabled = false

                val speech = SpeechRecognizer.Builder()
                    .serverURL("wss://speech.cpqd.com.br/asr/ws/v2/recognize/8k")
                    .credentials("felipe", "felipe.cpqd")
                    .listerning(this)
                    .config(recognitionConfig, "builtin:slm/general")
                    .build()

                audio?.let { speech.recognizer(it, TYPE_AUDIO_RAW) }

                isRecording = false
            }
        }
    }

    override fun onResult(result: String) {
        runOnUiThread {
            responseMic.text = result
            playMic.isEnabled = true
            progressMic.hide()
        }
    }

}