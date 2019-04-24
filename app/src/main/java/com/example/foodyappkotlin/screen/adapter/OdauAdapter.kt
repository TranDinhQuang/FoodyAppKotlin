package com.example.foodyappkotlin.screen.adapter

import android.content.Context
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

class OdauAdapter(var quanans: List<QuanAn>, val context: Context) : RecyclerView.Adapter<OdauAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OdauAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_odau, p0, false))
    }

    override fun getItemCount(): Int {
        return quanans.size
    }

    override fun onBindViewHolder(p0: OdauAdapter.ViewHolder, p1: Int) {
        p0.bindData(quanans[p1], context)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var v = view
        val storage = FirebaseStorage.getInstance().reference

        fun bindData(quanan: QuanAn, context: Context) {
            var storageRef: StorageReference = storage.child("monan").child("error")
            if ((quanan.hinhanhquanans.isNotEmpty())) {
                storageRef = storage.child("monan").child(quanan.hinhanhquanans[0])
            }
            if (quanan.binhluans.isNotEmpty()) {
                Log.d("kiemtra",quanan.binhluans[0].noidung)
                if (quanan.binhluans.size >= 2) {
                    v.text_cmt_one.text = quanan.binhluans[0].noidung
                    v.text_cmt_two.text = quanan.binhluans[1].noidung
                } else {
                    v.text_cmt_one.text = quanan.binhluans[0].noidung
                }
            }

            v.text_food.text = quanan.tenquanan
            v.text_address.text = quanan.diachi
            GlideApp.with(context)
                .load(storageRef)
                .error(R.drawable.ic_lock)
                .thumbnail(0.1f)
                .placeholder(R.drawable.ic_lock)
                .into(v.image_foody)
        }
    }

}
