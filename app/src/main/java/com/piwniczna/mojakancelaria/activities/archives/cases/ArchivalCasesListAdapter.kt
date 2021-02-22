package com.piwniczna.mojakancelaria.activities.archives.cases

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.piwniczna.mojakancelaria.models.CaseEntity
import com.piwniczna.mojakancelaria.R

class ArchivalCasesListAdapter(context: Context, var data: ArrayList<CaseEntity>, val activity: FragmentActivity) :
        ArrayAdapter<CaseEntity>(context, R.layout.layout_clients_list_item, data), Filterable {
    private var cases: ArrayList<CaseEntity> = data

    internal class ViewHolder {
        var nameTextView: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.layout_cases_list_item, parent, false)

            val viewHolder = ViewHolder()
            viewHolder.nameTextView=view!!.findViewById(R.id.case_name)

            view.tag=viewHolder
        }

        val holder = view.tag as ViewHolder

        holder.nameTextView!!.text=cases[position].name
        holder.nameTextView!!.backgroundTintList = activity.resources.getColorStateList(R.color.archive_light)


        return view
    }

    override fun getCount(): Int {
        return cases.size
    }

    override fun getItem(p0: Int): CaseEntity {
        return cases[p0]
    }

    override fun getItemId(p0: Int): Long {
        return cases[p0].id.toLong()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                cases = filterResults.values as ArrayList<CaseEntity>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
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
