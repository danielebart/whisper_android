package com.whispertflite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class Speech(private val context: Context) {
    fun recognize(
        onError: () -> Unit,
        onProgress: (String) -> Unit,
        onSuccess: (String) -> Unit,
    ) {
        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) = Unit

            override fun onBeginningOfSpeech() = Unit

            override fun onRmsChanged(rmsdB: Float) = Unit

            override fun onBufferReceived(buffer: ByteArray?) = Unit

            override fun onEndOfSpeech() = Unit

            override fun onError(error: Int) {
                System.out.println("Error speech " + error)
                speechRecognizer.cancel()
                speechRecognizer.destroy()
                onError()
            }

            override fun onResults(results: Bundle) {
                val data =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                data?.takeIf { it.isNotBlank() }?.let { onSuccess(it) }
                speechRecognizer.cancel()
                speechRecognizer.destroy()
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val data =
                    partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                data?.takeIf { it.isNotBlank() }?.let { onProgress(it) }
            }

            override fun onEvent(eventType: Int, params: Bundle?) = Unit
        }
        )
        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
        }
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "it-IT")
        speechRecognizer.startListening(recognizerIntent)
    }
}