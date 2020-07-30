package com.example.covid19_tracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.list_of_items.view.*

class StateListAdapter (val list:List<StatewiseItem>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.list_of_items,parent,false)

        val item = list[position]

        view.cnfTv.text =  item.confirmed
        view.actTv.text =  item.active
        view.dthTv.text =  item.deaths
        view.rcvTv.text = item.recovered
        view.stateTv.text =  item.state

        return view
    }

    override fun getItem(position: Int): Any = list[position]

    override fun getItemId(position: Int) = position.toLong()
    override fun getCount() = list.size

}