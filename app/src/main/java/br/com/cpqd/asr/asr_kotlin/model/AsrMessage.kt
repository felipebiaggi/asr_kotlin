package br.com.cpqd.asr.asr_kotlin.model

import android.util.Log
import br.com.cpqd.asr.asr_kotlin.constant.ByteArrayContants.Companion.COLON
import br.com.cpqd.asr.asr_kotlin.constant.ByteArrayContants.Companion.CRLF
import br.com.cpqd.asr.asr_kotlin.constant.ByteArrayContants.Companion.SPACE
import br.com.cpqd.asr.asr_kotlin.constant.CharsetConstants.Companion.NETWORK_CHARSET
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.ASR_PROTOCOL
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.ASR_VERSION
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.METHOD_CANCEL_RECOGNITION
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.METHOD_CREATE_SESSION
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.METHOD_END_OF_SPEECH
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.METHOD_RECOGNITION_RESULT
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.METHOD_RELEASE_SESSION
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.METHOD_RESPONSE
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.METHOD_SEND_AUDIO
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.METHOD_START_INPUT_TIMERS
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.METHOD_START_OF_SPEECH
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.METHOD_START_RECOGNITION
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.TOKEN_REGEX
import br.com.cpqd.asr.asr_kotlin.exception.*
import java.io.ByteArrayOutputStream
import java.io.IOException


class AsrMessage private constructor() {


    private val TAG: String = AsrMessage::class.java.simpleName

    private var mProtocol: String? = null
        set(value) {
            if (ASR_PROTOCOL == value) {
                field = value
            } else {
                throw WrongProtocolException("invalid message: wrong protocol - $value")
            }
        }

    private var mVersion: String? = null
        set(value) {
            if (ASR_VERSION == value) {
                field = value
            } else {
                throw WrongVersionException("invalid message: wrong version - $value")
            }
        }

    var mMethod: String? = null
        set(value) {
            isValidMethodOrNotNull(value)
            field = value
        }

    var mHeader: Map<String, String> = mutableMapOf()

    var mBody: ByteArray? = null

    constructor(message: ByteArray?) : this() {

        if (message == null || message.isEmpty()) {
            throw MessageEmptyException("invalid message: unexpected end of message")
        }

        //Convert the Byte Array to String and split into List<String>
        //We have with it two result, the header encoded UTF-8, and the message body, we can be a binary blob
        val splitHeaderBody: List<String> = message
            .toString(NETWORK_CHARSET)
            .split("\r\n\r\n")

        //The header is always necessary and expected
        val headerLines: List<String> = splitHeaderBody[0].split("\r\n")

        //The message body is optional
        val body: ByteArray = splitHeaderBody[1].toByteArray(NETWORK_CHARSET)

        //The first line is always necessary
        val headerFirstLine = headerLines[0].split(" ")

        //We expect the first line of the header to always have three elements
        //ASR 2.3 START_RECOGNITION
        if (headerFirstLine.size != 3) {
            throw HeaderMissingElementException("invalid message: invalid start line")
        } else {
            mProtocol = headerFirstLine[0].trim()
            mVersion = headerFirstLine[1].trim()
            mMethod = headerFirstLine[2].trim()
        }


        if (headerLines.size > 1) {
            mHeader = getHeaderFieldValue(headerLines)
        }

        if (mHeader.containsKey("Content-Length")
            && !mHeader["Content-Length"].isNullOrBlank()
        ) {
            mHeader["Content-Length"]?.let {
                if (Integer.parseInt(it) == body.size) {
                    mBody = body
                } else {
                    throw BodySizeMismatchExcepetion("body size is different than the length of the content")
                }
            }
        }
    }


    constructor(method: String, headerFields: Map<String, String>, body: ByteArray) : this() {
        mProtocol = ASR_PROTOCOL
        mVersion = ASR_VERSION
        mMethod = method
        mHeader = headerFields
        mBody = body
    }


    constructor(method:String): this(){
        mProtocol = ASR_PROTOCOL
        mVersion = ASR_VERSION
        mMethod = method
    }

    constructor(method: String, headerFields: Map<String, String>): this(){
        mProtocol = ASR_PROTOCOL
        mVersion = ASR_VERSION
        mMethod = method
        mHeader = headerFields
    }


    private fun getHeaderFieldValue(headerLines: List<String>): Map<String, String> {
        val listFields = mutableMapOf<String, String>()

        for (x in 1 until headerLines.size) {
            if (headerLines[x].matches(("$TOKEN_REGEX:.*").toRegex())) {
                val headerFieldSplit = headerLines[x].split(":")
                listFields[headerFieldSplit[0]] = headerFieldSplit[1].trim()
            } else {
                Log.i(TAG, "ignoring invalid header field: ${headerLines[x]}")
            }
        }

        return listFields
    }

    private fun isValidMethodOrNotNull(method: String?) {

        if (method.isNullOrBlank() || !(method.contentEquals(METHOD_CANCEL_RECOGNITION)
                    || method.contentEquals(METHOD_CREATE_SESSION)
                    || method.contentEquals(METHOD_START_RECOGNITION)
                    || method.contentEquals(METHOD_SEND_AUDIO)
                    || method.contentEquals(METHOD_RELEASE_SESSION)
                    || method.contentEquals(METHOD_RECOGNITION_RESULT)
                    || method.contentEquals(METHOD_RESPONSE)
                    || method.contentEquals(METHOD_START_OF_SPEECH)
                    || method.contentEquals(METHOD_END_OF_SPEECH)
                    || method.contentEquals(METHOD_START_INPUT_TIMERS))
        ) {
            throw WrongMethodExcepetion("invalid method - $method")
        }
    }


    fun toByteArray(): ByteArray {

        try {

            val baos = ByteArrayOutputStream()

            mProtocol?.let {
                baos.write(it.toByteArray(NETWORK_CHARSET))
            }

            baos.write(SPACE)

            mVersion?.let {
                baos.write(it.toByteArray(NETWORK_CHARSET))
            }

            baos.write(SPACE)

            mMethod?.let {
                baos.write(it.toByteArray(NETWORK_CHARSET))
            }

            baos.write(CRLF)


            mHeader.forEach { (key, value) ->
                baos.write(key.toByteArray(NETWORK_CHARSET))
                baos.write(COLON)
                baos.write(SPACE)
                baos.write(value.toByteArray(NETWORK_CHARSET))
                baos.write(CRLF)
            }

            baos.write(CRLF)

            mBody?.let {
                baos.write(it)
            }

            return baos.toByteArray()

        } catch (e: IOException) {
            Log.e(TAG, "could not serialize asr message into byte array", e)

            throw RuntimeException(e)
        }

    }

    override fun toString(): String {
        return "Message(TAG='$TAG', mProtocol=$mProtocol, mVersion=$mVersion, mMethod=$mMethod, mHeader=$mHeader, mBody=${mBody?.contentToString()})"
    }

}
