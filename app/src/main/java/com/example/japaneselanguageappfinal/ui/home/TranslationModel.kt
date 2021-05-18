/*
Object to recycle ML Kit translation modal and use in different activities/fragments
 */


package com.example.japaneselanguageappfinal.ui.home

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

object TranslationModel
{



    fun translateEnToJp(translateText: String): String {


        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.JAPANESE)
            .build()
        val englishJapaneseTranslator: Translator = Translation.getClient(options)
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        englishJapaneseTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                // Model downloaded successfully. Okay to start translating.
                Log.i("Success", "Japanese Model downloaded")

            }
            .addOnFailureListener { exception ->
                // Model couldn’t be downloaded or other internal error.
                Log.e("Error", "Model not downloaded")
            }
        englishJapaneseTranslator.translate(translateText)
            .addOnSuccessListener { translatedText ->
                // Translation successful.
                Log.i("success", translateText)
            }
            .addOnFailureListener { exception ->
                // Error.
                // ...
                Log.e("error", "please wait 10-20 secondsfor model to download")
            }
        return translateText
    }

    fun translateJpToEn(translateText:String): String {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.JAPANESE)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        val japaneseEnglishTranslator: Translator = Translation.getClient(options)
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        japaneseEnglishTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                // Model downloaded successfully. Okay to start translating.
                Log.i("Success", "Japanese Model downloaded")

            }
            .addOnFailureListener { exception ->
                // Model couldn’t be downloaded or other internal error.
                Log.e("Error", "Model not downloaded")
            }


        japaneseEnglishTranslator.translate(translateText)
            .addOnSuccessListener { translatedText ->
                // Translation successful.
                Log.i("success", translateText)
            }
            .addOnFailureListener { exception ->
                // Error.
                // ...
                Log.e("error", "please wait 10-20 secondsfor model to download")
            }
        return translateText
    }
}