package br.com.cpqd.asr.asr_kotlin.audio

import java.io.InputStream

class FileAudioSource(private val inputStream: InputStream) : AudioSource {

    private var finished = false

    override fun read(byte: ByteArray): Int {
        if(finished) return -1
        return inputStream.read(byte, 0, byte.size)
    }

    override fun close() {
        inputStream.close()
    }

    override fun finish() {
        finished = true
    }

}