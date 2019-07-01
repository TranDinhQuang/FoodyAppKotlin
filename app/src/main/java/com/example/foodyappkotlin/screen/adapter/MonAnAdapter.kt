package com.example.foodyappkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.MonAn
import kotlinx.android.synthetic.main.item_menu.view.*

class MonAnAdapter(val context: Context, val monAns: List<MonAn>) :
    RecyclerView.Adapter<MonAnAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_menu, p0, false))
    }

    override fun getItemCount(): Int {
        return monAns.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
//        p0.itemView.img_food_menu
        p0.itemView.text_food_name.text = monAns[p1].ten
        p0.itemView.text_food_price.text = monAns[p1].gia.toString()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
