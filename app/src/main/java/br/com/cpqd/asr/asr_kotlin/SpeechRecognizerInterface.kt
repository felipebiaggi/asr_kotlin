package br.com.cpqd.asr.asr_kotlin

import br.com.cpqd.asr.asr_kotlin.model.RecognitionResult

interface SpeechRecognizerInterface {

    fun waitRecognitionResult(): RecognitionResult?

}