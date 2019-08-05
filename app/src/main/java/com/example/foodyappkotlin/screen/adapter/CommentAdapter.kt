package com.example.foodyappkotlin.screen.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
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

class CommentAdapter(val context: Context, var comments: MutableList<BinhLuan>,var listLiked : Map<String,String>,val view :  CommentAdapter.CommentOnCLickListerner) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    val storage = FirebaseStorage.getInstance().reference
    lateinit var storageRef: StorageReference

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, p0, false))
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemView.text_title.text = comments[p1].tieude
        p0.itemView.text_content_comment.text = comments[p1].noidung
        p0.itemView.text_title.text = comments[p1].tieude
        p0.itemView.txt_num_like.text = "${comments[p1].num_like} like"
        p0.itemView.txt_num_share.text = "${comments[p1].num_share} share"
        if (comments[p1].hinhanh.isNotEmpty()) {
            val imageUrl: ArrayList<String> = ArrayList(comments[p1].hinhanh.values)
            loadImage(p0.itemView, imageUrl)
        }
        listLiked.filterValues {
           it == comments[p1].id
        }.mapNotNull {
            p0.itemView.img_like.setImageResource(R.drawable.ic_like_red)
        }

        p0.itemView.layout_comment.setOnClickListener {
            view.onClickItemCommentListerner(comments[p1])
        }
    }

    private fun loadImage(view: View, hinhanhs: ArrayList<String>) {
        when (hinhanhs.size) {
            1 -> {
                view.group_one_picture.visibility = View.VISIBLE
                storageRef = storage.child("binhluan").child(hinhanhs[0])
                glideLoadImage(view.img_one_one, storageRef)
            }
            2 -> {
                view.group_two_picture.visibility = View.VISIBLE
                storageRef = storage.child("binhluan").child(hinhanhs[0])
                glideLoadImage(view.img_two_one, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[1])
                glideLoadImage(view.img_two_two, storageRef)
            }
            3 -> {
                view.group_three_picture.visibility = View.VISIBLE
                storageRef = storage.child("binhluan").child(hinhanhs[0])
                glideLoadImage(view.img_three_one, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[1])
                glideLoadImage(view.img_three_two, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[2])
                glideLoadImage(view.img_three_three, storageRef)

            }
            4 -> {
                view.group_four_picture.visibility = View.VISIBLE
                storageRef = storage.child("binhluan").child(hinhanhs[0])
                glideLoadImage(view.img_four_one, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[1])
                glideLoadImage(view.img_four_two, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[2])
                glideLoadImage(view.img_four_three, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[3])
                glideLoadImage(view.img_four_four, storageRef)
            }
            else -> {
                view.group_four_picture.visibility = View.VISIBLE
                storageRef = storage.child("binhluan").child(hinhanhs[0])
                glideLoadImage(view.img_four_one, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[1])
                glideLoadImage(view.img_four_two, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[2])
                glideLoadImage(view.img_four_three, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[3])
                glideLoadImage(view.img_four_four, storageRef)
                view.img_transparent.visibility = View.VISIBLE
                view.text_number.visibility = View.VISIBLE
            }
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

    fun onDataChanged(comment: BinhLuan) {
        comments.add(comment)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface CommentOnCLickListerner{
       fun onClickItemCommentListerner(binhLuan : BinhLuan)
    }
}
