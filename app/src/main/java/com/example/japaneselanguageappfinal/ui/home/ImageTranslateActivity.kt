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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition

class ImageTranslateActivity : AppCompatActivity() {
    var imageView: ImageView? = null
    var textView: TextView? = null
    //@RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_translate)

        imageView = findViewById(R.id.imageId)
        //find textview
        textView = findViewById(R.id.textId)
        //check app level permission is granted for Camera
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //grant the permission
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)
        }
    }

    fun doProcess(view: View?) {
        //open the camera => create an Intent object
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val bundle = data!!.extras
        //from bundle, extract the image
        val bitmap = bundle!!["data"] as Bitmap?
        //set image in imageview
        imageView!!.setImageBitmap(bitmap)


        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient()
        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Task completed successfully
                // ...
                val resultText = visionText.text
                /*
                for (block in visionText.textBlocks) {
                    val blockText = block.text
                    val blockCornerPoints = block.cornerPoints
                    val blockFrame = block.boundingBox
                    for (line in block.lines) {
                        val lineText = line.text
                        val lineCornerPoints = line.cornerPoints
                        val lineFrame = line.boundingBox
                        for (element in line.elements) {
                            val elementText = element.text
                            val elementCornerPoints = element.cornerPoints
                            val elementFrame = element.boundingBox
                        }
                    }
                }
                 */
                //Call translater to convert to japanese


                textView!!.setText(resultText)
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
                Log.e("Error", "vision failed")
                textView!!.setText("failed")
            }

    }
}