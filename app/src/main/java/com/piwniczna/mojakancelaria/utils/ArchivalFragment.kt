package com.piwniczna.mojakancelaria.utils

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.R

open class ArchivalFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setActionBarColor()
        return view
    }


    fun setActionBarColor(){
        val bar = (activity as AppCompatActivity).supportActionBar
        bar?.setBackgroundDrawable(ColorDrawable(this.context!!.resources.getColor(R.color.archive_intence)))
    }
}