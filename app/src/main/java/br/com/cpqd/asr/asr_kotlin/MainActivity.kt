package br.com.cpqd.asr.asr_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.cpqd.asr.asr_kotlin.audio.FileAudioSource
import br.com.cpqd.asr.asr_kotlin.constant.ContentTypeConstants
import br.com.cpqd.asr.asr_kotlin.constant.ContentTypeConstants.Companion.TYPE_OCTET_STREAM
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener(this)


    }

    override fun onClick(v: View?) {

        Toast.makeText(this, "Iniciando...", Toast.LENGTH_SHORT).show()

        val audio = FileAudioSource(applicationContext.assets.open("cpf_8k.wav"))

        var speech = SpeechRecognizer.Builder()
            .serverURL("wss://speech.cpqd.com.br/asr/ws/v2/recognize/8k")
            .credentials("felipe", "felipe.cpqd")
            .audioSource(audio, TYPE_OCTET_STREAM)
            .build()



    }

}
