package br.com.cpqd.asr.asr_kotlin.audio

import java.io.InputStream

class FileAudioSource(private val inputStream: InputStream) : AudioSource {


    override fun read(byte: ByteArray): Int {
        return inputStream.read(byte, 0, byte.size)
    }

    override fun close() {
        inputStream.close()
    }

    override fun finish() {}

}