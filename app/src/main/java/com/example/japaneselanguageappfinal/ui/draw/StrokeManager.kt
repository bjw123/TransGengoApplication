/*
To set this up I used an open source project that the creator uploaded on his blog
https://github.com/nikit19/iSee
Was extremely helpful as the docs did not walk me through canvas API very well

I then changed the model that was used to Japanese and built features to suit my app
 */




package com.example.japaneselanguageappfinal.ui.draw


import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.example.japaneselanguageappfinal.MainActivity

import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.*


object StrokeManager {
    private var inkBuilder = Ink.builder()
    private var strokeBuilder = Ink.Stroke.builder()
    private lateinit var model: DigitalInkRecognitionModel

    fun addNewTouchEvent(event: MotionEvent) {
        val action = event.actionMasked
        val x = event.x
        val y = event.y
        val t = System.currentTimeMillis()

        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> strokeBuilder.addPoint(
                Ink.Point.create(
                    x,
                    y,
                    t
                )
            )
            MotionEvent.ACTION_UP -> {
                strokeBuilder.addPoint(Ink.Point.create(x, y, t))
                inkBuilder.addStroke(strokeBuilder.build())
                strokeBuilder = Ink.Stroke.builder()

            }
            else -> {
                // Action not relevant for ink construction
            }
        }
    }

    fun download() {
        var modelIdentifier: DigitalInkRecognitionModelIdentifier? = null
        try {
            modelIdentifier =
                DigitalInkRecognitionModelIdentifier.fromLanguageTag("ja")
        } catch (e: MlKitException) {
            // language tag failed to parse, handle error.
        }

        model =
            DigitalInkRecognitionModel.builder(modelIdentifier!!).build()

        val remoteModelManager = RemoteModelManager.getInstance()
        remoteModelManager.download(model, DownloadConditions.Builder().build())
            .addOnSuccessListener {
                Log.i("StrokeManager", "Model downloaded")
            }
            .addOnFailureListener { e: Exception ->
                Log.e("StrokeManager", "Error while downloading a model: $e")
            }
    }

    fun recognize(context: CanvasActivity) {
        val recognizer: DigitalInkRecognizer =
            DigitalInkRecognition.getClient(
                DigitalInkRecognizerOptions.builder(model).build()
            )

        val ink = inkBuilder.build()




        recognizer.recognize(ink)
            .addOnSuccessListener { result: RecognitionResult ->
                Toast.makeText(context, "I see ${result.candidates[0].text}", Toast.LENGTH_LONG)
                    .show()

            }
            .addOnFailureListener { e: Exception ->
                Log.e("StrokeManager", "Error during recognition: $e")
            }
    }

    fun clear() {
        inkBuilder = Ink.builder()
    }
}