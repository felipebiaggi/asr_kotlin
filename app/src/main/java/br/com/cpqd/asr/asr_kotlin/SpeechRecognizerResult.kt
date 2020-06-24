package br.com.cpqd.asr.asr_kotlin

interface SpeechRecognizerResult {
    fun onResult(result: String)
}