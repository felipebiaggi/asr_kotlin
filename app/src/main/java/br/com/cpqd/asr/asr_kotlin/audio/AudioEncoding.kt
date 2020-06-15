package br.com.cpqd.asr.asr_kotlin.audio

enum class AudioEncoding(private val sampleSize: Int) {

    LINEAR16(16),
    LINEAER8(8);

    fun getSampleSize(): Int {
        return sampleSize
    }

}