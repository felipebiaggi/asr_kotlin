package br.com.cpqd.asr.asr_kotlin.audio

import java.io.IOException

interface AudioSource {

    @Throws(IOException::class)
    fun read(byte: ByteArray) : Int

    @Throws(IOException::class)
    fun close()

    @Throws(IOException::class)
    fun finish()
}