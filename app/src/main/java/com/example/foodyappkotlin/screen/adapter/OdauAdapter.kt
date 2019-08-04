package com.example.foodyappkotlin.screen.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.di.module.GlideApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.item_odau.view.*

class OdauAdapter(
    var quanans: MutableList<QuanAn>,
    val itemClickListener: OdauAdapter.OnClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mListQuanAn = this.quanans
    val storage = FirebaseStorage.getInstance().reference
    private var isLoading: Boolean = false

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        if (p1 == VIEW_TYPE_ITEM) {
            return ViewHolderItem(
                LayoutInflater.from(p0.context).inflate(
                    R.layout.item_odau,
                    p0,
                    false
                )
            )
        } else if (p1 == VIEW_TYPE_LOADING) {
            return ViewHolderLoading(
                LayoutInflater.from(p0.context).inflate(
                    R.layout.item_progressbar,
                    p0,
                    false
                )
            )
        }
        return ViewHolderItem(
            LayoutInflater.from(p0.context).inflate(
                R.layout.item_odau,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mListQuanAn.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if (p0 is ViewHolderItem) {
            bindData(p0.itemView, mListQuanAn[p1], itemClickListener)
        } else {

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mListQuanAn[position].id == "") VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    private fun bindData(v: View, quanan: QuanAn, onItemClick: OdauAdapter.OnClickListener) {

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
        } else {
            v.group.visibility = View.GONE
        }

        v.layout_item_eating.setOnClickListener {
            onItemClick.onItemClickListener(quanan)
        }
        v.text_comment.text = "${quanan.num_comments} bình luận"
        v.text_take_picture.text = "${quanan.num_images} hình ảnh"
        v.text_point.text = quanan.danhgia.toString()
        v.text_food.text = quanan.tenquanan
        v.text_address.text = quanan.diachi
        GlideApp.with(v.context)
            .load(storageRef)
            .error(R.drawable.placeholder)
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(v.image_foody)
    }

    fun addAllItem(quanans: MutableList<QuanAn>) {
        mListQuanAn.addAll(quanans)
        notifyDataSetChanged()
    }

    fun addLoading() {
        isLoading = true
        mListQuanAn.add(QuanAn())
        notifyDataSetChanged()
    }

    fun removeItemLast() {
        mListQuanAn.removeAt(mListQuanAn.size - 1)
    }

    inner class ViewHolderItem(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderLoading(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnClickListener {
        fun onItemClickListener(quanAn: QuanAn)
    }
}
