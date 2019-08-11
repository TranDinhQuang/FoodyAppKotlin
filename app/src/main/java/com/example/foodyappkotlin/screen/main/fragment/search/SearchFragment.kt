package com.example.foodyappkotlin.screen.main.fragment.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseFragment
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.source.remote.FoodyRemoteDataSource
import com.example.foodyappkotlin.screen.adapter.SearchAdapter
import com.example.foodyappkotlin.screen.detail.DetailEatingActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_search_ui.*
import javax.inject.Inject

class SearchFragment : BaseFragment(), SearchInterface.View, SearchAdapter.SearchOnClickListener,
    TextWatcher {

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
    }

    private fun initData() {
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
            "KV1",
            edt_search.text.toString(),
            FoodyRemoteDataSource.FILLTER_BY_NAME
        )
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