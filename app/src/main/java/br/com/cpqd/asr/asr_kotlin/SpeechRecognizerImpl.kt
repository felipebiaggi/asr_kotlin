package br.com.cpqd.asr.asr_kotlin

import android.util.Log
import br.com.cpqd.asr.asr_kotlin.audio.FileAudioSource
import br.com.cpqd.asr.asr_kotlin.constant.CharsetConstants.Companion.NETWORK_CHARSET
import br.com.cpqd.asr.asr_kotlin.constant.ContentTypeConstants.Companion.TYPE_AUDIO_RAW
import br.com.cpqd.asr.asr_kotlin.constant.ContentTypeConstants.Companion.TYPE_OCTET_STREAM
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.METHOD_CREATE_SESSION
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.METHOD_SEND_AUDIO
import br.com.cpqd.asr.asr_kotlin.constant.HeaderMethodConstants.Companion.METHOD_START_RECOGNITION
import br.com.cpqd.asr.asr_kotlin.model.AsrMessage
import br.com.cpqd.asr.asr_kotlin.model.RecognitionResult
import br.com.cpqd.asr.asr_kotlin.model.UserAgent
import br.com.cpqd.asr.asr_kotlin.util.Util
import com.google.gson.Gson
import com.neovisionaries.ws.client.*
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock

class SpeechRecognizerImpl(private val builder: SpeechRecognizer.Builder) :
    WebSocketListenerAsr, SpeechRecognizerInterface {


    private val TAG: String = SpeechRecognizerImpl::class.java.simpleName

    private var query = mutableListOf<AsrMessage>()

    private var isSending: Boolean = false

    private val bufferPacket: ReentrantLock = ReentrantLock()

    private val conditionBufferPacket: Condition = bufferPacket.newCondition()

    private val result: ReentrantLock = ReentrantLock()

    private val conditionResult: Condition = result.newCondition()

    private val wss: WebSocket

    private var recognitionResult: RecognitionResult? = null

    init {

        val factory = WebSocketFactory()
            .setConnectionTimeout(builder.maxWaitSeconds * 1000)

        factory.verifyHostname = false
        wss = factory.createSocket(builder.uri)
            .setUserInfo(builder.credentials[0], builder.credentials[1])
            .addListener(this)


        thread(start = true, name = "WebSocketConnectionThread") {
            try {
                wss.connect()

            } catch (e: OpeningHandshakeException) {
                val sl = e.statusLine
                println("=== Status Line ===")
                System.out.format("HTTP Version  = %s\n", sl.httpVersion)
                System.out.format("Status Code   = %d\n", sl.statusCode)
                System.out.format("Reason Phrase = %s\n", sl.reasonPhrase)


                val headers =
                    e.headers
                println("=== HTTP Headers ===")
                for ((name, values) in headers) {

                    if (values == null || values.size == 0) {
                        println(name)
                        continue
                    }
                    for (value in values) {
                        System.out.format("%s: %s\n", name, value)
                    }
                }


                result.withLock {
                    conditionResult.signal()
                }

            } catch (e: WebSocketException) {
                Log.d(TAG, e.printStackTrace().toString())

                result.withLock {
                    conditionResult.signal()
                }
            }
        }
    }

    override fun onSendingHandshake(
        websocket: WebSocket?,
        requestLine: String?,
        headers: MutableList<Array<String>>?
    ) {
        requestLine?.let {
            Log.d(TAG, requestLine)
        }
    }

    override fun onConnected(
        websocket: WebSocket?,
        headers: MutableMap<String, MutableList<String>>?
    ) {
        recognizer(builder.audio, builder.audioType)
        websocket?.sendBinary(AsrMessage(METHOD_CREATE_SESSION, UserAgent.toMap()).toByteArray())

    }

    override fun onBinaryMessage(websocket: WebSocket?, binary: ByteArray?) {
        val responseMessage = AsrMessage(binary)

        if (responseMessage.mMethod == "RESPONSE" && responseMessage.mHeader["Session-Status"] == "IDLE") {
            websocket?.sendBinary(startRecognition())
        }

        if (responseMessage.mMethod == "RESPONSE" && responseMessage.mHeader["Session-Status"] == "LISTENING") {

            if (!isSending) {
                isSending = true
                thread(start = true, name = "SendAudioThread", isDaemon = true) {
                    var isLastPacket = false
                    while (!isLastPacket) {
                        bufferPacket.withLock {
                            if (query.size > 0) {
                                val message = query.first()
                                query.removeAt(0)

                                if (message.mHeader["LastPacket"] == "true") isLastPacket = false

                                websocket?.sendBinary(message.toByteArray())

                                conditionBufferPacket.signal()
                            }
                        }
                    }
                }
            }
        }

        if (responseMessage.mMethod == "RECOGNITION_RESULT") {
            result.withLock {
                responseMessage.mBody?.toString(NETWORK_CHARSET)?.let {
                    recognitionResult = Gson().fromJson(it, RecognitionResult::class.java)
                }
                conditionResult.signal()
            }
            websocket?.disconnect()
        }

    }


    override fun waitRecognitionResult(): RecognitionResult? {
        result.withLock {
            conditionResult.await()
            return recognitionResult
        }
    }

    private fun recognizer(audio: FileAudioSource?, contentType: String?) {

        if (contentType.isNullOrBlank() || !(
                    contentType.contentEquals(TYPE_OCTET_STREAM)
                            || contentType.contentEquals(
                        TYPE_AUDIO_RAW
                    ))
        ) {
            throw IllegalArgumentException()
        }

        if (audio == null) {
            throw IllegalArgumentException()
        }

        thread(start = true, name = "AudioBufferThread", isDaemon = true) {
            val buffer = Util.createBufferSizer(
                builder.chunkLength,
                builder.audioSampleRate,
                builder.sampleSize.getSampleSize()
            )
            var read = 0

            try {

                while (read != -1) {

                    read = audio.read(buffer)

                    val bufferToSend: ByteArray = if (read > 0 && read != buffer.size) {
                        buffer.copyOf(read)
                    } else {
                        buffer
                    }

                    val message = if (read > 0) {
                        AsrMessage(
                            METHOD_SEND_AUDIO,
                            mapOf(
                                "LastPacket" to "false",
                                "Content-Length" to bufferToSend.size.toString(),
                                "Content-Type" to contentType
                            ),
                            bufferToSend
                        )
                    } else {
                        AsrMessage(
                            METHOD_SEND_AUDIO,
                            mapOf(
                                "LastPacket" to "true",
                                "Content-Length" to "0",
                                "Content-Type" to contentType
                            ),
                            ByteArray(0)
                        )
                    }

                    bufferPacket.withLock {
                        query.add(message)
                        conditionBufferPacket.await()
                    }

                }

            } catch (e: IOException) {
                throw RuntimeException(e)
            } finally {
                audio.close()
            }

        }
    }

    //hard coded!
    //expected to change
    private fun startRecognition(): ByteArray {
        return AsrMessage(
            METHOD_START_RECOGNITION,
            mapOf
                (
                "Accept" to "application/json",
                "decoder.maxSentences" to "3",
                "noInputTimeout.enabled" to "true",
                "noInputTimeout.value" to "5000",
                "Content-Type" to "text/uri-list",
                "Content-Length" to "19"
            ),
            "builtin:slm/general".toByteArray(NETWORK_CHARSET)
        ).toByteArray()
    }

}