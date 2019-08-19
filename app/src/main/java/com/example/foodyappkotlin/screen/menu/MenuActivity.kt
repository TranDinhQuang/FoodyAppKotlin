package com.example.foodyappkotlin.screen.menu

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseActivity
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.response.ThucDonResponse
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.example.foodyappkotlin.screen.adapter.MonAnAdapter
import com.example.foodyappkotlin.screen.adapter.NuocUongAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_toolbar.*
import java.text.DecimalFormat
import javax.inject.Inject


class MenuActivity : BaseActivity(), MonAnAdapter.MonAnOnClickListener,
    NuocUongAdapter.NuocUongOnClickListener {
    var sum_money: Long = 0
    var idThucDon: String = ""

    @Inject
    lateinit var foodyRepository: FoodyRepository


    companion object {
        fun newInstance(context: Context, idThucDon: String): Intent {
            return Intent(context, MenuActivity::class.java).apply {
                putExtra(
                    "IDTHUCDON", idThucDon
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        AndroidInjection.inject(this)
        idThucDon = intent.getStringExtra("IDTHUCDON")!!
        callApi()
    }

    fun initData(thucDons: MutableList<ThucDonResponse>) {
        val monAnAdapter = MonAnAdapter(this, thucDons, MonAnAdapter.TYPE_ORDER, this)
        recycler_menu_monan.adapter = monAnAdapter
        button_order.setOnClickListener {
            showAlertListernerOneClick(
                "Thông báo!",
                "Chúng tôi đã ghi nhận đơn hàng của bạn, đơn vị vận chuyển của chúng tôi sẽ liên hệ với bạn, xin cảm ơn!",
                "Ok",
                DialogInterface.OnClickListener { p0, p1 -> finish() })
        }
        actionbarBack.visibility = View.VISIBLE
        actionbarBack.setOnClickListener {
            finish()
        }
    }

    fun callApi() {
        foodyRepository.getThucDons(idThucDon,
            object : FoodyDataSource.DataCallBack<MutableList<ThucDonResponse>> {
                override fun onSuccess(datas: MutableList<ThucDonResponse>) {
                    initData(datas)
                }

                override fun onFailure(message: String) {
                }
            })
    }

    fun calculatorAllItem(money: Long) {
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