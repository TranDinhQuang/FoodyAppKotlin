package com.example.reviewfoodkotlin.screen.main.fragment.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.reviewfoodkotlin.R
import com.example.reviewfoodkotlin.common.BaseFragment
import com.example.reviewfoodkotlin.data.models.QuanAn
import com.example.reviewfoodkotlin.data.repository.FoodyRepository
import com.example.reviewfoodkotlin.data.source.remote.FoodyRemoteDataSource
import com.example.reviewfoodkotlin.screen.adapter.SearchAdapter
import com.example.reviewfoodkotlin.screen.detail.DetailEatingActivity
import com.example.reviewfoodkotlin.screen.main.MainActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_search_ui.*
import javax.inject.Inject

class SearchFragment : BaseFragment(), SearchInterface.View, SearchAdapter.SearchOnClickListener,
    TextWatcher, AdapterView.OnItemSelectedListener {
    var list_of_items_khuvuc = arrayOf("Hà Nội", "TP.Hồ Chí Minh")
    var mIdKhuVuc: String = "KV1"

    @Inject
    lateinit var mActivity: MainActivity

    @Inject
    lateinit var foodyRepository: FoodyRepository

    private lateinit var mSearchAdapter: SearchAdapter

    private lateinit var mPresenter: SearchInterface.Presenter

    companion object {
        fun newInstance(): Fragment {
            val searchFragment = SearchFragment()
            return searchFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_search_ui, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        mActivity.actionbarBack.visibility = View.GONE
    }

    private fun initData() {
        spinner_fillter_khuvuc.onItemSelectedListener = this
        val adapterSpinnerKhuVuc =
            ArrayAdapter(activity, android.R.layout.simple_spinner_item, list_of_items_khuvuc)
        spinner_fillter_khuvuc!!.adapter = adapterSpinnerKhuVuc
        spinner_fillter_khuvuc.setSelection(0, true)
        spinner_fillter_khuvuc.isSelected = false

        recycler_quanans.layoutManager = LinearLayoutManager(activityContext)
        mSearchAdapter = SearchAdapter(activityContext, ArrayList(), this)
        recycler_quanans.adapter = mSearchAdapter
        mPresenter = SearchPresenter(foodyRepository)
        edt_search.addTextChangedListener(this)
    }

    override fun searchSuccess(datas: MutableList<QuanAn>) {
        mSearchAdapter.addAllQuanAn(datas)
    }

    override fun searchFailure(message: String) {
        Log.d("kiemtra", message)
    }

    override fun afterTextChanged(p0: Editable?) {
        mPresenter.searchQuanAn(
            this,
            mIdKhuVuc,
            edt_search.text.toString(),
            FoodyRemoteDataSource.FILLTER_BY_NAME
        )
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p2) {
            0 -> {
                mIdKhuVuc = "KV1"
            }
            1 -> {
                mIdKhuVuc = "KV2"
            }
        }
    }


    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onClickItemListerner(quanAn: QuanAn) {
        val intentDetailEating = DetailEatingActivity.newInstance(context!!, quanAn)
        startActivity(intentDetailEating)
    }
}