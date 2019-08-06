package com.example.foodyappkotlin.screen.main.fragment.myself

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseFragment
import dagger.android.support.AndroidSupportInjection

class PostQuanAnFragment : BaseFragment() {
    companion object {
        fun newInstance(): Fragment {
            val postQuanAnFragment = PostQuanAnFragment()
            return postQuanAnFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_add_restaurent, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
    }
}