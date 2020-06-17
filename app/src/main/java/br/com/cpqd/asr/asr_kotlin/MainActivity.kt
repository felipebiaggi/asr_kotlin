package br.com.cpqd.asr.asr_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Toast
import br.com.cpqd.asr.asr_kotlin.audio.FileAudioSource
import br.com.cpqd.asr.asr_kotlin.constant.ContentTypeConstants.Companion.TYPE_OCTET_STREAM
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var audio: FileAudioSource? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        play.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        play.isEnabled = false
        progress.show()

        thread(start = true, isDaemon = true, name = "ResultThread") {

            val speech = SpeechRecognizer.Builder()
                .serverURL("wss://speech.cpqd.com.br/asr/ws/v2/recognize/8k")
                .credentials("felipe", "felipe.cpqd")
                .audioSource(audio, TYPE_OCTET_STREAM)
                .build()

            val result = speech.waitRecognitionResult()

            response.text = result?.alternatives?.first()?.text

            //don't do it
            runOnUiThread {
                play.isEnabled = true
                progress.hide()
            }
        }
    }


    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.id) {
                R.id.transfer -> {
                    if (checked) {
                        audio =
                            FileAudioSource(applicationContext.assets.open("bank_transfira_8k.wav"))
                    }
                }
                R.id.bigAudio -> {
                    if (checked) {
                        audio = FileAudioSource(applicationContext.assets.open("big_audio_8k.wav"))
                    }
                }
                R.id.cpf -> {
                    if (checked) {
                        audio = FileAudioSource(applicationContext.assets.open("cpf_8k.wav"))
                    }
                }
                R.id.music -> {
                    if (checked) {
                        audio = FileAudioSource(applicationContext.assets.open("music.wav"))
                    }
                }
                R.id.noEndSilence -> {
                    if (checked) {
                        audio =
                            FileAudioSource(applicationContext.assets.open("no_end_silence_8k.wav"))
                    }
                }
                R.id.pizza -> {
                    if (checked) {
                        audio =
                            FileAudioSource(applicationContext.assets.open("pizza_veg_audio_8k.wav"))
                    }
                }
                R.id.silence -> {
                    if (checked) {
                        audio = FileAudioSource(applicationContext.assets.open("silence_8k.wav"))
                    }
                }
            }
        }
    }


}
