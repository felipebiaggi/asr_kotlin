package br.com.cpqd.asr.asr_kotlin.constant

import java.nio.charset.Charset
import java.util.*

class CharsetConstants {
    companion object {

        val DEFAULT_LOCALE : Locale = Locale.US

        val DEFAULT_CHARSET : Charset = Charset.forName("UTF-8")

        val NETWORK_CHARSET : Charset = Charset.forName("UTF-8")

    }
}