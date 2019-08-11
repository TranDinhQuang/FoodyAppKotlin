package com.example.foodyappkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.di.module.GlideApp
import com.example.foodyappkotlin.screen.detail.fragment_overview.OverviewFragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.item_my_restaurent.view.*

class RestaurentMyselfAdapter(val context: Context, var quanAns: MutableList<QuanAn>,val view : OnClickItemListerner) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val storage = FirebaseStorage.getInstance().reference
    lateinit var storageRef: StorageReference
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_restaurent, p0, false))
    }

    override fun getItemCount(): Int {
        return quanAns.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        p0.itemView.txt_ten_quan_an.text = quanAns[p1].tenquanan
        p0.itemView.txt_chi_nhanh.text = quanAns[p1].diachi
        var hinhAnhQuanAn = ArrayList<String>(quanAns[p1].hinhanhs.values)
        storageRef = storage.child("monan").child(hinhAnhQuanAn[0])
        glideLoadImage(p0.itemView.img_quan_an,storageRef)
        p0.itemView.layout_quanan.setOnClickListener {
            view.openOverViewFragment(quanAns[p1])
        }
    }

    private fun glideLoadImage(img: ImageView, url: StorageReference) {
        GlideApp.with(context)
            .load(url)
            .error(R.drawable.placeholder)
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(img)
    }

    fun addQuanAn(quanan  : QuanAn){
        quanAns.add(quanan)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnClickItemListerner{
        fun openOverViewFragment(quanan: QuanAn)

        fun editQuanAn(quanan: QuanAn)

        fun deleteQuanAn(quanan: QuanAn)
    }
}