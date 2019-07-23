package com.example.foodyappkotlin.screen.detail.fragment_post

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseFragment

class PostCommentFragment : BaseFragment() {
    companion object {
        fun newInstance(): Fragment {
            val postCommentFragment = PostCommentFragment()
            return postCommentFragment
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_comment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
