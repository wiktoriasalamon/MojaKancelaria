package com.piwniczna.mojakancelaria.activities.payments.add_payment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.piwniczna.mojakancelaria.Models.ObligationEntity
import com.piwniczna.mojakancelaria.R

class ObligationsToChooseListAdapter(context: Context, var data: ArrayList<ObligationEntity>) :
        ArrayAdapter<ObligationEntity>(context, R.layout.layout_obligations_to_pay_list_item, data) {

    internal class ViewHolder {
        var titleTextView: TextView? = null
        var amountTextView: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.layout_obligations_to_pay_list_item, parent, false)

            val viewHolder = ViewHolder()
            viewHolder.titleTextView = view!!.findViewById(R.id.obligation_to_pay_title)
            viewHolder.amountTextView = view!!.findViewById(R.id.obligation_to_pay_amount)
            view.tag=viewHolder
        }

        val holder = view.tag as ViewHolder

        holder.titleTextView!!.text = data[position].name
        holder.amountTextView!!.text = context.resources.getString(R.string.payed_amount_with_currency, data[position].payed.setScale(2).toString(), data[position].amount.setScale(2).toString())

        return view
    }
}