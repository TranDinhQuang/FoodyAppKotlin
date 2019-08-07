package com.example.foodyappkotlin.screen.menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseActivity
import com.example.foodyappkotlin.data.models.ThucDon
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.response.ThucDonResponse
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.example.foodyappkotlin.screen.adapter.MonAnAdapter
import com.example.foodyappkotlin.screen.adapter.NuocUongAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_menu.*
import javax.inject.Inject
import java.text.DecimalFormat


class MenuActivity : BaseActivity(),MonAnAdapter.MonAnOnClickListener,NuocUongAdapter.NuocUongOnClickListener{
    var sum_money : Long = 0

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

    fun initData(thucDons: MutableList<ThucDonResponse>){
        val monAnAdapter = MonAnAdapter(this,thucDons,MonAnAdapter.TYPE_ORDER,this)
        recycler_menu_monan.adapter = monAnAdapter
    }

    fun callApi(){
        foodyRepository.getThucDons("MATHUCDON1",object : FoodyDataSource.DataCallBack<MutableList<ThucDonResponse>>{
            override fun onSuccess(datas: MutableList<ThucDonResponse>) {
                initData(datas)
            }

            override fun onFailure(message: String) {

            }
        })
    }

    fun calculatorAllItem(money : Long){
        sum_money += money
        val formatter = DecimalFormat("#,###")
        val formattedNumber = formatter.format(sum_money)
        sum_value.text = "$formattedNumber VND"
    }

    override fun nuocUongCalculatorMoney(money: Long) {
        calculatorAllItem(money)
    }

    override fun monAnCalculatorMoney(money: Long) {
        calculatorAllItem(money)
    }
}