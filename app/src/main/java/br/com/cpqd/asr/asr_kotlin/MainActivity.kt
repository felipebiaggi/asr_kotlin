package br.com.cpqd.asr.asr_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var toView: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        start.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        startActivity(toView)
    }

    fun onRadioButtonClicked(view: View) {
        if(view is RadioButton) {
            val checked = view.isChecked

            when(view.id) {
                R.id.file -> {
                    if(checked){
                        toView = Intent(this, FileAudioActivity::class.java)
                    }
                }
                R.id.microphone -> {
                    if(checked) {
                        toView = Intent(this, MicrophoneAudioActivity::class.java)
                    }
                }
            }

        }
    }
}