package br.com.cpqd.asr.asr_kotlin

interface SpeechRecognizerResult {
    fun callback(result: String)
}