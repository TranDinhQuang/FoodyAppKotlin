package com.example.reviewfoodkotlin.screen.menu

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import com.example.reviewfoodkotlin.AppSharedPreference
import com.example.reviewfoodkotlin.R
import com.example.reviewfoodkotlin.common.BaseActivity
import com.example.reviewfoodkotlin.data.models.QuanAn
import com.example.reviewfoodkotlin.data.repository.FoodyRepository
import com.example.reviewfoodkotlin.data.response.MyOrdersResponse
import com.example.reviewfoodkotlin.data.response.ThucDonResponse
import com.example.reviewfoodkotlin.data.source.FoodyDataSource
import com.example.reviewfoodkotlin.screen.adapter.MonAnAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_toolbar.*
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.math.roundToLong


class MenuActivity : BaseActivity(), MonAnAdapter.MonAnOnClickListener {
    private var nodeRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var mQuanAn: QuanAn
    lateinit var monAnAdapter: MonAnAdapter
    private var sum_money: Long = 0
    private var idThucDon: String = ""

    @Inject
    lateinit var foodyRepository: FoodyRepository

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    companion object {
        fun newInstance(context: Context, quanAn: QuanAn): Intent {
            return Intent(context, MenuActivity::class.java).apply {
                putExtra("RESTAURANT", quanAn)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        AndroidInjection.inject(this)
        mQuanAn = intent.getSerializableExtra("RESTAURANT") as QuanAn
        idThucDon = mQuanAn.thucdon
        callApi()
    }

    @SuppressLint("SetTextI18n")
    fun initData(thucDons: MutableList<ThucDonResponse>) {
        monAnAdapter = MonAnAdapter(this, thucDons, MonAnAdapter.TYPE_ORDER, this)
        txt_fee_ship_value.text = "${(distance(
            appSharedPreference.getLocation().latitude,
            appSharedPreference.getLocation().longitude,
            mQuanAn.latitude,
            mQuanAn.longitude
        ).roundToLong() * 5000)} VND"
        if ((distance(
                appSharedPreference.getLocation().latitude,
                appSharedPreference.getLocation().longitude,
                mQuanAn.latitude,
                mQuanAn.longitude
            ).roundToLong() * 5000) < 30000
        ) {
            txt_fee_ship_value.text = "${convertMoneyToStringMoney(30000)} VND"
        } else {
            txt_fee_ship_value.text = "${convertMoneyToStringMoney(
                distance(
                    appSharedPreference.getLocation().latitude,
                    appSharedPreference.getLocation().longitude,
                    mQuanAn.latitude,
                    mQuanAn.longitude
                ).roundToLong() * 5000
            )} VND"
        }
        recycler_menu_monan.adapter = monAnAdapter
        if(mQuanAn.giaohang){
            button_order.visibility = View.VISIBLE
        }else{
            button_order.visibility = View.GONE
        }

        button_order.setOnClickListener {
            if (distance(
                    appSharedPreference.getLocation().latitude,
                    appSharedPreference.getLocation().longitude,
                    mQuanAn.latitude,
                    mQuanAn.longitude
                ).roundToLong() > 20
            ) {
                showAlertListernerOneClick(
                    "Thông báo!",
                    "Khoảng cách của bạn là quá lớn, khoảng cách giao hàng tối đa là : 20km",
                    "Thoát",
                    DialogInterface.OnClickListener { p0, p1 -> finish() })
            }else{
                setValueOrdersToServer()
            }
        }
        actionbarBack.visibility = View.VISIBLE
        actionbarBack.setOnClickListener {
            finish()
        }
    }

    fun setValueOrdersToServer() {
        val monAnOrder = monAnAdapter.monAns.filter { it.soluong > 0 }
        var request = MyOrdersResponse()
        var number_monan = 1

        request.monans = monAnOrder.associate {
            "monan${number_monan++}" to it
        }
        if (request.monans.isNotEmpty()) {
            request.tenquanan = mQuanAn.tenquanan
            request.diadiem = mQuanAn.diachi
            request.phiship = (distance(
                appSharedPreference.getLocation().latitude,
                appSharedPreference.getLocation().longitude,
                mQuanAn.latitude,
                mQuanAn.longitude
            ).roundToLong() * 5000)
            if (request.phiship < 30000) {
                request.phiship = 30000
            }
            request.thanhtien = request.phiship + sum_money
            val refDonHang = nodeRoot.child("thanhviens").child(appSharedPreference.getToken()!!)
                .child("donhang").push()
            request.id = refDonHang.key!!
            refDonHang.setValue(request)
            showAlertListernerOneClick(
                "Thông báo!",
                "Chúng tôi đã ghi nhận đơn hàng của bạn, đơn vị vận chuyển của chúng tôi sẽ liên hệ với bạn, xin cảm ơn!",
                "Ok",
                DialogInterface.OnClickListener { p0, p1 -> finish() })
        } else {
            showAlertMessage(
                "Có lỗi!", "Bạn phải đặt tối thiểu 1 mặt hàng"
            )
        }
    }

    fun distance(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
        val loc1 = Location("")
        loc1.latitude = lat1
        loc1.longitude = long1

        val loc2 = Location("")
        loc2.latitude = lat2
        loc2.longitude = long2
        val distance = Math.round(loc1.distanceTo(loc2) / 1000 * 100) / 100.0
        return distance
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

    fun convertMoneyToStringMoney(money: Long): String {
        val formatter = DecimalFormat("#,###")
        val formattedNumber = formatter.format(money)
        return formattedNumber
    }

    fun calculatorAllItem(money: Long) {
        sum_money += money
        val formatter = DecimalFormat("#,###")
        val formattedNumber = formatter.format(sum_money)
        sum_value.text = "$formattedNumber VND"
    }

    override fun monAnCalculatorMoney(money: Long) {
        calculatorAllItem(money)
    }
}