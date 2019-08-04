package com.example.foodyappkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.MonAn
import kotlinx.android.synthetic.main.item_menu_order.view.*

class MonAnAdapter(val context: Context, val monAns: MutableList<MonAn>,val type  :Int) :
    RecyclerView.Adapter<MonAnAdapter.ViewHolder>() {
    companion object {
        val TYPE_VIEW = 1
        val TYPE_ORDER = 2
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_menu_order, p0, false))
    }

    override fun getItemCount(): Int {
        return monAns.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        if(type == TYPE_ORDER){
            p0.itemView.layout_value.visibility = View.VISIBLE
        }else{
            p0.itemView.layout_value.visibility = View.GONE
        }
        p0.itemView.text_food_name.text = monAns[p1].ten
        p0.itemView.text_food_price.text = monAns[p1].gia.toString()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
