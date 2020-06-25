package br.com.cpqd.asr.asr_kotlin.model

class RecognitionConfig {

    private var map = mutableMapOf<String, String>()

    private fun setAccept(accept: String?) {
        if (accept != null)
            map["Accept"] = accept
    }

    private fun setContentId(contentId: String?) {
        if (contentId != null)
            map["Content-ID"] = contentId
    }

    private fun setContentType(contentType: String?) {
        if (contentType != null)
            map["Content-Type"] = contentType
    }

    private fun setNoInputTimeoutEnabled(noInputTimeoutEnabled: Boolean?) {
        if (noInputTimeoutEnabled != null)
            map["noInputTimeout.enabled"] = noInputTimeoutEnabled.toString()
    }

    private fun setNoInputTimeout(setNoInputTimeout: Int?) {
        if (setNoInputTimeout != null)
            map["noInputTimeout.value"] = setNoInputTimeout.toString()
    }

    private fun setRecognitionTimeoutEnabled(recognitionTimeoutEnable: Boolean?) {
        if (recognitionTimeoutEnable != null)
            map["recognitionTimeout.enabled"] = recognitionTimeoutEnable.toString()
    }

    private fun setRecognitionTimeout(recognitionTimeout: Int?) {
        if (recognitionTimeout != null)
            map["recognitionTimeout.value"] = recognitionTimeout.toString()
    }

    private fun setStartInputTimers(decoderStartInputTimers: Boolean?) {
        if (decoderStartInputTimers != null)
            map["decoder.startInputTimers"] = decoderStartInputTimers.toString()
    }

    private fun setMaxSentences(decoderMaxSentences: Int?) {
        if (decoderMaxSentences != null)
            map["decoder.maxSentences"] = decoderMaxSentences.toString()
    }

    private fun setHeadMarginMiliseconds(endpointHedMargin: Int?) {
        if (endpointHedMargin != null)
            map["endpointer.headMargin"] = endpointHedMargin.toString()
    }

    private fun setTailMarginMiliseconds(endpointerTailMargin: Int?) {
        if (endpointerTailMargin != null)
            map["endpointer.tailMargin"] = endpointerTailMargin.toString()
    }

    private fun setWaitEndMiliseconds(endpointerWaitEnd: Int?) {
        if (endpointerWaitEnd != null)
            map["endpointer.waitEnd"] = endpointerWaitEnd.toString()
    }

    private fun setEndpointerLevelThreshold(endpointerLevelThreshold: Int?) {
        if (endpointerLevelThreshold != null)
            map["endpointer.levelThreshold"] = endpointerLevelThreshold.toString()
    }

    private fun setEndpointerLevelMode(endpointerLevelMode: Int?) {
        if (endpointerLevelMode != null)
            map["endpointer.levelMode"] = endpointerLevelMode.toString()
    }

    private fun setEndpointerAutoLevelLen(endpointerAutoLevelLen: Int?) {
        if (endpointerAutoLevelLen != null)
            map["endpointer.autoLevelLen"] = endpointerAutoLevelLen.toString()
    }

    private fun setContinuousMode(continuousMode: Boolean?) {
        if (continuousMode != null)
            map["decoder.continuousMode"] = continuousMode.toString()
    }

    private fun setConfidenceThreshold(decoderConfidenceThreshold: Int?) {
        if (decoderConfidenceThreshold != null)
            map["decoder.confidenceThreshold"] = decoderConfidenceThreshold.toString()
    }

    fun configMap(): MutableMap<String, String> {
        return map
    }

    override fun toString(): String {
        var str: String = ""

        map.forEach { (key, value) ->
            str += "$key - $value\n"
        }

        return str
    }

    class Builder {

