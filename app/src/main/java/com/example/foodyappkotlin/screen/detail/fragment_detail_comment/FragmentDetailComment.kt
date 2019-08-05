package com.example.foodyappkotlin.screen.detail.fragment_detail_comment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.AppSharedPreference
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseFragment
import com.example.foodyappkotlin.data.models.BinhLuan
import com.example.foodyappkotlin.data.models.ThaoLuan
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.screen.adapter.ThaoLuanAdapter
import com.example.foodyappkotlin.screen.detail.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail_comment.*
import javax.inject.Inject

class FragmentDetailComment : BaseFragment(),DetailCommentInterface.View {

    var idQuanAn = ""

    lateinit var binhLuan: BinhLuan

    lateinit var mPresenter : DetailCommentPresenter

    lateinit var thaoLuanAdapter: ThaoLuanAdapter

    @Inject
    lateinit var repository: FoodyRepository

    companion object {
        fun newInstance(idQuanAn: String, binhLuan: BinhLuan): Fragment {
            val fragmentDetailComment = FragmentDetailComment()
            fragmentDetailComment.idQuanAn = idQuanAn
            fragmentDetailComment.binhLuan = binhLuan
            return fragmentDetailComment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_comment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        mPresenter = DetailCommentPresenter(repository)
        thaoLuanAdapter = ThaoLuanAdapter(activityContext,ArrayList())
        recycler_thao_luan.adapter = thaoLuanAdapter
        mPresenter.getThaoLuan(idQuanAn,binhLuan.id,this)

    }

    override fun getThaoLuanSuccess(data: ThaoLuan) {
        thaoLuanAdapter.setThaoLuan(data)
    }

    override fun getThaoLuanFailure(message: String) {
        Log.d("kiemtra","ThaoLuanFailure $message")
    }

}