package com.example.reviewfoodkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.reviewfoodkotlin.R
import com.example.reviewfoodkotlin.data.response.ThucDonResponse

class MyOrdersAdapter(val context: Context, val monAns: MutableList<ThucDonResponse>) :
    RecyclerView.Adapter<MyOrdersAdapter.ViewHolder>() {

    companion object {
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyOrdersAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order, p0, false))
    }

    override fun getItemCount(): Int {
        return monAns.size
    }

    override fun onBindViewHolder(p0: MyOrdersAdapter.ViewHolder, p1: Int) {

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}