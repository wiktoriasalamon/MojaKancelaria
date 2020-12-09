package com.piwniczna.mojakancelaria.activities.obligations

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.Models.ObligationEntity
import com.piwniczna.mojakancelaria.Models.ObligationType
import com.piwniczna.mojakancelaria.R

class ObligationsListAdapter(context: Context, var data: ArrayList<ObligationEntity>) :
        ArrayAdapter<ObligationEntity>(context, R.layout.layout_obligations_list_item, data), Filterable {
    private var obligations: ArrayList<ObligationEntity> = data

    internal class ViewHolder {
        var titleTextView: TextView? = null
        var amountTextView: TextView? = null
        var dateTextView: TextView? = null
        var typeTextView: TextView? = null
        var layout: RelativeLayout? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.layout_obligations_list_item, parent, false)

            val viewHolder = ViewHolder()
            viewHolder.titleTextView=view!!.findViewById(R.id.obligation_title)
            viewHolder.amountTextView=view!!.findViewById(R.id.obligation_amount)
            viewHolder.dateTextView=view!!.findViewById(R.id.obligation_date)
            viewHolder.typeTextView=view!!.findViewById(R.id.obligation_type)
            viewHolder.layout=view!!.findViewById(R.id.obligation_item)

            view.tag=viewHolder
        }

        val holder = view.tag as ViewHolder

        holder.titleTextView!!.text=obligations[position].name
        holder.amountTextView!!.text= context.resources.getString(R.string.amount_with_currency, obligations[position].amount.setScale(2).toString())
        holder.dateTextView!!.text=obligations[position].paymentDate.toString()
        holder.typeTextView!!.text=ObligationHelper.getTypeString(obligations[position].type, context)
        holder.layout!!.backgroundTintList = ObligationHelper.getColor(obligations[position], context)

        return view
    }

    override fun getCount(): Int {
        return obligations.size
    }

    override fun getItem(p0: Int): ObligationEntity {
        return obligations[p0]
    }

    override fun getItemId(p0: Int): Long {
        return obligations[p0].id.toLong()
    }

    fun filter(filterList: ArrayList<ObligationType>) {
        obligations = data.filter {
            it.type in filterList
        } as ArrayList<ObligationEntity>
        notifyDataSetChanged()
    }
}
