package com.example.foodyappkotlin.screen.main.fragment.myself

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.foodyappkotlin.AppSharedPreference
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseFragment
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.screen.adapter.RestaurentMyselfAdapter
import com.example.foodyappkotlin.screen.detail.DetailEatingActivity
import com.example.foodyappkotlin.screen.main.MainActivity
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_my_restaurent.*
import javax.inject.Inject

class QuanAnCuaToiFragment : BaseFragment(), AdapterView.OnItemSelectedListener,
    RestaurentMyselfAdapter.OnClickItemListerner {

    var nodeRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
    lateinit var refQuanAn: Query
    lateinit var mListernerQuanAn: ChildEventListener
    val storage = FirebaseStorage.getInstance().reference
    var list_of_items = arrayOf("Hà Nội", "TP.Hồ Chí Minh")
    private var mIdKhuVuc = ""

    @Inject
    lateinit var mActivity: MainActivity

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    @Inject
    lateinit var repository: FoodyRepository


    lateinit var mPresenter: QuanAnCuaToiPresenter
    lateinit var mAdapterRestaurent: RestaurentMyselfAdapter

    companion object {
        fun newInstance(): Fragment {
            val quanAnCuaToiFragment = QuanAnCuaToiFragment()
            return quanAnCuaToiFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_my_restaurent, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        mActivity.actionbarBack.visibility = View.GONE
    }

    private fun initData() {
        spinner_khuvuc!!.onItemSelectedListener = this
        val adapterSpinner =
            ArrayAdapter(activity, android.R.layout.simple_spinner_item, list_of_items)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_khuvuc!!.adapter = adapterSpinner

        mPresenter = QuanAnCuaToiPresenter(repository)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recycler_restaurent_myself.layoutManager = linearLayoutManager
        mAdapterRestaurent = RestaurentMyselfAdapter(activityContext, ArrayList(), this)
        recycler_restaurent_myself.adapter = mAdapterRestaurent
        btn_add.setOnClickListener {
            mActivity.pushFragment(R.id.frame_layout, PostQuanAnFragment.newInstance())
        }
/*
        swiperefresh.setOnRefreshListener {
            mAdapterRestaurent.quanAns.clear()
            getQuanAnFllowNguoiDang()
        }*/
    }

    fun getQuanAnFllowNguoiDang() {
        refQuanAn = nodeRoot.child("quanans").child(mIdKhuVuc).orderByChild("nguoidang")
            .equalTo(appSharedPreference.getUser().taikhoan)
        mListernerQuanAn = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
         /*       if(swiperefresh.isRefreshing){
                    swiperefresh.isRefreshing = false
                }*/
                val quanAn = p0.getValue(QuanAn::class.java)
                mAdapterRestaurent.addQuanAn(quanAn!!)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        }
        refQuanAn.addChildEventListener(mListernerQuanAn)
    }

    override fun onStop() {
        super.onStop()
        refQuanAn.removeEventListener(mListernerQuanAn)
    }

    fun getQuanAnSuccess(data: QuanAn) {
        mAdapterRestaurent.addQuanAn(data)
    }

    fun getQuanAnFailure(message: String) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p2) {
            0 -> {
                mIdKhuVuc = "KV1"
                mAdapterRestaurent.quanAns.clear()
                getQuanAnFllowNguoiDang()
            }
            1 -> {
                mIdKhuVuc = "KV2"
                mAdapterRestaurent.quanAns.clear()
                getQuanAnFllowNguoiDang()
            }
            else -> {
            }
        }
    }

    override fun openOverViewFragment(quanan: QuanAn) {
        val intentDetailEating = DetailEatingActivity.newInstance(context!!, quanan)
        startActivity(intentDetailEating)
    }

    override fun editQuanAn(quanan: QuanAn) {
        mActivity.pushFragment(R.id.frame_layout, ChangingQuanAnFragment.newInstance(quanan))
    }

    override fun deleteQuanAn(quanan: QuanAn) {
        showAlertListerner("Thông báo!","Bạn có chắc chắn muốn xoá quán ăn này?",
            DialogInterface.OnClickListener { p0, p1 -> nodeRoot.child("quanans").child("KV${quanan.id_khuvuc}").child(quanan.id).removeValue() })
    }
}