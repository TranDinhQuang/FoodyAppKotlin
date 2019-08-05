package com.example.foodyappkotlin.screen.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.example.foodyappkotlin.BaseApp
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseActivity
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.example.foodyappkotlin.screen.adapter.RestaurentMyselfAdapter
import com.example.foodyappkotlin.screen.adapter.ViewPagerAdapterMain
import com.example.foodyappkotlin.screen.detail.DetailEatingActivity
import com.example.foodyappkotlin.screen.main.fragment.ODauFragment
import com.example.foodyappkotlin.screen.main.fragment.home.HomeFragment
import com.example.foodyappkotlin.screen.main.fragment.myself.QuanAnCuaToiFragment
import com.example.foodyappkotlin.screen.main.fragment.search.SearchFragment
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.layout_main_activity.*
import javax.inject.Inject


class MainActivity : BaseActivity() {
    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main_activity)
        AndroidInjection.inject(this)
        init()
    }

    private fun init() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        pushFragmentWithoutBackStack(R.id.frame_layout,ODauFragment.getInstance())
    }

    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.navigation_home -> {
//                    toolbar.setTitle("Shop")
                    pushFragmentWithoutBackStack(R.id.frame_layout,ODauFragment.getInstance())
                    return true
                }
                R.id.navigation_notify -> {
//                    toolbar.setTitle("My Gifts")
                    pushFragmentWithoutBackStack(R.id.frame_layout,QuanAnCuaToiFragment.newInstance())
                    return true
                }
                R.id.navigation_profile -> {
                       pushFragmentWithoutBackStack(R.id.frame_layout,SearchFragment.newInstance())
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
