package br.com.cpqd.asr.asr_kotlin.model

class RecognitionError(message: AsrMessage) {

    private var errorCode: String? = null
    private var errorMessage: String? = null

    init {
        errorCode = message.mHeader["Error-Code"]
        errorMessage = message.mHeader["Message"]
    }


    override fun toString(): String {
        return "Error-Code: $errorCode - Message: $errorMessage"
    }
}