package com.example.japaneselanguageappfinal.ui.draw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.japaneselanguageappfinal.*

class CanvasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)


        val drawingView = findViewById<DrawingView>(R.id.drawingView)
        val recognize = findViewById<TextView>(R.id.recognize)
        val clear = findViewById<Button>(R.id.clear)


        //init strokemanager object and call to recognize what is on canvas
        StrokeManager.download()
        recognize.setOnClickListener {
            StrokeManager.recognize(this)
        }

        //clear canvas and stroke manager
        clear.setOnClickListener {
            drawingView.clear()
            StrokeManager.clear()
        }
    }
}