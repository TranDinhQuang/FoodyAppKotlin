package com.example.foodyappkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.QuanAn

class SearchAdapter(
    val context: Context,
    val quanAns: MutableList<QuanAn>,
    val view: SearchAdapter.SearchOnClickListener
) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SearchAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search, p0, false))
    }

    override fun getItemCount(): Int {
        return quanAns.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

    }

    fun addQuanAn(quanAn: QuanAn) {
        quanAns.add(quanAn)
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface SearchOnClickListener {
    }
}