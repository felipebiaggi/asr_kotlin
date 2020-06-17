package br.com.cpqd.asr.asr_kotlin.model

data class RecognitionResult (val alternatives : List<Alternative>, val segmentIndex: Int, val lastSegment: Boolean, val finalResult: Boolean, val startTime: Double, val endTime: Double, val resultStatus: String)