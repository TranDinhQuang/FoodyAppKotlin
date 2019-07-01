package com.example.foodyappkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.BinhLuan
import com.example.foodyappkotlin.di.module.GlideApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.multiimage_layout.view.*

class CommentAdapter(val context: Context, val comments: List<BinhLuan>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    val storage = FirebaseStorage.getInstance().reference
    lateinit var storageRef: StorageReference

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, p0, false))
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemView.text_title.text = comments[p1].tieude
        p0.itemView.text_content_comment.text = comments[p1].noidung
        p0.itemView.text_title.text = comments[p1].tieude
        p0.itemView.sum_like.text = comments[p1].luotthich.toString()
        if (comments[p1].hinhanh.isNotEmpty()) {
            val imageUrl: ArrayList<String> = ArrayList(comments[p1].hinhanh.values)
            loadImage(p0.itemView, imageUrl)
        }
    }

    private fun loadImage(view: View, hinhanhs: ArrayList<String>) {
        when (hinhanhs.size) {
            1 -> {
                view.group_one_picture.visibility = View.VISIBLE
                storageRef = storage.child("monan").child(hinhanhs[0])
                glideLoadImage(view.img_one_one, storageRef)
            }
            2 -> {
                view.group_two_picture.visibility = View.VISIBLE
                storageRef = storage.child("monan").child(hinhanhs[0])
                glideLoadImage(view.img_two_one, storageRef)
                storageRef = storage.child("monan").child(hinhanhs[1])
                glideLoadImage(view.img_two_two, storageRef)
            }
            3 -> {
                view.group_three_picture.visibility = View.VISIBLE
                storageRef = storage.child("monan").child(hinhanhs[0])
                glideLoadImage(view.img_three_one, storageRef)
                storageRef = storage.child("monan").child(hinhanhs[1])
                glideLoadImage(view.img_three_two, storageRef)
                storageRef = storage.child("monan").child(hinhanhs[2])
                glideLoadImage(view.img_three_three, storageRef)

            }
            4 -> {
                view.group_four_picture.visibility = View.VISIBLE
                storageRef = storage.child("monan").child(hinhanhs[0])
                glideLoadImage(view.img_four_one, storageRef)
                storageRef = storage.child("monan").child(hinhanhs[1])
                glideLoadImage(view.img_four_two, storageRef)
                storageRef = storage.child("monan").child(hinhanhs[2])
                glideLoadImage(view.img_four_three, storageRef)
                storageRef = storage.child("monan").child(hinhanhs[3])
                glideLoadImage(view.img_four_four, storageRef)
            }
            else -> {
                view.group_four_picture.visibility = View.VISIBLE
                storageRef = storage.child("monan").child(hinhanhs[0])
                glideLoadImage(view.img_four_one, storageRef)
                storageRef = storage.child("monan").child(hinhanhs[1])
                glideLoadImage(view.img_four_two, storageRef)
                storageRef = storage.child("monan").child(hinhanhs[2])
                glideLoadImage(view.img_four_three, storageRef)
                storageRef = storage.child("monan").child(hinhanhs[3])
                glideLoadImage(view.img_four_four, storageRef)
                view.img_transparent.visibility = View.VISIBLE
                view.text_number.visibility = View.VISIBLE
            }
        }
    }

    private fun glideLoadImage(img: ImageView, url: StorageReference) {
        GlideApp.with(context)
            .load(url)
            .error(R.drawable.ic_lock)
            .thumbnail(0.1f)
            .placeholder(R.drawable.ic_lock)
            .into(img)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
