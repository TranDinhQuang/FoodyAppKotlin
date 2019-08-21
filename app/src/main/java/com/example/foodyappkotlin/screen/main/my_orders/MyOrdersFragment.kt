package com.example.foodyappkotlin.screen.main.my_orders

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodyappkotlin.AppSharedPreference
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseFragment
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.screen.adapter.MyOrdersAdapter
import com.example.foodyappkotlin.screen.main.MainActivity
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
    }

    fun getThucDons() {
        val refThucDon =
            nodeRoot.child("thanhviens").child(appSharedPreference.getToken()!!).child("donhang")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        p0.children.forEach {
                            if (it.hasChildren()) {

                            }
                        }
                    }
                })
    }

    fun initView() {
     /*   mAdapter = MyOrdersAdapter(activityContext,)
        recycler_orders.adapter*/
    }
}