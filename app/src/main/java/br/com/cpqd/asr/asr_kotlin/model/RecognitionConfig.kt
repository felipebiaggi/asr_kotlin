package br.com.cpqd.asr.asr_kotlin.model

class RecognitionConfig {

    private var map = mutableMapOf<String, String>()

    private fun setNoInputTimeoutEnabled(noInputTimeoutEnabled: Boolean){
        map["noInputTimeout.enabled"] = noInputTimeoutEnabled.toString()
    }

    private fun setNoInputTimeout(setNoInputTimeout: Int) {
        map["noInputTimeout.value"] = setNoInputTimeout.toString()
    }

    private fun setRecognitionTimeoutEnable(recognitionTimeoutEnable: Boolean) {
        map["recognitionTimeout.enabled"] = recognitionTimeoutEnable.toString()
    }



}