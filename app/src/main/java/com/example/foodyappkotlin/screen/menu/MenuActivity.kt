package com.example.foodyappkotlin.screen.menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseActivity
import com.example.foodyappkotlin.data.models.ThucDon
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.example.foodyappkotlin.screen.adapter.MonAnAdapter
import com.example.foodyappkotlin.screen.adapter.NuocUongAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_menu.*
import javax.inject.Inject

class MenuActivity : BaseActivity() {

    @Inject
    lateinit var foodyRepository: FoodyRepository


    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, MenuActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        AndroidInjection.inject(this)
        callApi()
    }

    fun initData(thucDon: ThucDon){
        val monAnAdapter = MonAnAdapter(this,thucDon.monAns,MonAnAdapter.TYPE_ORDER)
        val nuocUongAdapter = NuocUongAdapter(this,thucDon.nuocUongs,NuocUongAdapter.TYPE_ORDER)
        recycler_menu_monan.adapter = monAnAdapter
        recycler_menu_nuocuong.adapter = nuocUongAdapter
    }

    fun callApi(){
        foodyRepository.getThucDons("MATHUCDON1",object : FoodyDataSource.DataCallBack<ThucDon>{
            override fun onSuccess(data: ThucDon) {
                initData(data)
            }

            override fun onFailure(message: String) {

            }

        })
    }
}