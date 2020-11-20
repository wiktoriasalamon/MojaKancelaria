package com.piwniczna.mojakancelaria.activities.clients

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.piwniczna.mojakancelaria.R

class ClientsListAdapter(context: Context, var data: List<String>) :
        ArrayAdapter<String>(context, R.layout.layout_clients_list_item, data) {
    internal class ViewHolder {
        var nameTextView: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.layout_clients_list_item, parent, false)

            val viewHolder = ViewHolder()
            viewHolder.nameTextView=view!!.findViewById(R.id.clientName)

            view.tag=viewHolder
        }

        val holder = view.getTag() as ViewHolder

        holder.nameTextView!!.text=data[position]

        return view
    }
}