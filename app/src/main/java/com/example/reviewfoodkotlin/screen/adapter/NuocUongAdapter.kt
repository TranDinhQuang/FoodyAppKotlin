package com.example.reviewfoodkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.reviewfoodkotlin.R
import com.example.reviewfoodkotlin.data.models.NuocUong
import kotlinx.android.synthetic.main.item_menu_order.view.*

class NuocUongAdapter(val context: Context, val nuocUongs: MutableList<NuocUong>,val type : Int,val view : NuocUongAdapter.NuocUongOnClickListener) :
    RecyclerView.Adapter<NuocUongAdapter.ViewHolder>() {
    var number_order: Int = 0

    companion object {
        val TYPE_VIEW = 1
        val TYPE_ORDER = 2
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NuocUongAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_menu_order, p0, false))
    }

    override fun getItemCount(): Int {
        return nuocUongs.size
    }

    override fun onBindViewHolder(p0: NuocUongAdapter.ViewHolder, p1: Int) {
        if(type == NuocUongAdapter.TYPE_ORDER){
            var number_order = 0
            p0.itemView.layout_value.visibility = View.VISIBLE
            p0.itemView.img_minus.setOnClickListener {
                if (number_order > 0) {
                    number_order--
                    view.nuocUongCalculatorMoney(-nuocUongs[p1].gia)
                }
                p0.itemView.txt_number_order.text = "$number_order"
            }
            p0.itemView.img_plus.setOnClickListener {
                number_order++
                view.nuocUongCalculatorMoney(nuocUongs[p1].gia)
                p0.itemView.txt_number_order.text = "$number_order"
            }
        }else{
            p0.itemView.layout_value.visibility = View.GONE
        }
        p0.itemView.text_food_name.text = nuocUongs[p1].ten
        p0.itemView.text_food_price.text = nuocUongs[p1].gia.toString()
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface NuocUongOnClickListener{
        fun nuocUongCalculatorMoney(money : Long)
    }
}
