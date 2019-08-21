package com.example.reviewfoodkotlin.screen.main.fragment.home

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.reviewfoodkotlin.R
import com.example.reviewfoodkotlin.common.BaseFragment
import com.example.reviewfoodkotlin.screen.adapter.ViewPagerAdapterMain
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {
    companion object {
        fun newInstance(): Fragment {
            val homeFragment = HomeFragment()
            return homeFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_home, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        val manager: FragmentManager = activityContext.supportFragmentManager
        val adapter = ViewPagerAdapterMain(manager)
        viewpager_restaurant.adapter = adapter
        tab_layout.setupWithViewPager(viewpager_restaurant)
        viewpager_restaurant.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                tab_layout
            )
        )
        tab_layout.setTabsFromPagerAdapter(adapter)//deprecated
        tab_layout.addOnTabSelectedListener(
            TabLayout.ViewPagerOnTabSelectedListener(
                viewpager_restaurant
            )
        )
    }
}