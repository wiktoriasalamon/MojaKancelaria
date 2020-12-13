package com.piwniczna.mojakancelaria.activities.archives.clients

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.utils.ObligationHelper

class ArchivalClientsListAdapter(context: Context, var data: ArrayList<ClientEntity>, val activity: FragmentActivity) :
    ArrayAdapter<ClientEntity>(context, R.layout.layout_clients_list_item, data), Filterable {
    private var clients: ArrayList<ClientEntity> = data

    internal class ViewHolder {
        var nameTextView: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.layout_clients_list_item, parent, false)

            val viewHolder = ViewHolder()
            viewHolder.nameTextView = view!!.findViewById(R.id.clientName)

            view.tag = viewHolder
        }

        val holder = view.tag as ViewHolder

        holder.nameTextView!!.text = clients[position].name
        holder.nameTextView!!.backgroundTintList = activity.resources.getColorStateList(R.color.archive_light)

        return view
    }

    override fun getCount(): Int {
        return clients.size
    }

    override fun getItem(p0: Int): ClientEntity {
        return clients[p0]
    }

    override fun getItemId(p0: Int): Long {
        return clients[p0].id.toLong()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                clients = filterResults.values as ArrayList<ClientEntity>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    data
                else
                    data.filter {
                        it.name.toLowerCase().contains(queryString)
                    }
                return filterResults
            }
        }
    }
}