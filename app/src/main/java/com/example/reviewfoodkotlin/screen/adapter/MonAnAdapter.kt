package com.example.reviewfoodkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.reviewfoodkotlin.R
import com.example.reviewfoodkotlin.data.response.ThucDonResponse
import com.example.reviewfoodkotlin.di.module.GlideApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.item_menu_order.view.*

class MonAnAdapter(
    val context: Context,
    val monAns: MutableList<ThucDonResponse>,
    val type: Int,
    val view: MonAnAdapter.MonAnOnClickListener
) :
    RecyclerView.Adapter<MonAnAdapter.ViewHolder>() {
    val storage = FirebaseStorage.getInstance().reference

    companion object {
        val TYPE_VIEW = 1
        val TYPE_ORDER = 2
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_menu_order, p0, false))
    }

    override fun getItemCount(): Int {
        return monAns.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        if (type == TYPE_ORDER) {
            var number_order = 0
            p0.itemView.layout_value.visibility = View.VISIBLE
            p0.itemView.img_minus.setOnClickListener {
                if (number_order > 0) {
                    number_order--
                    monAns[p1].soluong--
                    view.monAnCalculatorMoney(-monAns[p1].gia)
                }
                p0.itemView.txt_number_order.text = "$number_order"
            }
            p0.itemView.img_plus.setOnClickListener {
                number_order++
                monAns[p1].soluong++
                view.monAnCalculatorMoney(monAns[p1].gia)
                p0.itemView.txt_number_order.text = "$number_order"
            }
        } else {
            p0.itemView.layout_value.visibility = View.GONE
        }
        val storageRef = storage.child("monan").child(monAns[p1].hinhanh)
        glideLoadImage(p0.itemView.img_food_menu, storageRef)
        p0.itemView.text_food_name.text = monAns[p1].ten
        p0.itemView.text_food_price.text = monAns[p1].gia.toString()
    }

    private fun glideLoadImage(img: ImageView, url: String) {
        GlideApp.with(context)
            .load(url)
            .error(R.drawable.placeholder)
            .centerCrop()
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(img)
    }

    private fun glideLoadImage(img: ImageView, url: StorageReference) {
        GlideApp.with(context)
            .load(url)
            .error(R.drawable.placeholder)
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(img)
    }

    fun addThucDon(thucdon: ThucDonResponse) {
        monAns.add(thucdon)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface MonAnOnClickListener {
        fun monAnCalculatorMoney(money: Long)
    }
}
