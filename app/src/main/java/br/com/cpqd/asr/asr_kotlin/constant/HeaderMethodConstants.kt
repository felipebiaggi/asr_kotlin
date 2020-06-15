package br.com.cpqd.asr.asr_kotlin.constant

class HeaderMethodConstants {
    companion object {

        const val ASR_PROTOCOL: String = "ASR"

        const val ASR_MAJOR_VERSION: String = "2"

        const val ASR_MINOR_VERSION: String = "3"

        const val ASR_VERSION: String = "$ASR_MAJOR_VERSION.$ASR_MINOR_VERSION"

        const val METHOD_CANCEL_RECOGNITION: String = "CANCEL_RECOGNITION"

        const val METHOD_CREATE_SESSION: String = "CREATE_SESSION"

        const val METHOD_START_RECOGNITION: String = "START_RECOGNITION"

        const val METHOD_SEND_AUDIO: String = "SEND_AUDIO"

        const val METHOD_RELEASE_SESSION: String = "RELEASE_SESSION"

        const val METHOD_RECOGNITION_RESULT: String = "RECOGNITION_RESULT"

        const val METHOD_RESPONSE: String = "RESPONSE"

        const val METHOD_START_OF_SPEECH: String = "START_OF_SPEECH"

        const val METHOD_END_OF_SPEECH: String = "END_OF_SPEECH"

        const val METHOD_START_INPUT_TIMERS = "START_INPUT_TIMERS"

        const val TOKEN_REGEX: String = "[\\Q!#$%&'*+-.^_`|~\\E0-9A-Za-z]+"

    }
}