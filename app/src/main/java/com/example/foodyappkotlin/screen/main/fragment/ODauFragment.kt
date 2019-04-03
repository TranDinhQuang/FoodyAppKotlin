package com.example.foodyappkotlin.screen.main.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.screen.adapter.OdauAdapter
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_odau.view.*
import javax.inject.Inject

class ODauFragment : Fragment() {
    private lateinit var lOdauAdapter: OdauAdapter

    @Inject
    lateinit var foodyRepository: FoodyRepository

    companion object {
        fun getInstance(): ODauFragment {
            return ODauFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_odau, container, false)
        AndroidSupportInjection.inject(this)
        initView(view)
        return view
    }


    private fun initView(view: View) {
        val quanans = foodyRepository.getQuanAns()
        lOdauAdapter = OdauAdapter(quanans, context!!)

        view.recycler_quan_an.adapter = lOdauAdapter
    }
}
