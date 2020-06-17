package br.com.cpqd.asr.asr_kotlin.model

data class Alternative (val text: String, val words: List<Word>, val score: Int, val lm: String)