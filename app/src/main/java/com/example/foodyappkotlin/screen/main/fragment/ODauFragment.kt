package com.example.foodyappkotlin.screen.main.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.screen.adapter.OdauAdapter
import com.example.foodyappkotlin.screen.detail.DetailEatingActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_odau.*
import kotlinx.android.synthetic.main.fragment_odau.view.*
import java.util.ArrayList
import javax.inject.Inject

class ODauFragment : Fragment(), ODauInterface.View, OdauAdapter.OnClickListener {
    private lateinit var lOdauAdapter: OdauAdapter
    private lateinit var mView: View
    private lateinit var mODauPresenter: ODauPresenter
    private lateinit var quanans: List<QuanAn>

    @Inject
    lateinit var foodyRepository: FoodyRepository

    companion object {
        fun getInstance(): ODauFragment {
            return ODauFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        mODauPresenter.getQuanAns()
        lOdauAdapter = OdauAdapter(quanans, this)
        mView.recycler_quan_an.adapter = lOdauAdapter
    }

    override fun QuanAnsFailure(msg: String) {
        progressBar.visibility = View.GONE
    }

    override fun QuanAnsSuccess(quanans: List<QuanAn>) {
        progressBar.visibility = View.GONE
        lOdauAdapter.addAllItem(quanans)
    }

    override fun onItemClickListener(quanAn: QuanAn) {
        val intentDetailEating = DetailEatingActivity.newInstance(context!!,quanAn)
        startActivity(intentDetailEating)
    }
}
