package com.example.reviewfoodkotlin.screen.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.reviewfoodkotlin.R
import com.example.reviewfoodkotlin.data.response.MyOrdersResponse
import kotlinx.android.synthetic.main.item_order.view.*
import java.text.DecimalFormat

class MyOrdersAdapter(val context: Context, val orders: MutableList<MyOrdersResponse>) :
    RecyclerView.Adapter<MyOrdersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyOrdersAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order, p0, false))
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: MyOrdersAdapter.ViewHolder, p1: Int) {
        p0.itemView.txt_name_order.text = "Đơn hàng ${p1 + 1}"
        p0.itemView.txt_name_restaurant.text = orders[p1].tenquanan
        p0.itemView.txt_address_restaurant.text = orders[p1].diadiem
        p0.itemView.txt_status.text = "Đang xử lý"
        p0.itemView.txt_fee_ship_value.text = "${convertMoneyToStringMoney(orders[p1].phiship)} VND"
        p0.itemView.txt_accounting_value.text =
            "${convertMoneyToStringMoney(orders[p1].thanhtien)} VND"
        var builderItem = StringBuilder()
        orders[p1].monans.forEach {
            builderItem.append(it.value.ten)
            builderItem.append(": ")
            builderItem.append(convertMoneyToStringMoney(it.value.gia))
            builderItem.append(" x")
            builderItem.append(it.value.soluong)
            builderItem.append(", ")
        }
        p0.itemView.txt_item_order_value.text =
            builderItem.trim().substring(0, builderItem.length - 1)
    }

    fun convertMoneyToStringMoney(money: Long): String {
        val formatter = DecimalFormat("#,###")
        val formattedNumber = formatter.format(money)
        return formattedNumber
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}