package com.example.foodyappkotlin.screen.main.fragment.profile

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.foodyappkotlin.AppSharedPreference
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseFragment
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.di.module.GlideApp
import com.example.foodyappkotlin.screen.login.LoginActivity
import com.example.foodyappkotlin.screen.main.MainActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*
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
        mActivity.actionbarBack.visibility = View.GONE
    }

    private fun initData() {
        txt_logout.setOnClickListener {
            val intent = Intent(activityContext, LoginActivity::class.java)
            startActivity(intent)
            appSharedPreference.setToken("")
            mActivity.finish()
        }
        glideLoadImage(img_avatar_user, appSharedPreference.getUser().hinhanh)
        txt_user_name.text = appSharedPreference.getUser().tenhienthi
        txt_user.text = appSharedPreference.getUser().taikhoan
        txt_address.text = getAddress(appSharedPreference.getLocation().latitude,appSharedPreference.getLocation().longitude)
        if(appSharedPreference.getUser().permission == 2){
            txt_permission.text  = "Người dùng"
        }else{
            txt_permission.text  = "Admin"
        }
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

    fun getAddress(latitude: Double, longitude: Double): String {
        var addressString = ""
        val geo = Geocoder(activityContext, Locale.getDefault())
        val addresses = geo.getFromLocation(latitude, longitude, 1)
        if (addresses.isEmpty()) {
        } else {
            if (addresses.size > 0) {
                addresses.forEach {
                    addressString = it.getAddressLine(0)
                    Log.d("kiemtra", "dia chi ${it.getAddressLine(0)}")
                }
            }
        }
        return addressString
    }
}