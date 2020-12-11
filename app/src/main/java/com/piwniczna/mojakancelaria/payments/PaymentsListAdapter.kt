package com.piwniczna.mojakancelaria.payments

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.Models.ObligationEntity
import com.piwniczna.mojakancelaria.Models.ObligationType
import com.piwniczna.mojakancelaria.Models.PaymentEntity
import com.piwniczna.mojakancelaria.R

class PaymentsListAdapter(context: Context, var data: ArrayList<PaymentEntity>) :
        ArrayAdapter<PaymentEntity>(context, R.layout.layout_obligations_list_item, data), Filterable {
    private var payments: ArrayList<PaymentEntity> = data

    internal class ViewHolder {
        var titleTextView: TextView? = null
        var amountTextView: TextView? = null
        var dateTextView: TextView? = null
        var layout: RelativeLayout? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.layout_payment_list_item, parent, false)

            val viewHolder = ViewHolder()
            viewHolder.titleTextView = view!!.findViewById(R.id.payment_title)
            viewHolder.amountTextView = view!!.findViewById(R.id.payment_amount)
            viewHolder.dateTextView = view!!.findViewById(R.id.payment_date)
            viewHolder.layout = view!!.findViewById(R.id.obligation_item)

            view.tag = viewHolder
        }

        val holder = view.tag as ViewHolder

        holder.titleTextView!!.text = data[position].name
        holder.amountTextView!!.text = context.resources.getString(R.string.amount_with_currency, data[position].amount.setScale(2).toString())
        holder.dateTextView!!.text = data[position].date.toString()

        return view
    }



}
