package com.example.foodyappkotlin.screen.main

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.screen.adapter.ViewPagerAdapterMain
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.layout_main_activity.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main_activity)
        AndroidInjection.inject(this)
        init()
    }

    private fun init() {
        val manager: FragmentManager = supportFragmentManager
        val adapter = ViewPagerAdapterMain(manager)
        viewpager_restaurant.adapter = adapter
        tab_layout.setupWithViewPager(viewpager_restaurant)
        viewpager_restaurant.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.setTabsFromPagerAdapter(adapter)//deprecated
        tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewpager_restaurant))
    }

    private fun Firebase() {
    }
}
