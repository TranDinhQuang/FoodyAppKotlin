package com.example.foodyappkotlin.screen.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.MonAn
import kotlinx.android.synthetic.main.item_menu_order.view.*
import kotlinx.android.synthetic.main.multiimage_layout.view.*

class MonAnAdapter(val context: Context, val monAns: MutableList<MonAn>, val type: Int,val view : MonAnAdapter.MonAnOnClickListener) :
    RecyclerView.Adapter<MonAnAdapter.ViewHolder>() {

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
                    view.monAnCalculatorMoney(-monAns[p1].gia)
                }
                p0.itemView.txt_number_order.text = "$number_order"
            }
            p0.itemView.img_plus.setOnClickListener {
                number_order++
                view.monAnCalculatorMoney(monAns[p1].gia)
                p0.itemView.txt_number_order.text = "$number_order"
            }
        } else {
            p0.itemView.layout_value.visibility = View.GONE
        }
        p0.itemView.text_food_name.text = monAns[p1].ten
        p0.itemView.text_food_price.text = monAns[p1].gia.toString()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface MonAnOnClickListener{
        fun monAnCalculatorMoney(money : Long)
    }
}
