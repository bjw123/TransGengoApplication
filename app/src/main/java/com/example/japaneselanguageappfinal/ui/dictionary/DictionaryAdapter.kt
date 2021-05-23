package com.example.japaneselanguageappfinal.ui.dictionary

import android.content.Intent
import android.graphics.Paint
import android.util.JsonReader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.japaneselanguageappfinal.R
import org.json.JSONArray
import org.json.JSONObject





class DictionaryAdapter(
        private val activity:
        FragmentActivity?, private val dictionaryList: JSONArray
) :
        RecyclerView.Adapter<DictionaryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.dictionary_element, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //refer to current index of json file
        val entry =  JSONObject(dictionaryList[position].toString())
        val readingEntry = entry.getJSONArray("japanese")[0].toString()
        val readingDefinition = JSONObject(readingEntry.toString())
        val meaningEntry = entry.getJSONArray("senses")[0].toString()
        val meaningDefinition = JSONObject(meaningEntry.toString())

        holder.word.text = entry.getString("slug")
        holder.reading.text = readingDefinition.getString("reading")

        //val formatDefinitions = meaningDefinition.get("english_definitions")
        holder.meaning.text = meaningDefinition.getJSONArray("english_definitions").toString()


        /*
        Glide.with(activity!!).load(user.userImage).placeholder(R.drawable.profile)
                .into(holder.imgUser)
        //holder.imgUser.setImageResource(R.drawable.profile)
        holder.layoutUser.setOnClickListener() {
            val intent = Intent(activity, MessageActivity::class.java)
            intent.putExtra("UserId", user.userId)
            activity?.startActivity(intent)
        }

         */
    }

    override fun getItemCount(): Int {
        return dictionaryList.length()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val word: TextView = view.findViewById(R.id.word)
        val reading: TextView = view.findViewById(R.id.reading_jsontxt)
        val meaning: TextView = view.findViewById(R.id.meaning_jsontxt)

    }

}






/*
class DictionaryAdapter (private val dicElement: ArrayList<JSONObject>) : RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder>() {

    class DictionaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryViewHolder { //default display
        return DictionaryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.dictionary_element, parent, false))
    }






    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {//when new item is added
        val thisElement = dicElement[position]

        holder.itemView.apply {word.text = thisElement["slug"]
        }


    }

    override fun getItemCount(): Int {
        return dicElement.size //return size
    }



}

 */
