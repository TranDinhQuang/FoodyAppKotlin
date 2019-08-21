package com.example.reviewfoodkotlin.screen.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import android.view.View
import com.example.reviewfoodkotlin.R
import com.example.reviewfoodkotlin.common.BaseActivity
import com.example.reviewfoodkotlin.screen.main.fragment.ODauFragment
import com.example.reviewfoodkotlin.screen.main.fragment.myself.QuanAnCuaToiFragment
import com.example.reviewfoodkotlin.screen.main.fragment.profile.ProfileFragment
import com.example.reviewfoodkotlin.screen.main.fragment.search.SearchFragment
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.layout_main_activity.*

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
        pushFragmentWithoutBackStack(R.id.frame_layout, ODauFragment.getInstance())
    }

    private val mOnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_home -> {
                        pushFragmentWithoutBackStack(R.id.frame_layout, ODauFragment.getInstance())
                        return true
                    }
                    R.id.navigation_restaurent -> {
                        pushFragmentWithoutBackStack(
                            R.id.frame_layout,
                            QuanAnCuaToiFragment.newInstance()
                        )
                        return true
                    }
                    R.id.navigation_profile -> {
                        pushFragmentWithoutBackStack(
                            R.id.frame_layout,
                            ProfileFragment.newInstance()
                        )
                        return true
                    }
                    R.id.navigation_search -> {
                        pushFragmentWithoutBackStack(
                            R.id.frame_layout,
                            SearchFragment.newInstance()
                        )
                        return true
                    }
                }
                return false
            }
        }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun showActionBack(listerner : View.OnClickListener){
        actionbarBack.visibility = View.VISIBLE
        actionbarBack.setOnClickListener(listerner)
    }
}
