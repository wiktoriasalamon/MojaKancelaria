package com.piwniczna.mojakancelaria.activities.other.letters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.piwniczna.mojakancelaria.R


class LettersListAdapter(context: Context, var data: ArrayList<Letter>) :
    ArrayAdapter<Letter>(context, R.layout.layout_letter_list_item, data), Filterable {
    private var letters: ArrayList<Letter> = data

    internal class ViewHolder {
        var numberTextView: TextView? = null
        var daysTextView: TextView? = null
        var statusTextView: TextView? = null
        var layout: LinearLayout? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.layout_letter_list_item, parent, false)

            val viewHolder = ViewHolder()
            viewHolder.numberTextView=view!!.findViewById(R.id.letter_number)
            viewHolder.daysTextView=view!!.findViewById(R.id.letter_days_left)
            viewHolder.statusTextView=view!!.findViewById(R.id.letter_status)
            viewHolder.layout=view!!.findViewById(R.id.letter_item)

            view.tag=viewHolder
        }

        val holder = view.tag as ViewHolder

        holder.numberTextView!!.text = letters[position].number
        holder.daysTextView!!.text = "${letters[position].days} dni"
        holder.statusTextView!!.text = letters[position].status
        holder.layout!!.backgroundTintList = getColor(letters[position], context)

        return view
    }

    override fun getCount(): Int {
        return letters.size
    }

    override fun getItem(p0: Int): Letter {
        return letters[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    fun getItemNumber(p0: Int): String {
        return letters[p0].number
    }

    private fun getColor(letter: Letter, context: Context): ColorStateList{
        if (letter.days == "-") {
            return context.resources.getColorStateList(R.color.payed)
        }
        if (letter.days == "??") {
            return context.resources.getColorStateList(R.color.grey)
        }
        val days = letter.days.toInt()
        if (days < 2) {
            return context.resources.getColorStateList(R.color.overdue)
        }
        if (days < 4) {
            return context.resources.getColorStateList(R.color.little_overdue)
        }
        if (days < 8) {
            return context.resources.getColorStateList(R.color.inprogress)
        }
        return context.resources.getColorStateList(R.color.topay)
    }

}
