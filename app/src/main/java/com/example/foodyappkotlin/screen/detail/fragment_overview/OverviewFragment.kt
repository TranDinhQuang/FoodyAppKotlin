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
import com.example.foodyappkotlin.data.models.BinhLuan
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.models.ThucDon
import com.example.foodyappkotlin.di.module.GlideApp
import com.example.foodyappkotlin.screen.adapter.CommentAdapter
import com.example.foodyappkotlin.screen.adapter.MonAnAdapter
import com.example.foodyappkotlin.screen.adapter.NuocUongAdapter
import com.example.foodyappkotlin.screen.detail.DetailEatingActivity
import com.example.foodyappkotlin.screen.detail.DetailViewModel
import com.example.foodyappkotlin.screen.detail.fragment_comments.FragmentComments
import com.example.foodyappkotlin.screen.detail.fragment_post.PostCommentFragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_detail_eating.*
import kotlinx.android.synthetic.main.layout_feature.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

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
        AndroidSupportInjection.inject(this)
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
            if (item.binhluans.isNotEmpty()) {
                recycler_user_comment.visibility = View.VISIBLE
                text_view_all_comment.text = "Xem thêm"
                findCommentData(item.binhluans)
            } else {
                recycler_user_comment.visibility = View.GONE
                text_view_all_comment.text = "Hãy là người đầu tiên đánh giá quán ăn"
            }
            findThucDonData(item.thucdons)
        })

        ln_post_comment.setOnClickListener {
            mActivity.pushFragment(R.id.layout_food_detail, PostCommentFragment.newInstance())
        }

        text_view_all_comment.setOnClickListener {
            mActivity.pushFragment(R.id.layout_food_detail, FragmentComments.newInstance())
        }
    }

    fun findQuanAnData(quanAn: QuanAn) {
        var storageRef: StorageReference = storage.child("error")
        if ((quanAn.hinhanhquanans.isNotEmpty())) {
            storageRef = storage.child("monan").child(quanAn.hinhanhquanans[0])
        }
        GlideApp.with(activityContext)
            .load(storageRef)
            .error(R.drawable.placeholder)
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(image_eating)

        var sum = 0
        text_sum_comment.text = quanAn.binhluans.size.toString()
        quanAn.binhluans.forEach {
            if (it.hinhanh.isNotEmpty()) {
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

/*
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
        })*/
        recycler_menu.isNestedScrollingEnabled = false
    }

    fun findThucDonData(thucDon: ThucDon) {
     /*   recycler_menu.visibility = View.VISIBLE
        text_menu_viewmore.text = "Xem thêm"
        findThucDonData(item.thucdons)
        recycler_menu.visibility = View.GONE
        text_menu_viewmore.text = "Quán ăn chưa có thực đơn"*/
        if (thucDon.monAns.size > 0) {
            monAnAdapter = MonAnAdapter(activity!!, thucDon.monAns)
            recycler_menu.adapter = monAnAdapter
        }else if (thucDon.nuocUongs.size > 0) {
            nuocUongAdapter = NuocUongAdapter(activity!!, thucDon.nuocUongs)
            recycler_menu.adapter = nuocUongAdapter
        }
    }

    fun findCommentData(binhluans: ArrayList<BinhLuan>) {
        if (!binhluans.isEmpty()) {
            val gson = Gson()
            Log.d("data", gson.toJson(binhluans))
            commentAdapter = CommentAdapter(activity!!, binhluans,ArrayList())
            recycler_user_comment.layoutManager = LinearLayoutManager(activityContext)
            recycler_user_comment.adapter = commentAdapter
            recycler_user_comment.isNestedScrollingEnabled = false
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

}
