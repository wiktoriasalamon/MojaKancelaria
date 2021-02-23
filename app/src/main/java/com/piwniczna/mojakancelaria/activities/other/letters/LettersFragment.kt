package com.piwniczna.mojakancelaria.activities.other.letters

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.models.*
import kotlin.collections.ArrayList
//TODO()


class LettersFragment(var client: ClientEntity, var case: CaseEntity, var outgoing: Boolean)  : Fragment() {
    lateinit var lettersListView: ListView
    lateinit var title: TextView
    lateinit var lettersList: ArrayList<Letter>
    lateinit var lettersListAdapter: LettersListAdapter
    lateinit var dbService: DataService


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_obligations, container, false)
        dbService = DataService(this.context!!)

        lettersListView = view.findViewById(R.id.letters_list_view) as ListView

        lettersList = arrayListOf()
        lettersListAdapter = LettersListAdapter(this.context!!, lettersList)
        lettersListView.adapter = lettersListAdapter

        lettersListView.setOnItemLongClickListener { _, _, position, _ ->
            //todo: delete by position
            true
        }

        val title = view.findViewById<TextView>(R.id.letters_title)
        if(outgoing){
            title.text="Wysłane"
            title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sent, 0, 0, 0)
        }
        else{
            title.text="Awiza"
            title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_awizo, 0, 0, 0)
        }


        getLettersFromDB()

        return view
    }

    fun onBackPressed() {
        //TODO()
    }

    fun getLettersFromDB() {
        AsyncTask.execute {
            val rawLetters = dbService.getLetters().filter { it.outgoing == outgoing }
            //todo limit to max 100
            val numbers = rawLetters.map { it -> it.number }
            //todo get data from api
            //read data from api


            TODO()
            /*
            val letters = ""
            lettersList.clear()
            lettersList.addAll()
            activity?.runOnUiThread {
                obligationsListAdapter.notifyDataSetChanged()
            }*/
        }
    }




}
