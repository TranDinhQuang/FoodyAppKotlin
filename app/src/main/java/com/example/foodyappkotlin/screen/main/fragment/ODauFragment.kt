package com.example.foodyappkotlin.screen.main.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.screen.adapter.OdauAdapter
import com.example.foodyappkotlin.screen.detail.DetailEatingActivity
import com.example.foodyappkotlin.view.EndlessRecyclerViewScrollListener
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_odau.*
import kotlinx.android.synthetic.main.fragment_odau.view.*
import java.util.*
import javax.inject.Inject


class ODauFragment : Fragment(), ODauInterface.View, OdauAdapter.OnClickListener {
    private lateinit var lOdauAdapter: OdauAdapter
    private lateinit var mView: View
    private lateinit var mODauPresenter: ODauPresenter
    private lateinit var quanans: List<QuanAn>
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    private var mPage = 1
    private var isLoading: Boolean = false


    @Inject
    lateinit var foodyRepository: FoodyRepository

    companion object {
        fun getInstance(): ODauFragment {
            return ODauFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_odau, container, false)
        AndroidSupportInjection.inject(this)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        progressBar.visibility = View.VISIBLE
        quanans = ArrayList()
        mODauPresenter = ODauPresenter(foodyRepository, this)
        mODauPresenter.getQuanAns(1, mPage)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mView.recycler_quan_an.layoutManager = linearLayoutManager
        lOdauAdapter = OdauAdapter(quanans as ArrayList<QuanAn>, this)
        mView.recycler_quan_an.adapter = lOdauAdapter
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (!isLoading) {
                    mPage += 1
                    Log.d("XXX", " onLoadMore page $mPage totalItemsCount $totalItemsCount")
                    isLoading = true
                    mODauPresenter.getQuanAns(1, mPage)
                    lOdauAdapter.addLoading()
                }
            }
        }

        mView.recycler_quan_an.addOnScrollListener(scrollListener)
    }

    override fun QuanAnsFailure(msg: String) {
        isLoading = false
        progressBar.visibility = View.GONE
    }

    override fun QuanAnsSuccess(quanans: MutableList<QuanAn>) {
        isLoading = false
        progressBar.visibility = View.GONE
        if (mPage != 1) {
            lOdauAdapter.removeItemLast()
        }
        lOdauAdapter.addAllItem(quanans)
    }

    override fun onItemClickListener(quanAn: QuanAn) {
        val intentDetailEating = DetailEatingActivity.newInstance(context!!, quanAn)
        startActivity(intentDetailEating)
    }
}
