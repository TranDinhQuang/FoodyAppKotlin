package com.example.foodyappkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.QuanAn

class RestaurentMyselfAdapter(val context: Context, var quanAns: MutableList<QuanAn>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_restaurent, p0, false))
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}