package br.com.cpqd.asr.asr_kotlin

import android.util.AndroidRuntimeException
import android.util.Log
import br.com.cpqd.asr.asr_kotlin.audio.AudioSource
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
import java.util.*
import kotlin.concurrent.thread


class SpeechRecognizerImpl(private val builder: SpeechRecognizer.Builder) :
    WebSocketListenerAsr {


    private val TAG: String = SpeechRecognizerImpl::class.java.simpleName

    private var query = Collections.synchronizedList(LinkedList<AsrMessage>())

    private var isSending: Boolean = false

    private val wss: WebSocket

    init {

        val factory = WebSocketFactory()
            .setConnectionTimeout(builder.maxWaitSeconds * 1000)

        factory.verifyHostname = false

        wss = factory.createSocket(builder.uri)
            .setUserInfo(builder.credentials[0], builder.credentials[1])
            .addListener(this)
            .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)


        thread(start = true, name = "WebSocketConnectionThread", isDaemon = true) {
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



                clear()

            } catch (e: WebSocketException) {
                e.message?.let { Log.d(TAG, it) }
                clear()
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
        Log.d(TAG, "Connected")
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
                thread(start = true, name = "SendAudioThread") {
                    while (query.size > 0) {
                        websocket?.sendBinary(query.first().toByteArray())
                        query.removeAt(0)
                    }
                }
            }
        }

        if (responseMessage.mMethod == "RECOGNITION_RESULT") {

            responseMessage.mBody?.toString(NETWORK_CHARSET)?.let {
                builder.listerning?.onResult(
                    Gson().fromJson(it, RecognitionResult::class.java).getString()
                )
            }
            websocket?.disconnect()
        }

    }


    fun recognizer(fileAudio: AudioSource, contentType: String) {

        if (contentType.isBlank() || !(
                    contentType.contentEquals(TYPE_OCTET_STREAM)
                            || contentType.contentEquals(
                        TYPE_AUDIO_RAW
                    ))
        ) {
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

                    read = fileAudio.read(buffer)

                    val bufferToSend: ByteArray = if (read > 0 && read != buffer.size) {
                        buffer.copyOf(read)
                    } else {
                        buffer
                    }

                    //Arrumar isso depois
                    var copy = ByteArray(bufferToSend.size)
                    System.arraycopy(bufferToSend, 0, copy, 0, bufferToSend.size)


                    val message = if (read > 0) {
                        AsrMessage(
                            METHOD_SEND_AUDIO,
                            mutableMapOf(
                                "LastPacket" to "false",
                                "Content-Type" to contentType
                            ),
                            copy
                        )
                    } else {
                        AsrMessage(
                            METHOD_SEND_AUDIO,
                            mutableMapOf(
                                "LastPacket" to "true",
                                "Content-Type" to contentType
                            )
                        )
                    }
                    query.add(message)
                }

            } catch (e: IOException) {
                e.message?.let { Log.w(TAG, it) }
            } catch (e: AndroidRuntimeException) {
                e.message?.let { Log.w(TAG, it) }
            } catch (e: IllegalStateException) {
                e.message?.let { Log.w(TAG, it) }
            } finally {
                fileAudio.close()
            }
        }
    }


    private fun startRecognition(): ByteArray {
        return AsrMessage(
            METHOD_START_RECOGNITION,
            builder.recognizerConfig.configMap(),
            builder.recognizerConfigBody.toByteArray(NETWORK_CHARSET)
        ).toByteArray()
    }

    private fun clear() {
        builder.listerning?.onResult("")
    }

}