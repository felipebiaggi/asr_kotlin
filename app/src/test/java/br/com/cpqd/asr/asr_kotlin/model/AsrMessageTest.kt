package br.com.cpqd.asr.asr_kotlin.model

import br.com.cpqd.asr.asr_kotlin.constant.CharsetConstants.Companion.NETWORK_CHARSET
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.METHOD_SEND_AUDIO
import br.com.cpqd.asr.asr_kotlin.exception.*
import org.junit.Assert.*
import org.junit.Test
import java.io.ByteArrayOutputStream

class AsrMessageTest {

    val SPACE: ByteArray = " ".toByteArray(NETWORK_CHARSET)
    val CRLF: ByteArray = "\r\n".toByteArray(NETWORK_CHARSET)
    val COLON: ByteArray = ":".toByteArray(NETWORK_CHARSET)

    @Test(expected = MessageEmptyException::class)
    fun should_ThrowException_WhenByteArrayIsEmpty() {
        AsrMessage(ByteArrayOutputStream().toByteArray())
    }

    @Test(expected = HeaderMissingElementException::class)
    fun should_ThrowException_WhenFirstLineDontHaveThreeElements() {
        var baos = ByteArrayOutputStream()
        baos.write("2.3".toByteArray(NETWORK_CHARSET))
        baos.write(SPACE)
        baos.write("START_RECOGNITION".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)
        baos.write(CRLF)

        AsrMessage(baos.toByteArray())
    }

    @Test(expected = WrongProtocolException::class)
    fun should_ThrowException_WhenIncorrectProtocol(){
        var baos = ByteArrayOutputStream()
        baos.write("AAA".toByteArray(NETWORK_CHARSET))
        baos.write(SPACE)
        baos.write("2.3".toByteArray(NETWORK_CHARSET))
        baos.write(SPACE)
        baos.write("START_RECOGNITION".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)
        baos.write(CRLF)

        var message = AsrMessage(baos.toByteArray())
    }

    @Test(expected = WrongVersionException::class)
    fun should_ThrowException_WhenIncorrectVersion(){
        var baos = ByteArrayOutputStream()
        baos.write("ASR".toByteArray(NETWORK_CHARSET))
        baos.write(SPACE)
        baos.write("2.5".toByteArray(NETWORK_CHARSET))
        baos.write(SPACE)
        baos.write("START_RECOGNITION".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)
        baos.write(CRLF)

        var message = AsrMessage(baos.toByteArray())
    }

    @Test(expected = WrongMethodExcepetion::class)
    fun should_ThrowException_WhenIncorrectMethod(){
        var baos = ByteArrayOutputStream()
        baos.write("ASR".toByteArray(NETWORK_CHARSET))
        baos.write(SPACE)
        baos.write("2.3".toByteArray(NETWORK_CHARSET))
        baos.write(SPACE)
        baos.write("~~~~~~~~".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)
        baos.write(CRLF)

        var message = AsrMessage(baos.toByteArray())
    }

    @Test(expected = BodySizeMismatchExcepetion::class)
    fun should_ThrowException_WhenContentLengthAndBodySizeAreIncompatible(){
        var baos = ByteArrayOutputStream()
        baos.write("ASR".toByteArray(NETWORK_CHARSET))
        baos.write(SPACE)
        baos.write("2.3".toByteArray(NETWORK_CHARSET))
        baos.write(SPACE)
        baos.write("START_RECOGNITION".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)

        baos.write("Accept".toByteArray(NETWORK_CHARSET))
        baos.write(COLON)
        baos.write(SPACE)
        baos.write("application/json".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)

        baos.write("decoder.maxSentences".toByteArray(NETWORK_CHARSET))
        baos.write(COLON)
        baos.write(SPACE)
        baos.write("3".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)

        baos.write("noInputTimeout.enabled".toByteArray(NETWORK_CHARSET))
        baos.write(COLON)
        baos.write(SPACE)
        baos.write("true".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)

        baos.write("noInputTimeout.value".toByteArray(NETWORK_CHARSET))
        baos.write(COLON)
        baos.write(SPACE)
        baos.write("5000".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)

        baos.write("Content-Type".toByteArray(NETWORK_CHARSET))
        baos.write(COLON)
        baos.write(SPACE)
        baos.write("text/uri-list".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)

        baos.write("Content-Length".toByteArray(NETWORK_CHARSET))
        baos.write(COLON)
        baos.write(SPACE)
        baos.write("30".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)

        baos.write(CRLF)

        baos.write("builtin:slm/general".toByteArray(NETWORK_CHARSET))

        var message = AsrMessage(baos.toByteArray())
    }

    @Test
    fun testOneLineHeader(){
        var baos = ByteArrayOutputStream()
        baos.write("ASR".toByteArray(NETWORK_CHARSET))
        baos.write(SPACE)
        baos.write("2.3".toByteArray(NETWORK_CHARSET))
        baos.write(SPACE)
        baos.write("START_RECOGNITION".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)
        baos.write(CRLF)
        var message = AsrMessage(baos.toByteArray())

        assertTrue(message.mHeader.isEmpty())
        assertNull(message.mBody)
    }


    @Test
    fun testSecondaryConstructor(){
        var message = AsrMessage(METHOD_SEND_AUDIO, mapOf("Accept" to "application/json"), ByteArray(0))
        println(message.toByteArray().toString(NETWORK_CHARSET))
    }



    @Test
    fun splitMessage() {
        var baos = ByteArrayOutputStream()
        baos.write("ASR".toByteArray(NETWORK_CHARSET))
        baos.write(SPACE)
        baos.write("2.3".toByteArray(NETWORK_CHARSET))
        baos.write(SPACE)
        baos.write("START_RECOGNITION".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)

        baos.write("Accept".toByteArray(NETWORK_CHARSET))
        baos.write(COLON)
        baos.write(SPACE)
        baos.write("application/json".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)

        baos.write("decoder.maxSentences".toByteArray(NETWORK_CHARSET))
        baos.write(COLON)
        baos.write(SPACE)
        baos.write("3".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)

        baos.write("noInputTimeout.enabled".toByteArray(NETWORK_CHARSET))
        baos.write(COLON)
        baos.write(SPACE)
        baos.write("true".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)

        baos.write("noInputTimeout.value".toByteArray(NETWORK_CHARSET))
        baos.write(COLON)
        baos.write(SPACE)
        baos.write("5000".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)

        baos.write("Content-Type".toByteArray(NETWORK_CHARSET))
        baos.write(COLON)
        baos.write(SPACE)
        baos.write("text/uri-list".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)

        baos.write("Content-Length".toByteArray(NETWORK_CHARSET))
        baos.write(COLON)
        baos.write(SPACE)
        baos.write("19".toByteArray(NETWORK_CHARSET))
        baos.write(CRLF)

        baos.write(CRLF)

        baos.write("builtin:slm/general".toByteArray(NETWORK_CHARSET))

        print(baos.toByteArray().toString(NETWORK_CHARSET))

        var message = AsrMessage(baos.toByteArray())


    }

}