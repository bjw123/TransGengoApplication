package com.example.japaneselanguageappfinal.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.japaneselanguageappfinal.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val textTranslateButton: Button = root.findViewById(R.id.textTranslateButton)
        textTranslateButton.setOnClickListener({
            var clickintent = Intent(context, TextTranslateActivity::class.java)
            startActivity(clickintent)
        })

        val imgTranslateButton: Button = root.findViewById(R.id.img_trans_btn)
        imgTranslateButton.setOnClickListener({
            var clickintent = Intent(context, ImageTranslateActivity::class.java)
            startActivity(clickintent)
        })

        return root
    }
}