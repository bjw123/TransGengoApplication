package com.example.japaneselanguageappfinal.ui.draw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.japaneselanguageappfinal.R

class DrawFragment : Fragment() {

    private lateinit var drawViewModel: DrawViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        drawViewModel =
                ViewModelProvider(this).get(DrawViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_draw, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        drawViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}