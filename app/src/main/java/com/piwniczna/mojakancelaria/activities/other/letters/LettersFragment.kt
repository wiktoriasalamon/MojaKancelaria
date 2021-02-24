package com.piwniczna.mojakancelaria.activities.other.letters

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.clients.clients_list.ClientsFragment
import com.piwniczna.mojakancelaria.models.*
import com.piwniczna.mojakancelaria.trackingmore.APIService
import java.lang.Exception
import java.net.ConnectException
import kotlin.collections.ArrayList
//TODO()


class LettersFragment(var outgoing: Boolean)  : Fragment() {
    lateinit var lettersListView: ListView
    lateinit var title: TextView
    lateinit var lettersList: ArrayList<Letter>
    lateinit var lettersListAdapter: LettersListAdapter
    lateinit var dbService: DataService


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_letters, container, false)
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

        toastMessage("Ładowanie danych z serwera...")
        getLettersFromDB()

        return view
    }

    private fun toastMessage(message: String) {
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(context, message, duration)
        toast.show()
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            ClientsFragment()
        )?.commit()
    }

    fun getLettersFromDB() {
        AsyncTask.execute {
            val rawLetters = dbService.getLetters().filter { it.outgoing == outgoing }
            val numbers = rawLetters.map { it -> it.number }
            try {
                lettersList.clear()

                for (n in numbers.reversed()){
                    Log.e("Loading ","-$n")
                    val letter = APIService.getLetter(n)
                    lettersList.add(letter)
                    activity?.runOnUiThread {
                        lettersListAdapter.notifyDataSetChanged()
                    }
                }

                Thread.sleep(600)
                activity?.runOnUiThread {
                    toastMessage("Załadowano dane!")
                }
            }
            catch (e: ConnectException){
                e.printStackTrace()
                Thread.sleep(500)
                activity?.runOnUiThread {
                    toastMessage("Błąd łączenia z serwerem\nSprawdź połączenie internetowe...")
                }
            }
            catch (e: Exception){
                e.printStackTrace()
                Thread.sleep(500)
                activity?.runOnUiThread {
                    toastMessage("Błąd podczas pobierania danych...")
                }
            }
        }
    }




}
