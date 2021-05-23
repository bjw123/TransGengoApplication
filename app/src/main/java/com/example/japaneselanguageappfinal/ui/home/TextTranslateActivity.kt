package com.example.japaneselanguageappfinal.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.japaneselanguageappfinal.R
import com.example.japaneselanguageappfinal.ui.draw.StrokeManager
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class TextTranslateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_translate)

        val editText = findViewById<EditText>(R.id.editText)
        val textView = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.button)


        //specify + initialize model
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.JAPANESE)
            .build()
        val englishJapaneseTranslator: Translator = Translation.getClient(options)
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        //download model if not already downloaded
        englishJapaneseTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                // Model downloaded successfully. Okay to start translating.
                Log.i("Success", "Japanese Model downloaded")

            }
            .addOnFailureListener { exception ->
                // Model couldn’t be downloaded or other internal error.
                Log.e("Error", "Model not downloaded")
            }

        //translation example
        englishJapaneseTranslator.translate("ready to translate")
            .addOnSuccessListener { translatedText ->
                // Translation successful.
                textView.text = translatedText
            }
            .addOnFailureListener { exception ->
                // Error.
                // ...
                textView.text = "please wait 10-20 secondsfor model to download"
            }



        //translate user input
        button.setOnClickListener {
            englishJapaneseTranslator.translate(editText.text.toString())
                .addOnSuccessListener { translatedText ->
                    // Translation successful.
                    textView.text = translatedText
                }
                .addOnFailureListener { exception ->
                    // Error.
                    // ...
                    textView.text = "translation failed"
                }
        }

    }
}