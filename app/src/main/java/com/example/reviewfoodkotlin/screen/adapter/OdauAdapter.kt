package com.example.reviewfoodkotlin.screen.adapter

import android.content.Context
import android.location.Location
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.reviewfoodkotlin.R
import com.example.reviewfoodkotlin.data.models.BinhLuan
import com.example.reviewfoodkotlin.data.models.QuanAn
import com.example.reviewfoodkotlin.di.module.GlideApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.item_odau.view.*
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.round

class OdauAdapter(
    var quanans: MutableList<QuanAn>, val locationDevider: Location, val permission: Int,
    val itemClickListener: OdauAdapter.OnClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var nodeRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
    val storage = FirebaseStorage.getInstance().reference

    var mListQuanAn = this.quanans
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
            bindData(p0.itemView, mListQuanAn[p1], locationDevider, itemClickListener)
        } else {

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mListQuanAn[position].id == "") VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    private fun bindData(
        v: View,
        quanan: QuanAn,
        locationDevider: Location,
        onItemClick: OdauAdapter.OnClickListener
    ) {

        var storageRef: StorageReference = storage.child("error")
        val hinhAnhQuanAn = ArrayList<String>()
        quanan.hinhanhs.forEach {
            hinhAnhQuanAn.add(it.value)
        }
        if (hinhAnhQuanAn.size > 0) {
            storageRef = storage.child("monan").child(hinhAnhQuanAn[0])
        }
        if (quanan.binhluans.isNotEmpty()) {
            var listBinhLuan = ArrayList<BinhLuan>(quanan.binhluans.values)
            v.group.visibility = View.VISIBLE
            v.group2.visibility = View.VISIBLE
            if (quanan.binhluans.size >= 2) {
                v.text_cmt_one.text = listBinhLuan[0].noidung
                glideLoadImage(v.context, v.image_avatar_comment, listBinhLuan[0].hinhanh_user)
                v.text_cmt_two.text = listBinhLuan[1].noidung
                glideLoadImage(
                    v.context,
                    v.image_avatar_comment_second,
                    listBinhLuan[1].hinhanh_user
                )
            } else if (quanan.binhluans.size == 1) {
                v.text_cmt_one.text = listBinhLuan[0].noidung
                glideLoadImage(v.context, v.image_avatar_comment, listBinhLuan[0].hinhanh_user)
                v.group2.visibility = View.GONE
            }
        } else {
            v.group.visibility = View.GONE
            v.group2.visibility = View.GONE
        }

        v.layout_item_eating.setOnClickListener {
            onItemClick.onItemClickListener(quanan)
        }

        v.text_comment.text = "${quanan.binhluans.size} bình luận"
        v.text_take_picture.text = "${quanan.hinhanhs.size} hình ảnh"
        var diemQuanAn = 0.0
        quanan.binhluans.mapNotNull {
            diemQuanAn += it.value.chamdiem
        }
        if (diemQuanAn > 0) {
            diemQuanAn /= quanan.binhluans.size
        }
        v.text_point.text = "${(roundOffDecimal(diemQuanAn))}"
        v.text_food.text = quanan.tenquanan
        v.text_address.text = quanan.diachi

        if (permission == 1) {
            v.txt_delete_restaurant.visibility = View.VISIBLE
            v.txt_delete_restaurant.setOnClickListener {
                itemClickListener.deleteRestaurant(quanan)
            }
        }else{
            v.txt_delete_restaurant.visibility = View.GONE
        }

        if (locationDevider != null) {
            v.text_distance.text = "${distance(
                locationDevider.latitude,
                locationDevider.longitude,
                quanan.latitude,
                quanan.longitude
            )} km"
        }

        if (quanan.giaohang) {
            v.button_order.visibility = View.VISIBLE
            v.button_order.setOnClickListener {
                itemClickListener.startActivityMenu(quanan)
            }
        } else {
            v.button_order.visibility = View.GONE
        }

        GlideApp.with(v.context)
            .load(storageRef)
            .error(R.drawable.placeholder)
            .centerCrop()
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(v.image_foody)
    }

    fun roundOffDecimal(number: Double): Double? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(number).toDouble()
    }

    private fun glideLoadImage(context: Context, img: ImageView, url: String) {
        GlideApp.with(context)
            .load(url)
            .error(R.drawable.placeholder)
            .centerCrop()
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(img)
    }


    fun distance(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
        val loc1 = Location("")
        loc1.latitude = lat1
        loc1.longitude = long1

        val loc2 = Location("")
        loc2.latitude = lat2
        loc2.longitude = long2
        val distance = Math.round(loc1.distanceTo(loc2) / 1000 * 100) / 100.0
        return distance
    }

    fun addAllItem(quanans: MutableList<QuanAn>) {
        mListQuanAn.clear()
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

        fun startActivityMenu(quanAn: QuanAn)

        fun deleteRestaurant(quanAn: QuanAn)
    }
}