        private var accept: String? = null
        private var contentId: String? = null
        private var contentType: String? = null
        private var continuousMode: Boolean? = null
        private var confidenceThreshold: Int? = null
        private var maxSentences: Int? = null
        private var noInputTimeoutMilis: Int? = null
        private var recognitionTimeoutMilis: Int? = null
        private var headMarginMilis: Int? = null
        private var tailMarginMilis: Int? = null
        private var waitEndMilis: Int? = null
        private var recognitionTimeoutEnabled: Boolean? = null
        private var noInputTimeoutEnabled: Boolean? = null
        private var startInputTimers: Boolean? = null
        private var endPointerAutoLevelLen: Int? = null
        private var endPointerLevelMode: Int? = null
        private var endPointerLevelThreshold: Int? = null


        fun continuousMode(value: Boolean): Builder {
            this.continuousMode = value
            return this
        }

        fun confidenceThreshold(value: Int): Builder {
            if (value < 0) throw IllegalArgumentException()
            this.confidenceThreshold = value
            return this
        }

        fun maxSentences(value: Int): Builder {
            if (value < 0) throw IllegalArgumentException()
            this.maxSentences = value
            return this
        }

        fun noInputTimeoutMilis(value: Int): Builder {
            if (value < 0) throw IllegalArgumentException()
            this.noInputTimeoutMilis = value
            return this
        }

        fun recognitionTimeoutMilis(value: Int): Builder {
            if (value < 0) throw IllegalArgumentException()
            this.recognitionTimeoutMilis = value
            return this
        }

        fun headMarginMilis(value: Int): Builder {
            if (value < 0) throw IllegalArgumentException()
            this.headMarginMilis = value
            return this
        }

        fun tailMarginMilis(value: Int): Builder {
            if (value < 0) throw IllegalArgumentException()
            this.tailMarginMilis = value
            return this
        }

        fun waitEndMilis(value: Int): Builder {
            if (value < 0) throw IllegalArgumentException()
            this.waitEndMilis = value
            return this
        }

        fun startInputTimers(value: Boolean): Builder {
            this.startInputTimers = value
            return this
        }

        fun endPointerAutoLevelLen(value: Int): Builder {
            if (value < 0) throw IllegalArgumentException()
            this.endPointerAutoLevelLen = value
            return this
        }

        fun endPointerLevelMode(value: Int): Builder {
            if (value < 0) throw IllegalArgumentException()
            this.endPointerLevelMode = value
            return this
        }

        fun endPointerLevelThreshold(value: Int): Builder {
            if (value < 0) throw IllegalArgumentException()
            this.endPointerLevelThreshold = value
            return this
        }

        fun noInputTimeoutEnabled(value: Boolean): Builder {
            this.noInputTimeoutEnabled = value
            return this
        }

        fun recognitionTimeoutEnabled(value: Boolean): Builder {
            this.recognitionTimeoutEnabled = value
            return this
        }

        fun accept(value: String): Builder {
            this.accept = value
            return this
        }

        fun contentId(value: String): Builder {
            this.contentId = value
            return this
        }

        fun contentType(value: String): Builder {
            this.contentType = value
            return this
        }

        fun build(): RecognitionConfig {
            val config = RecognitionConfig()
            config.setAccept(accept)
            config.setContentId(contentId)
            config.setContentType(contentType)
            config.setContinuousMode(continuousMode)
            config.setConfidenceThreshold(confidenceThreshold)
            config.setMaxSentences(maxSentences)
            config.setNoInputTimeout(noInputTimeoutMilis)
            config.setRecognitionTimeout(recognitionTimeoutMilis)
            config.setHeadMarginMiliseconds(headMarginMilis)
            config.setTailMarginMiliseconds(tailMarginMilis)
            config.setWaitEndMiliseconds(waitEndMilis)
            config.setStartInputTimers(startInputTimers)
            config.setEndpointerAutoLevelLen(endPointerAutoLevelLen)
            config.setEndpointerLevelMode(endPointerLevelMode)
            config.setEndpointerLevelThreshold(endPointerLevelThreshold)
            config.setNoInputTimeoutEnabled(noInputTimeoutEnabled)
            config.setRecognitionTimeoutEnabled(recognitionTimeoutEnabled)

            return config
        }

    }
}