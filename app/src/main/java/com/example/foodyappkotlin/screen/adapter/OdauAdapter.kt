package com.example.foodyappkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.QuanAn

class OdauAdapter(var quanans: List<QuanAn>, val context: Context) : RecyclerView.Adapter<OdauAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OdauAdapter.ViewHolder {
        Log.d("odaufragment", "${quanans.size}")
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_odau, p0, false))
    }

    override fun getItemCount(): Int {
        return quanans.size
    }

    override fun onBindViewHolder(p0: OdauAdapter.ViewHolder, p1: Int) {
        p0.bindData()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData() {

        }
    }

}