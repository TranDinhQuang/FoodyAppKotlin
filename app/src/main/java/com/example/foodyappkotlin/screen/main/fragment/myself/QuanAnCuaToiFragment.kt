package com.example.foodyappkotlin.screen.main.fragment.myself

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseFragment
import com.example.foodyappkotlin.screen.adapter.RestaurentMyselfAdapter
import com.example.foodyappkotlin.screen.main.MainActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_my_restaurent.*
import javax.inject.Inject

class QuanAnCuaToiFragment : BaseFragment() {

    @Inject
    lateinit var mActivity : MainActivity

    companion object {
        fun newInstance(): Fragment {
            val quanAnCuaToiFragment = QuanAnCuaToiFragment()
            return quanAnCuaToiFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_my_restaurent, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recycler_restaurent_myself.layoutManager = linearLayoutManager
        var restaurentMyselfAdapter = RestaurentMyselfAdapter(activityContext, ArrayList())
        recycler_restaurent_myself.adapter = restaurentMyselfAdapter
        btn_add.setOnClickListener {
            mActivity.pushFragment(R.id.frame_layout,PostQuanAnFragment.newInstance())
        }
    }
}