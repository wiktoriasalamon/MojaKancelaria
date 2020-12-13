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
import com.piwniczna.mojakancelaria.Models.CaseEntity
import com.piwniczna.mojakancelaria.Models.RelationEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.utils.ObligationHelper

class ObligationsOfPaymentListAdapter(
        context: Context,
        var data: ArrayList<RelationEntity>,
        var dbService: DataService,
        val activity: FragmentActivity) :
        ArrayAdapter<RelationEntity>(context, R.layout.layout_obligations_list_item, data) {

    internal class ViewHolder {
        var titleTextView: TextView? = null
        var amountTextView: TextView? = null
        var caseAndTypeTextView: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.layout_payments_payed_obligations_list_item, parent, false)

            val viewHolder = ViewHolder()
            viewHolder.titleTextView=view!!.findViewById(R.id.payed_obligation_title)
            viewHolder.amountTextView=view!!.findViewById(R.id.payed_obligation_amount)
            viewHolder.caseAndTypeTextView = view!!.findViewById(R.id.payed_obligation_case_and_type)

            view.tag=viewHolder
        }

        val holder = view.tag as ViewHolder

        AsyncTask.execute {
            val ob = dbService.getObligation(data[position].obligationId)
            val case = dbService.getCase(ob.caseId)
            activity.runOnUiThread {
                holder.titleTextView!!.text=ob.name
                holder.amountTextView!!.text= context.resources.getString(R.string.plus_amount_with_currency, data[position].amount.setScale(2).toString())
                holder.caseAndTypeTextView!!.text = context.resources.getString(
                        R.string.case_and_type,
                        case.name,
                        ObligationHelper.getTypeString(ob.type, context)
                )
            }
        }

        return view
    }
}