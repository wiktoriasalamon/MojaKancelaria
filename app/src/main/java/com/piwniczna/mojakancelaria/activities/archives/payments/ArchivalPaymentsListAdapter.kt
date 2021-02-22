package com.piwniczna.mojakancelaria.activities.archives.payments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.piwniczna.mojakancelaria.models.PaymentEntity
import com.piwniczna.mojakancelaria.R

class ArchivalPaymentsListAdapter(context: Context, var data: ArrayList<PaymentEntity>, val activity: FragmentActivity) :
        ArrayAdapter<PaymentEntity>(context, R.layout.layout_obligations_list_item, data), Filterable {

    internal class ViewHolder {
        var titleTextView: TextView? = null
        var amountTextView: TextView? = null
        var dateTextView: TextView? = null
        var layout: LinearLayout? = null
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
            viewHolder.layout = view!!.findViewById(R.id.payment_item)

            view.tag = viewHolder
        }

        val holder = view.tag as ViewHolder

        holder.titleTextView!!.text = data[position].name
        holder.amountTextView!!.text = context.resources.getString(R.string.amount_with_currency, data[position].amount.setScale(2).toString())
        holder.dateTextView!!.text = data[position].convertDate()
        holder.layout!!.backgroundTintList = activity.resources.getColorStateList(R.color.archive_light)

        return view
    }



}
