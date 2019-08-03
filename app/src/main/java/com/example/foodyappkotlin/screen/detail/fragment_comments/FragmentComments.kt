package com.example.foodyappkotlin.screen.detail.fragment_comments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseFragment
import com.example.foodyappkotlin.data.models.BinhLuan
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.example.foodyappkotlin.screen.adapter.CommentAdapter
import com.example.foodyappkotlin.screen.detail.DetailViewModel
import kotlinx.android.synthetic.main.fragment_comment.*
import kotlinx.android.synthetic.main.item_empty_value.*
import javax.inject.Inject

class FragmentComments : BaseFragment() {
    lateinit var commentAdapter: CommentAdapter
    lateinit var detailViewModel: DetailViewModel
    lateinit var comments: MutableList<BinhLuan>

    @Inject
    lateinit var repository: FoodyRepository

    companion object {
        fun newInstance(): Fragment {
            val fragmentComments = FragmentComments()
            return fragmentComments
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        commentAdapter = CommentAdapter(activity!!, ArrayList())
        recycler_comments.visibility = View.VISIBLE
        recycler_comments.adapter = commentAdapter
        detailViewModel = activity?.run {
            ViewModelProviders.of(this).get(DetailViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        repository.getAllCommentFollowQuanAn(
            detailViewModel.quanan.value!!.id,
            object : FoodyDataSource.DataCallBack<BinhLuan> {
                override fun onSuccess(data: BinhLuan) {
                    commentAdapter.onDataChanged(data)
                }

                override fun onFailure(message: String) {
                    recycler_comments.visibility = View.GONE
                    layout_empty.visibility = View.VISIBLE
                    Toast.makeText(activityContext, "Có lỗi xảy ra", Toast.LENGTH_SHORT)
                }
            })
    }
}
