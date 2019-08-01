package com.example.foodyappkotlin.screen.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.di.module.GlideApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.item_odau.view.*

class OdauAdapter(var quanans: List<QuanAn>, val itemClickListener: OdauAdapter.OnClickListener) :
    RecyclerView.Adapter<OdauAdapter.ViewHolder>() {
    val mListQuanAn = this.quanans
    val storage = FirebaseStorage.getInstance().reference

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OdauAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_odau, p0, false))
    }

    override fun getItemCount(): Int {
        return mListQuanAn.size
    }

    override fun onBindViewHolder(p0: OdauAdapter.ViewHolder, p1: Int) {
        bindData(p0.itemView,mListQuanAn[p1], itemClickListener)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(v :View, quanan: QuanAn, onItemClick: OdauAdapter.OnClickListener){
        var storageRef: StorageReference = storage.child("error")
        if ((quanan.hinhanhquanans.isNotEmpty())) {
            storageRef = storage.child("monan").child(quanan.hinhanhquanans[0])
        }
        if (quanan.binhluans.isNotEmpty()) {
            v.group.visibility = View.VISIBLE
            if (quanan.binhluans.size >= 2) {
                v.text_cmt_one.text = quanan.binhluans[0].noidung
                v.text_cmt_two.text = quanan.binhluans[1].noidung
            } else {
                v.text_cmt_one.text = quanan.binhluans[0].noidung
            }
        }else{
            v.group.visibility = View.GONE
        }

        v.layout_item_eating.setOnClickListener {
            onItemClick.onItemClickListener(quanan)
        }
        v.text_comment.text = "${quanan.num_comments} bình luận"
        v.text_take_picture.text =  "${quanan.num_images} hình ảnh"
        v.text_point.text = quanan.danhgia.toString()
        v.text_food.text = quanan.tenquanan
        v.text_address.text = quanan.diachi
        GlideApp.with(v.context)
            .load(storageRef)
            .error(R.drawable.ic_lock)
            .thumbnail(0.1f)
            .placeholder(R.drawable.ic_lock)
            .into(v.image_foody)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnClickListener {
        fun onItemClickListener(quanAn: QuanAn)
    }
}
