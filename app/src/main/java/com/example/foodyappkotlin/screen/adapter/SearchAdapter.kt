package com.example.foodyappkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.di.module.GlideApp
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.item_odau.view.*
import kotlinx.android.synthetic.main.item_search.view.*

class SearchAdapter(
    val context: Context,
    val quanAns: MutableList<QuanAn>,
    val view: SearchAdapter.SearchOnClickListener
) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    val storage = FirebaseStorage.getInstance().reference


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SearchAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search, p0, false))
    }

    override fun getItemCount(): Int {
        return quanAns.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val hinhAnhQuanAns = ArrayList<String>(quanAns[p1].hinhanhs.values)
        val storageRef = storage.child("monan").child(hinhAnhQuanAns[0])

        p0.itemView.txt_ten_quan_an.text = quanAns[p1].tenquanan
        p0.itemView.txt_dia_chi.text = quanAns[p1].diachi
        p0.itemView.layout_item_search.setOnClickListener {
            view.onClickItemListerner(quanAns[p1])
        }

        GlideApp.with(context)
            .load(storageRef)
            .error(R.drawable.placeholder)
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(p0.itemView.img_quan_an)
    }

    fun addAllQuanAn(datas: MutableList<QuanAn>) {
        quanAns.clear()
        quanAns.addAll(datas)
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface SearchOnClickListener {
        fun onClickItemListerner(quanAn: QuanAn)
    }
}