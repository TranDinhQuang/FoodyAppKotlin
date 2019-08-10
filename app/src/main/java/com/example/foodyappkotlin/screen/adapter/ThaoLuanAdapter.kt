package com.example.foodyappkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.ThaoLuan
import com.example.foodyappkotlin.di.module.GlideApp
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.item_thao_luan.view.*

class ThaoLuanAdapter(
    val context: Context,
    val thaoLuans: MutableList<ThaoLuan>,
    val userId: String,
    val quanAnId: String,
    val binhLuanId: String,
    val nodeRoot: DatabaseReference
) :
    RecyclerView.Adapter<ThaoLuanAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_thao_luan, p0, false))
    }

    override fun getItemCount(): Int {
        return thaoLuans.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        if (userId == thaoLuans[p1].taikhoan) {
            p0.itemView.txt_delete_thao_luan.visibility = View.VISIBLE
            p0.itemView.txt_delete_thao_luan.setOnClickListener {
                nodeRoot.child("binhluans").child(quanAnId).child(binhLuanId)
                    .child(thaoLuans[p1].id_thaoluan).removeValue()
            }
        } else {
            p0.itemView.txt_delete_thao_luan.visibility = View.GONE
        }
        p0.itemView.txt_user_name.text = thaoLuans[p1].username
        p0.itemView.txt_noi_dung.text = thaoLuans[p1].noidung
        GlideApp.with(context)
            .load(thaoLuans[p1].hinhanh)
            .error(R.drawable.placeholder)
            .centerCrop()
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(p0.itemView.img_avatar)
    }

    fun setThaoLuan(thaoLuan: ThaoLuan) {
        thaoLuans.add(thaoLuan)
        notifyDataSetChanged()
    }

    fun removeThaoLuan(thaoLuan: ThaoLuan) {
        thaoLuans.remove(thaoLuan)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
