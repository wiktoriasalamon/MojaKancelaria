package com.piwniczna.mojakancelaria.activities.payments.add_payment

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.ObligationEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.utils.ObligationHelper

class ObligationsToChooseListAdapter(
    context: Context,
    var data: ArrayList<ObligationEntity>,
    var dbService: DataService,
    val activity: FragmentActivity) :
        ArrayAdapter<ObligationEntity>(context, R.layout.layout_obligations_to_pay_list_item, data) {

    internal class ViewHolder {
        var titleTextView: TextView? = null
        var amountTextView: TextView? = null
        var caseAndTypeTextView: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.layout_obligations_to_pay_list_item, parent, false)

            val viewHolder = ViewHolder()
            viewHolder.titleTextView = view!!.findViewById(R.id.obligation_to_pay_title)
            viewHolder.amountTextView = view!!.findViewById(R.id.obligation_to_pay_amount)
            viewHolder.caseAndTypeTextView = view!!.findViewById(R.id.obligation_to_pay_case_and_type)
            view.tag=viewHolder
        }

        val holder = view.tag as ViewHolder

        AsyncTask.execute {
            val case = dbService.getCase(data[position].caseId)
            activity.runOnUiThread {
                holder.titleTextView!!.text = data[position].name
                holder.amountTextView!!.text = context.resources.getString(
                    R.string.payed_amount_with_currency,
                    data[position].payed.setScale(2).toString(),
                    data[position].amount.setScale(2).toString())
                holder.caseAndTypeTextView!!.text = context.resources.getString(
                    R.string.case_and_type,
                    case.name,
                    ObligationHelper.getTypeString(data[position].type, context)
                )
            }
        }

        return view
    }
}