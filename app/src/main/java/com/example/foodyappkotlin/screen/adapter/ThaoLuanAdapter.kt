package com.example.foodyappkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.ThaoLuan
import com.example.foodyappkotlin.di.module.GlideApp
import kotlinx.android.synthetic.main.item_thao_luan.view.*

class ThaoLuanAdapter(val context: Context, val thaoLuans: MutableList<ThaoLuan>) :
    RecyclerView.Adapter<ThaoLuanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_thao_luan, p0, false))
    }

    override fun getItemCount(): Int {
        return thaoLuans.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemView.txt_user_name.text = thaoLuans[p1].username
        p0.itemView.txt_noi_dung.text = thaoLuans[p1].noidung
        GlideApp.with(context)
            .load(thaoLuans[p1].hinhanh)
            .error(R.drawable.placeholder)
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(p0.itemView.img_avatar)
    }

    fun setThaoLuan(thaoLuan: ThaoLuan) {
        thaoLuans.add(thaoLuan)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
