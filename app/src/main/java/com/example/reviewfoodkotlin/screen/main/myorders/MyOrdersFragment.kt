package com.example.reviewfoodkotlin.screen.main.myorders

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.reviewfoodkotlin.AppSharedPreference
import com.example.reviewfoodkotlin.R
import com.example.reviewfoodkotlin.common.BaseFragment
import com.example.reviewfoodkotlin.data.repository.FoodyRepository
import com.example.reviewfoodkotlin.data.response.MyOrdersResponse
import com.example.reviewfoodkotlin.screen.adapter.MyOrdersAdapter
import com.example.reviewfoodkotlin.screen.main.MainActivity
import com.google.firebase.database.*
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_my_orders.*
import javax.inject.Inject

class MyOrdersFragment : BaseFragment() {
    var nodeRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
    lateinit var mAdapter: MyOrdersAdapter

    @Inject
    lateinit var foodyRepository: FoodyRepository

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    @Inject
    lateinit var mActivity: MainActivity

    companion object {
        fun newInstance(): Fragment {
            val myOrdersFragment = MyOrdersFragment()
            return myOrdersFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_my_orders, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity.showActionBack(View.OnClickListener { mActivity.popFragment() })
        getThucDons()
    }

    fun getThucDons() {
        var monAns: MutableList<MyOrdersResponse> = ArrayList()
        var refThucDon =
            nodeRoot.child("thanhviens").child(appSharedPreference.getToken()!!).child("donhang")
        refThucDon.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    if (it.hasChildren()) {
                        monAns.add(it.getValue(MyOrdersResponse::class.java)!!)
                    }
                    initView(monAns)
                }
                refThucDon.removeEventListener(this)
            }
        })
    }

    fun initView(orders: MutableList<MyOrdersResponse>) {
        mAdapter = MyOrdersAdapter(activityContext, orders)
        recycler_orders.adapter = mAdapter
    }
}