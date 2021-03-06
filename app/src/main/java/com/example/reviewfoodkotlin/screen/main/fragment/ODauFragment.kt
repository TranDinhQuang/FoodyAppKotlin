package com.example.reviewfoodkotlin.screen.main.fragment

import android.content.DialogInterface
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.reviewfoodkotlin.AppSharedPreference
import com.example.reviewfoodkotlin.R
import com.example.reviewfoodkotlin.data.models.QuanAn
import com.example.reviewfoodkotlin.data.repository.FoodyRepository
import com.example.reviewfoodkotlin.data.request.QuanAnRequest
import com.example.reviewfoodkotlin.data.source.remote.FoodyRemoteDataSource
import com.example.reviewfoodkotlin.screen.adapter.OdauAdapter
import com.example.reviewfoodkotlin.screen.detail.DetailEatingActivity
import com.example.reviewfoodkotlin.screen.main.MainActivity
import com.example.reviewfoodkotlin.screen.main.myorders.MyOrdersFragment
import com.example.reviewfoodkotlin.screen.menu.MenuActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_odau.*
import kotlinx.android.synthetic.main.fragment_odau.view.*
import javax.inject.Inject

class ODauFragment : Fragment(), ODauInterface.View, OdauAdapter.OnClickListener,
    AdapterView.OnItemSelectedListener {
    var nodeRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var mLocationPermissionGranted: Boolean = false
    var location: Location? = null

    private lateinit var lOdauAdapter: OdauAdapter
    private lateinit var mView: View
    private lateinit var mODauPresenter: ODauPresenter
    private lateinit var mQuanans: MutableList<QuanAn>
    private lateinit var quanAnRequest: QuanAnRequest

    var list_of_items = arrayOf("Mới nhất", "Cũ nhất", "Gần tôi")
    var list_of_items_khuvuc = arrayOf("Hà Nội", "TP.Hồ Chí Minh")

    private var isLoading: Boolean = false

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    @Inject
    lateinit var foodyRepository: FoodyRepository

    @Inject
    lateinit var mActivity: MainActivity

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        settingToolbar()
    }

    fun settingToolbar() {
        mActivity.actionbarBack.visibility = View.GONE
        mActivity.actionbarRight.visibility = View.VISIBLE
        mActivity.actionbarRight.setImageResource(R.drawable.ic_orders)
        mActivity.actionbarRight.setOnClickListener {
            mActivity.pushFragment(R.id.frame_layout, MyOrdersFragment.newInstance())
        }
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

        spinner_fillter_khuvuc!!.onItemSelectedListener = this
        val adapterSpinnerKhuVuc =
            ArrayAdapter(activity, android.R.layout.simple_spinner_item, list_of_items_khuvuc)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_fillter_khuvuc!!.adapter = adapterSpinnerKhuVuc
        spinner_fillter_khuvuc.setSelection(0, true)
        spinner_fillter_khuvuc.isSelected = false

        progressBar.visibility = View.VISIBLE

        mQuanans = ArrayList()
        mODauPresenter = ODauPresenter(foodyRepository, this)

        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mView.recycler_quan_an.layoutManager = linearLayoutManager
        lOdauAdapter = OdauAdapter(
            ArrayList(),
            appSharedPreference.getLocation(),
            appSharedPreference.getUser().permission,
            this
        )
        mView.recycler_quan_an.adapter = lOdauAdapter
        setOnItemSelected()
        swipe_refresh.setOnRefreshListener {
            mODauPresenter.getQuanAns(quanAnRequest)
        }
    }

    fun setOnItemSelected() {
        spinner_fillter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> {
                        quanAnRequest.page = 1
                        quanAnRequest.typeCall = FoodyRemoteDataSource.SORT_BY_DATE_DESC
                        mODauPresenter.getQuanAns(quanAnRequest)
                    }
                    1 -> {
                        quanAnRequest.page = 1
                        quanAnRequest.typeCall = FoodyRemoteDataSource.SORT_BY_DATE_ASC
                        mODauPresenter.getQuanAns(quanAnRequest)
                    }
                    2 -> {
                        quanAnRequest.page = 1
                        quanAnRequest.typeCall = FoodyRemoteDataSource.SORT_NEAR_ME
                        mODauPresenter.getQuanAns(quanAnRequest)
                    }
                }
            }
        }

        spinner_fillter_khuvuc.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    when (p2) {
                        0 -> {
                            quanAnRequest.idKhuVuc = "KV1"
                            mODauPresenter.getQuanAns(quanAnRequest)
                        }
                        1 -> {
                            quanAnRequest.idKhuVuc = "KV2"
                            mODauPresenter.getQuanAns(quanAnRequest)
                        }
                    }
                }

            }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun QuanAnsFailure(msg: String) {
        swipe_refresh.isRefreshing = false
        isLoading = false
        progressBar.visibility = View.GONE
    }

    override fun QuanAnsSuccess(quanans: MutableList<QuanAn>) {
        isLoading = false
        swipe_refresh.isRefreshing = false
        progressBar.visibility = View.GONE
        if (quanAnRequest.typeCall == FoodyRemoteDataSource.SORT_NEAR_ME) {
            val quanAnFilter = ArrayList<QuanAn>()
            quanans.forEach {
                if (distance(it.latitude, it.longitude, appSharedPreference.getLocation()) < 5.0) {
                    quanAnFilter.add(it)
                }
            }
            mQuanans.addAll(quanAnFilter)
            lOdauAdapter.addAllItem(quanAnFilter)
        } else {
            mQuanans.addAll(quanans)
            lOdauAdapter.addAllItem(quanans)
        }
    }

    override fun onItemClickListener(quanAn: QuanAn) {
        val intentDetailEating = DetailEatingActivity.newInstance(context!!, quanAn)
        startActivity(intentDetailEating)
    }

    override fun startActivityMenu(quanAn: QuanAn) {
        startActivity(MenuActivity.newInstance(context!!, quanAn))
    }

    private fun distance(lat1: Double, long1: Double, location: Location): Double {
        val loc1 = Location("")
        loc1.latitude = lat1
        loc1.longitude = long1

        val loc2 = Location("")
        loc2.latitude = location.latitude
        loc2.longitude = location.longitude
        val distance = Math.round(loc1.distanceTo(loc2) / 1000 * 100) / 100.0
        return distance
    }

    override fun onStop() {
        super.onStop()
        mODauPresenter.removeListernerQuanAn()
    }

    override fun deleteRestaurant(quanAn: QuanAn) {
        mActivity.showAlertListerner("Thông báo!", "Bạn có chắc chắn muốn xoá quán ăn này?",
            DialogInterface.OnClickListener { p0, p1 ->
                nodeRoot.child("quanans").child("KV${quanAn.id_khuvuc}").child(quanAn.id)
                    .removeValue().addOnSuccessListener {
                        nodeRoot.child("thucdons").child(quanAn.thucdon).removeValue()
                    }.addOnFailureListener {
                        mActivity.showAlertMessage(
                            "Thất bại!",
                            "Có lỗi xảy ra trong quá trình xoá quán ăn, vui lòng kiểm tra các kết nối mạng và thử lại"
                        )
                    }
            })
    }
}
