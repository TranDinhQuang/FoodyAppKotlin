package com.example.foodyappkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.di.module.GlideApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.item_picture_post.view.*

class PicturePostAdapter(val context: Context, val view: PicturePostAdapter.OnClickListener) :
        RecyclerView.Adapter<PicturePostAdapter.ViewHolder>() {
    var imgsFile: MutableList<String> = ArrayList()
    var isEditQuanAn = false
    val storage = FirebaseStorage.getInstance().reference
    lateinit var storageRef: StorageReference

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PicturePostAdapter.ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_picture_post, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return imgsFile.size
    }

    override fun onBindViewHolder(p0: PicturePostAdapter.ViewHolder, p1: Int) {
        if(isEditQuanAn){
            storageRef = storage.child("monan").child(imgsFile[p1])
            glideLoadImage(p0.itemView.image_upload, storageRef)
            if(p1 == imgsFile.size - 1){
                isEditQuanAn = false
            }
        }else{
            glideLoadImage(p0.itemView.image_upload, imgsFile[p1])
        }
        p0.itemView.img_close.setOnClickListener {
            view.removeImage(p1)
            imgsFile.removeAt(p1)
            notifyDataSetChanged()
        }
    }

    fun setImagePost(mPhotoPath: String) {
        imgsFile.add(mPhotoPath)
        notifyDataSetChanged()
    }


    fun setAllImagePost(mPhotoPath: List<String>) {
        isEditQuanAn = true
        imgsFile.addAll(mPhotoPath)
        notifyDataSetChanged()
    }

    private fun glideLoadImage(img: ImageView, url: StorageReference) {
        GlideApp.with(context)
            .load(url)
            .error(R.drawable.placeholder)
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(img)
    }

    private fun glideLoadImage(img: ImageView, url: String) {
        GlideApp.with(context)
                .load(url)
                .error(R.drawable.placeholder)
                .thumbnail(0.1f)
                .placeholder(R.drawable.placeholder)
                .into(img)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnClickListener {
        fun removeImage(position: Int)
    }
}
