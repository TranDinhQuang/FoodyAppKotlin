package com.example.foodyappkotlin.screen.detail.fragment_overview

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseFragment
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.models.ThucDon
import com.example.foodyappkotlin.di.module.GlideApp
import com.example.foodyappkotlin.screen.adapter.CommentAdapter
import com.example.foodyappkotlin.screen.adapter.MonAnAdapter
import com.example.foodyappkotlin.screen.adapter.NuocUongAdapter
import com.example.foodyappkotlin.screen.detail.DetailEatingActivity
import com.example.foodyappkotlin.screen.detail.DetailViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_detail_eating.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class OverviewFragment : BaseFragment() {
    lateinit var inputParser: SimpleDateFormat
    lateinit var detailViewModel: DetailViewModel
    lateinit var monAnAdapter: MonAnAdapter
    lateinit var nuocUongAdapter: NuocUongAdapter
    lateinit var commentAdapter: CommentAdapter
    val storage = FirebaseStorage.getInstance().reference

    @Inject
    lateinit var mActivity: DetailEatingActivity

    companion object {
        private const val INPUT_FORMAT = "HH:mm"
        fun newInstance(): Fragment {
            val overviewFragment = OverviewFragment()
            return overviewFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_eating, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }


    fun initData() {
        inputParser = SimpleDateFormat(INPUT_FORMAT, Locale.ENGLISH)
        detailViewModel = activity?.run {
            ViewModelProviders.of(this).get(DetailViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        detailViewModel.quanan.observe(this, Observer<QuanAn> { item ->
            findQuanAnData(item!!)
            findCommentData(item!!)
        })
    }

    fun findQuanAnData(quanAn: QuanAn) {
        var storageRef: StorageReference = storage.child("error")
        if ((quanAn.hinhanhquanans.isNotEmpty())) {
            storageRef = storage.child("monan").child(quanAn.hinhanhquanans[0])
        }
        GlideApp.with(activityContext)
            .load(storageRef)
            .error(R.drawable.ic_lock)
            .thumbnail(0.1f)
            .placeholder(R.drawable.ic_lock)
            .into(image_eating)

        var sum = 0
        text_sum_comment.text = quanAn.binhluans.size.toString()
        quanAn.binhluans.forEach {
            if(it.hinhanh.isNotEmpty()) {
                sum += it.hinhanh.size
            }
        }.let {
            text_count_image.text = sum.toString()
        }
        text_name_eating.text = quanAn.tenquanan
        text_location.text = quanAn.diachi
//        text_distance
        if (compareTimes(quanAn.giomocua, quanAn.giodongcua)) {
            text_status.text = "Đang mở cửa: ${quanAn.giomocua} - ${quanAn.giodongcua}"
        } else {
            text_status.text = "Đang đóng cửa: Mở cửa vào lúc ${quanAn.giomocua}"
        }


        detailViewModel.thucdon.observe(this, Observer<ThucDon> { item ->
            run {
                if (item!!.monAns.size > 0) {
                    monAnAdapter = MonAnAdapter(activity!!, item.monAns)
                    recycler_menu.adapter = monAnAdapter
                } else if (item!!.nuocUongs.size > 0) {
                    nuocUongAdapter = NuocUongAdapter(activity!!, item.nuocUongs)
                    recycler_menu.adapter = nuocUongAdapter
                }
            }
        })
    }

    fun findCommentData(quanAn: QuanAn) {
        if (!quanAn.binhluans.isEmpty()) {
            val gson = Gson()
            Log.d("data", gson.toJson(quanAn.binhluans))
            commentAdapter = CommentAdapter(activity!!, quanAn.binhluans)
            recycler_user_comment.layoutManager = LinearLayoutManager(activityContext)
            recycler_user_comment.adapter = commentAdapter
        }
    }

    fun compareTimes(timeOpen: String, timeClose: String): Boolean {
        val now = Calendar.getInstance()
        val hour = now.get(Calendar.HOUR)
        val minute = now.get(Calendar.MINUTE)
        var date: Date = parseDate("$hour:$minute")
        val timeOne = parseDate(timeOpen)
        val timeTwo = parseDate(timeClose)
        if (timeOne.before(date) && timeTwo.after(date)) {
            return true
        }
        return false
    }

    private fun parseDate(date: String): Date {
        return try {
            inputParser.parse(date)
        } catch (e: java.text.ParseException) {
            Date(0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
