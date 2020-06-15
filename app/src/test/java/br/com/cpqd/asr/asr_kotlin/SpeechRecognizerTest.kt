package br.com.cpqd.asr.asr_kotlin

import br.com.cpqd.asr.asr_kotlin.exception.InvalidCredentialsException
import br.com.cpqd.asr.asr_kotlin.exception.URLBlankException
import org.junit.Test
import java.net.URISyntaxException

class SpeechRecognizerTest {

    @Test(expected = URLBlankException::class)
    fun should_ThrowException_WhenURLIsBlank(){
        var speech = SpeechRecognizer
            .Builder()
            .serverURL("")
    }

    @Test(expected = URISyntaxException::class)
    fun should_ThrowException_WhenStringCouldNotBeParsedAsURI(){
        var speech = SpeechRecognizer
            .Builder()
            .serverURL("wss: //speech. cpqd.com .br/asr/ws/v2/ recognize /8k")
    }

    @Test
    fun testAsrURL() {
        var speech = SpeechRecognizer.Builder().serverURL("wss://speech.cpqd.com.br/asr/ws/v2/recognize/8k")
    }

    @Test(expected = InvalidCredentialsException::class)
    fun should_ThrowException_WhenCredentialsAreBlank(){
        var speech = SpeechRecognizer
            .Builder()
            .serverURL("wss://speech.cpqd.com.br/asr/ws/v2/recognize/8k")
            .credentials("", "")
    }


}