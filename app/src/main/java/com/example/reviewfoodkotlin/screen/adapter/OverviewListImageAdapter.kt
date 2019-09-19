package com.example.reviewfoodkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.reviewfoodkotlin.R
import com.example.reviewfoodkotlin.di.module.GlideApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.item_list_image.view.*

class OverviewListImageAdapter(
    val context: Context,
    val listImage: MutableList<String>,
    val view: OverviewListImageAdapter.OverviewListImageOnClickListener
) :
    RecyclerView.Adapter<OverviewListImageAdapter.ViewHolder>() {
    val storage = FirebaseStorage.getInstance().reference
    lateinit var storageRef: StorageReference

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OverviewListImageAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_image, p0, false))
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        storageRef = storage.child("monan").child(listImage[p1])
        glideLoadImage(context, p0.itemView.image_view, storageRef)
        p0.itemView.image_view.setOnClickListener {
            view.imageOnlickListerner(listImage[p1])
        }
    }

    private fun glideLoadImage(context: Context, img: ImageView, url: StorageReference) {
        GlideApp.with(context)
            .load(url)
            .error(R.drawable.placeholder)
            .centerCrop()
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(img)
    }

    override fun getItemCount(): Int {
        return listImage.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OverviewListImageOnClickListener {
        fun imageOnlickListerner(url :String)
    }
}
