package br.com.cpqd.asr.asr_kotlin.util

class Util {

    companion object {
        fun createBufferSizer(audioLenght: Int, sampleRate: Int, sampleSize: Int): ByteArray {
            return ByteArray((audioLenght * sampleRate * sampleSize / 1000L / 8).toInt())
        }
    }

}