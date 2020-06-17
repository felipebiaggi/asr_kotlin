package br.com.cpqd.asr.asr_kotlin

import android.content.Context
import android.os.Build
import br.com.cpqd.asr.asr_kotlin.audio.AudioEncoding
import br.com.cpqd.asr.asr_kotlin.audio.FileAudioSource
import br.com.cpqd.asr.asr_kotlin.exception.InvalidCredentialsException
import br.com.cpqd.asr.asr_kotlin.exception.URLBlankException
import java.net.URI

class SpeechRecognizer {


    class Builder {

        var uri: URI? = null

        var credentials: Array<String> = arrayOf()

        var audioSampleRate: Int = 8000

        var chunkLength: Int = 250

        var maxWaitSeconds: Int = 30

        var sampleSize: AudioEncoding = AudioEncoding.LINEAR16

        var audio: FileAudioSource? = null

        var audioType: String? = null

        fun serverURL(url: String): Builder {
            if (url.isBlank()) {
                throw URLBlankException()
            }
            this.uri = URI(url)
            return this
        }

        fun credentials(user: String, secret: String): Builder {
            if (user.isBlank() || secret.isBlank()) {
                throw InvalidCredentialsException()
            }
            this.credentials = arrayOf(user, secret)
            return this
        }


        fun sampleRate(sampleRate : Int): Builder {
            this.audioSampleRate = sampleRate
            return this
        }

        fun chunkBuffer(chunk: Int): Builder{
            this.chunkLength = chunk
            return this
        }

        fun maxWaitSeconds(maxWaitSeconds: Int): Builder {
            this.maxWaitSeconds = maxWaitSeconds
            return this
        }

        fun sampleSize(sampleSize: AudioEncoding): Builder {
            this.sampleSize = sampleSize
            return this
        }

        fun builder(): Builder {
            return Builder()
        }

        fun build(): SpeechRecognizerImpl {
            return SpeechRecognizerImpl(this)
        }

        fun audioSource(audio: FileAudioSource?, type: String): Builder {
            this.audio = audio
            this.audioType = type
            return this
        }

    }

}