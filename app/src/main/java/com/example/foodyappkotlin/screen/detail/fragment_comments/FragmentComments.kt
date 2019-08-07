package com.example.foodyappkotlin.screen.detail.fragment_comments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.screen.adapter.CommentAdapter
import com.example.foodyappkotlin.screen.detail.DetailViewModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_comment.*
import kotlinx.android.synthetic.main.item_empty_value.*
import javax.inject.Inject

class FragmentComments : BaseFragment(), CommentAdapter.CommentOnCLickListerner {

    lateinit var commentAdapter: CommentAdapter
    lateinit var detailViewModel: DetailViewModel
    lateinit var comments: MutableList<BinhLuan>
    lateinit var mPresenter: CommentsPresenter
    lateinit var dataRef: Query
    lateinit var childEventListener: ChildEventListener

    @Inject
    lateinit var repository: FoodyRepository

    @Inject
    lateinit var appSharedPreference: AppSharedPreference


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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    private fun initData() {
        mPresenter = CommentsPresenter(repository, this)

        detailViewModel = activity?.run {
            ViewModelProviders.of(this).get(DetailViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


        commentAdapter =
            CommentAdapter(activity!!, ArrayList(),
                appSharedPreference.getToken()!!, appSharedPreference.getUser().liked, this)
        recycler_comments.adapter = commentAdapter
        detailViewModel.quanan.observe(this, Observer<QuanAn> { item ->
            if (item != null) {
//                mPresenter.getAllComment(item.id)
                getAllCommentFollowQuanAn(item.id)
            }
        })
    }

    fun getAllCommentFollowQuanAn(idQuanAn: String) {
        dataRef =
            FirebaseDatabase.getInstance().reference.child("quanans").child("KV1").child(idQuanAn)
                .child("binhluans")
        var binhluans = ArrayList<BinhLuan>()

        childEventListener = object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val comment = p0.getValue(BinhLuan::class.java)
                if (comment != null) {
                    binhluans.add(comment)
                    getAllCommentSuccess(comment)
                } else {
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        }
        dataRef.addChildEventListener(childEventListener)
    }

    fun getAllCommentSuccess(data: BinhLuan) {
        if (recycler_comments.visibility == View.GONE) {
            recycler_comments.visibility = View.VISIBLE
        }
        commentAdapter.onDataChanged(data)
    }

    fun getAllCommentFailure(message: String) {
        if (commentAdapter.comments.isEmpty()) {
            recycler_comments.visibility = View.GONE
            layout_empty.visibility = View.VISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        if (dataRef != null) {
            dataRef.removeEventListener(childEventListener)
        }

    }

    override fun onClickEditComment(binhLuan: BinhLuan) {
    }

    override fun onClickDeleteComment(binhLuan: BinhLuan) {
    }

    override fun onClickItemCommentListerner(binhLuan: BinhLuan) {

    }
}
