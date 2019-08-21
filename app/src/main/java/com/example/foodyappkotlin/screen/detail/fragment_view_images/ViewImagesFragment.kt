package com.example.foodyappkotlin.screen.detail.fragment_view_images

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.AppSharedPreference
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseFragment
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.screen.detail.DetailEatingActivity
import com.example.foodyappkotlin.screen.detail.fragment_comments.FragmentComments
import javax.inject.Inject

class ViewImagesFragment : BaseFragment() {

    @Inject
    lateinit var mActivity: DetailEatingActivity

    @Inject
    lateinit var repository: FoodyRepository

    @Inject
    lateinit var appSharedPreference: AppSharedPreference


    companion object {
        fun newInstance(): Fragment {
            val fragmentComments = FragmentComments()
            return fragmentComments
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_images, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        mActivity.showActionBack(View.OnClickListener { mActivity.popFragment() })
    }

    private fun initData() {
    }

    override fun onStop() {
        super.onStop()
    }
}