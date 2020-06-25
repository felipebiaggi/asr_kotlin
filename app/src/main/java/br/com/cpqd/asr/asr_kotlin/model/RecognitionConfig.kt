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

    private fun setHeadMarginMiliseconds(endpointHeadMargin: Int?) {
        if (endpointHeadMargin != null)
            map["endpointer.headMargin"] = endpointHeadMargin.toString()
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

    private fun setEndpointerUseToneDetectors(endpointerUseToneDetectors: Boolean?) {
        if (endpointerUseToneDetectors != null)
            map["endpointer.useToneDetectors"] = endpointerUseToneDetectors.toString()
    }

    private fun setWordDetails(wordDetails: String?) {
        if (wordDetails != null)
            map["decoder.wordDetails"] = wordDetails
    }

    private fun setMaxSegmentDuration(maxSegmentDuration: Int?) {
        if (maxSegmentDuration != null)
            map["endpointer.maxSegmentDuration"] = maxSegmentDuration.toString()
    }

    private fun setSegmentOverlapTime(segmentOverlapTime: Int?) {
        if(segmentOverlapTime != null)
            map["endpointer.segmentOverlapTime"] = segmentOverlapTime.toString()
    }

    private fun setHintsWords(hintsWords: String?){
        if(hintsWords != null)
            map["hints.words"] = hintsWords
    }

    private fun setTextifyEnabled(textifyEnabled: Boolean?) {
        if(textifyEnabled != null)
            map["textify.enabled"] = textifyEnabled.toString()
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
        private var endpointerUseToneDetectors: Boolean? = null
        private var wordDetails: String? = null
        private var maxSegmentDuration: Int? = null
        private var segmentOverlapTime: Int? = null
        private var hintsWords: String? = null
        private var textifyEnabled: Boolean? = null


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

        fun endpointerUseToneDetectors(value: Boolean): Builder {
            this.endpointerUseToneDetectors = value
            return this
        }

        fun wordDetails(value: String): Builder {
            this.wordDetails = value
            return this
        }

        fun maxSegmentDuration(value: Int): Builder {
            this.maxSegmentDuration = value
            return this
        }

        fun segmentOverlapTime(value: Int): Builder {
            this.segmentOverlapTime = value
            return this
        }

        fun hintsWords(value: String): Builder {
            this.hintsWords = value
            return this
        }

        fun textifyEnabled(value: Boolean): Builder {
            this.textifyEnabled = value
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
            config.setEndpointerUseToneDetectors(endpointerUseToneDetectors)
            config.setWordDetails(wordDetails)
            config.setMaxSegmentDuration(maxSegmentDuration)
            config.setSegmentOverlapTime(segmentOverlapTime)
            config.setHintsWords(hintsWords)

            return config
        }

    }
}