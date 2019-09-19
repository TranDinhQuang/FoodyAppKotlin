package com.example.reviewfoodkotlin.screen.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.reviewfoodkotlin.screen.main.fragment.GanToiFragment
import com.example.reviewfoodkotlin.screen.main.fragment.ODauFragment


class ViewPagerAdapterMain(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    lateinit var bundle: Bundle

    companion object {
        private const val TAB_O_DAU = 0
        private const val TAB_GAN_TOI = 1

    }

    override fun getItem(p0: Int): Fragment {
        return when (p0) {
            TAB_O_DAU -> ODauFragment.getInstance()
            else -> {
                GanToiFragment.getInstance()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Ở đâu"
            1 -> "Ăn gì"
            else -> {
                "Ở đâu"
            }
        }
    }
}
