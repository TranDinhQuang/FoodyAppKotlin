package com.example.foodyappkotlin.screen.main.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.foodyappkotlin.AppSharedPreference
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.request.QuanAnRequest
import com.example.foodyappkotlin.data.source.remote.FoodyRemoteDataSource
import com.example.foodyappkotlin.screen.adapter.OdauAdapter
import com.example.foodyappkotlin.screen.detail.DetailEatingActivity
import com.example.foodyappkotlin.screen.menu.MenuActivity
import com.example.foodyappkotlin.view.EndlessRecyclerViewScrollListener
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_odau.*
import kotlinx.android.synthetic.main.fragment_odau.view.*
import javax.inject.Inject


class ODauFragment : Fragment(), ODauInterface.View, OdauAdapter.OnClickListener,
    AdapterView.OnItemSelectedListener {

    private lateinit var lOdauAdapter: OdauAdapter
    private lateinit var mView: View
    private lateinit var mODauPresenter: ODauPresenter
    private lateinit var mQuanans: MutableList<QuanAn>
    private lateinit var quanAnRequest: QuanAnRequest
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    var list_of_items = arrayOf("Mới nhất", "Cũ nhất", "Gần tôi")

    private var isLoading: Boolean = false

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        quanAnRequest = QuanAnRequest()
        quanAnRequest.idKhuVuc = "KV1"
        quanAnRequest.page = 1
        spinner_fillter!!.onItemSelectedListener = this
        val adapterSpinner =
            ArrayAdapter(activity, android.R.layout.simple_spinner_item, list_of_items)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_fillter!!.adapter = adapterSpinner

        progressBar.visibility = View.VISIBLE

        mQuanans = ArrayList()
        mODauPresenter = ODauPresenter(foodyRepository, this)

        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mView.recycler_quan_an.layoutManager = linearLayoutManager
        lOdauAdapter = OdauAdapter(ArrayList(), appSharedPreference.getLocation(), this)
        mView.recycler_quan_an.adapter = lOdauAdapter

     /*   scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (!isLoading) {
                    quanAnRequest.page  += 1
                    Log.d("XXX", " onLoadMore page ${quanAnRequest.page}  totalItemsCount $totalItemsCount")
                    isLoading = true
                    quanAnRequest.valueAt = mQuanans[mQuanans.size - 1].id
                    mODauPresenter.getQuanAns(quanAnRequest)
                    lOdauAdapter.addLoading()
                }
            }
        }

        mView.recycler_quan_an.addOnScrollListener(scrollListener)*/
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when(p2){
            0 ->{
                quanAnRequest.page = 1
                quanAnRequest.typeCall = FoodyRemoteDataSource.SORT_BY_DATE_ASC
                mODauPresenter.getQuanAns(quanAnRequest)
            }
            1->{
                quanAnRequest.page = 1
                quanAnRequest.typeCall = FoodyRemoteDataSource.SORT_BY_DATE_DESC
                mODauPresenter.getQuanAns(quanAnRequest)
            }
            2 ->{
                quanAnRequest.page = 1
            }

        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun QuanAnsFailure(msg: String) {
        isLoading = false
        progressBar.visibility = View.GONE
    }

    override fun QuanAnsSuccess(quanans: MutableList<QuanAn>) {
        isLoading = false
        progressBar.visibility = View.GONE
   /*     if (quanAnRequest.page != 1) {
            lOdauAdapter.removeItemLast()
        }*/
        mQuanans.addAll(quanans)
        lOdauAdapter.addAllItem(quanans)
    }

    override fun onItemClickListener(quanAn: QuanAn) {
        val intentDetailEating = DetailEatingActivity.newInstance(context!!, quanAn)
        startActivity(intentDetailEating)
    }

    override fun startActivityMenu() {
        startActivity(MenuActivity.newInstance(context!!))
    }
}
