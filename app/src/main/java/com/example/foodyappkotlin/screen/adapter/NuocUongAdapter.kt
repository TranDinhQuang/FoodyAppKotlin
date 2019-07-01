package com.example.foodyappkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.NuocUong
import kotlinx.android.synthetic.main.item_menu.view.*

class NuocUongAdapter(val context: Context, val nuocUongs: List<NuocUong>) :
    RecyclerView.Adapter<NuocUongAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NuocUongAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_menu, p0, false))
    }

    override fun getItemCount(): Int {
        return nuocUongs.size
    }

    override fun onBindViewHolder(p0: NuocUongAdapter.ViewHolder, p1: Int) {
        p0.itemView.text_food_name.text = nuocUongs[p1].ten
        p0.itemView.text_food_price.text = nuocUongs[p1].gia.toString()
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
