package com.example.foodyappkotlin.screen.main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.foodyappkotlin.BaseApp
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseActivity
import com.example.foodyappkotlin.screen.adapter.ViewPagerAdapterMain
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.layout_main_activity.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main_activity)
        AndroidInjection.inject(this)
        init()
    }

    private fun init() {
        val manager: FragmentManager = supportFragmentManager
        val adapter = ViewPagerAdapterMain(manager)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        viewpager_restaurant.adapter = adapter
        tab_layout.setupWithViewPager(viewpager_restaurant)
        viewpager_restaurant.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.setTabsFromPagerAdapter(adapter)//deprecated
        tab_layout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewpager_restaurant))
    }

    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            val fragment: Fragment
            when (item.itemId) {
                R.id.navigation_home -> {
//                    toolbar.setTitle("Shop")
                    return true
                }
                R.id.navigation_notify -> {
//                    toolbar.setTitle("My Gifts")
                    return true
                }
                R.id.navigation_profile -> {
//                    toolbar.setTitle("Profile")
                    return true
                }
                R.id.navigation_save -> {
//                    toolbar.setTitle("Profile")
                    return true
                }
            }
            return false
        }
    }
}
