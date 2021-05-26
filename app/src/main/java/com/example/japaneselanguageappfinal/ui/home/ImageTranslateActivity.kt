package com.example.japaneselanguageappfinal.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.japaneselanguageappfinal.R
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.*
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import java.io.ByteArrayOutputStream


//private lateinit var functions: FirebaseFunctions





class ImageTranslateActivity : AppCompatActivity() {

    var imageView: ImageView? = null
    var textView: TextView? = null
    var translatedTextView: TextView? = null
    lateinit var functions: FirebaseFunctions
    functions = Firebase.functions
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
                    // Model couldn’t be downloaded or other internal error.
                    Log.e("Error", "Model not downloaded")
                }


        /*
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

         */

        // Convert bitmap to base64 encoded string
        val byteArrayOutputStream = ByteArrayOutputStream()
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        }
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        val base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
        //functions = Firebase.functions


        // Create json request to cloud vision
        val request = JsonObject()
// Add image to request
        val image = JsonObject()
        image.add("content", JsonPrimitive(base64encoded))
        request.add("image", image)
//Add features to the request
        val feature = JsonObject()
        feature.add("type", JsonPrimitive("TEXT_DETECTION"))
// Alternatively, for DOCUMENT_TEXT_DETECTION:
// feature.add("type", JsonPrimitive("DOCUMENT_TEXT_DETECTION"))
        val features = JsonArray()
        features.add(feature)
        request.add("features", features)
        /* CAN PROVIDE HINTS
    val imageContext = JsonObject()
    val languageHints = JsonArray()
    languageHints.add("en")
    imageContext.add("languageHints", languageHints)
    request.add("imageContext", imageContext)

         */

        annotateImage(request.toString())
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        // Task failed with an exception
                        // ...
                        val annotation = task.result!!.asJsonArray[0].asJsonObject["fullTextAnnotation"].asJsonObject
                        System.out.format("%nComplete annotation:")
                        System.out.format("%n%s", annotation["text"].asString)
                        translatedTextView?.text = annotation["text"].asString

                    } else {
                        // Task completed successfully
                        // ...
                    }
                }






    }
    private fun annotateImage(requestJson: String): Task<JsonElement> {
        return functions
                .getHttpsCallable("annotateImage")
                .call(requestJson)
                .continueWith { task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    val result = task.result?.data
                    JsonParser.parseString(Gson().toJson(result))
                }
    }
}