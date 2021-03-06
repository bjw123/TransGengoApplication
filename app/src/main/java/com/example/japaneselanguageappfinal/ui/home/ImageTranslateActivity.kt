package com.example.japaneselanguageappfinal.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.japaneselanguageappfinal.R
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition

class ImageTranslateActivity : AppCompatActivity() {
    var imageView: ImageView? = null
    var textView: TextView? = null
    var translatedTextView: TextView? = null
    //@RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_translate)

        imageView = findViewById(R.id.imageId)
        //find textview
        textView = findViewById(R.id.textId)
        translatedTextView = findViewById(R.id.translatedText)
        //check app level permission is granted for Camera
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //grant the permission
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)
        }




    }

    //take Photo
    fun doProcess(view: View?) {
        //open the camera => create an Intent object
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 101)
    }


    //on image selected
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val bundle = data!!.extras
        //from bundle, extract the image
        val bitmap = bundle!!["data"] as Bitmap?
        //set image in imageview
        imageView!!.setImageBitmap(bitmap)
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
                    translatedTextView?.text = "Ready to Translate"

                }
                .addOnFailureListener { exception ->
                    // Model couldn???t be downloaded or other internal error.
                    Log.e("Error", "Model not downloaded")
                }


        //Pass bitmap over to library
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient()
        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Task completed successfully
                // ...
                val resultText = visionText.text
                textView!!.setText(resultText)
                englishJapaneseTranslator.translate(resultText)
                        .addOnSuccessListener { translatedText ->
                            // Translation successful.
                            translatedTextView?.text = translatedText
                        }
                        .addOnFailureListener { exception ->
                            // Error.
                            // ...
                            translatedTextView?.text   = "please try again in 20 seconds (model has not downloaded)"
                        }
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
                Log.e("Error", "vision failed")
                textView!!.setText("failed")
            }



    }
}