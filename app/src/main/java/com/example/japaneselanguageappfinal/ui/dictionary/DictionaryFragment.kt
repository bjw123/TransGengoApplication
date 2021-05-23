package com.example.japaneselanguageappfinal.ui.dictionary

import android.content.res.AssetManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.japaneselanguageappfinal.R
import org.json.JSONArray
import org.json.JSONObject

class DictionaryFragment : Fragment() {

    private lateinit var dictionaryViewModel: DictionaryViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {
        dictionaryViewModel =
                ViewModelProvider(this).get(DictionaryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dictionary, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        //val testText: TextView = root.findViewById(R.id.test)
        val recyclerView: RecyclerView = root.findViewById(R.id.dictionary_recycle)
        dictionaryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        //Get JSON for JLPT N5
        val jsonString = context?.assets?.readAssetsFile("n5.json")
        val jsonArray = JSONArray(jsonString)
        //testText.text = jsonArray[1].toString()

        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.adapter = DictionaryAdapter(activity, jsonArray)





        return root
    }

    fun AssetManager.readAssetsFile(fileName : String): String = open(fileName).bufferedReader().use{it.readText()}
}