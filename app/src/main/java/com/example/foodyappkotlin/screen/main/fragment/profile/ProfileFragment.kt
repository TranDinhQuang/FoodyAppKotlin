package com.example.foodyappkotlin.screen.main.fragment.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.foodyappkotlin.AppSharedPreference
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseFragment
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.di.module.GlideApp
import com.example.foodyappkotlin.screen.adapter.SearchAdapter
import com.example.foodyappkotlin.screen.login.LoginActivity
import com.example.foodyappkotlin.screen.main.MainActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment : BaseFragment() {

    @Inject
    lateinit var foodyRepository: FoodyRepository

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    @Inject
    lateinit var mActivity: MainActivity

    companion object {
        fun newInstance(): Fragment {
            val profileFragment = ProfileFragment()
            return profileFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_profile, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        txt_logout.setOnClickListener {
            val intent = Intent(activityContext, LoginActivity::class.java)
            startActivity(intent)
            appSharedPreference.setToken("")
            mActivity.finish()
        }
        glideLoadImage(img_avatar_user,appSharedPreference.getUser().hinhanh)

    }


    private fun glideLoadImage(img: ImageView, url: String) {
        GlideApp.with(activityContext)
            .load(url)
            .error(R.drawable.placeholder)
            .fitCenter()
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(img)
    }

}