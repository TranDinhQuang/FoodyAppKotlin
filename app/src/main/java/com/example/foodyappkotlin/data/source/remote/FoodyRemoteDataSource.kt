package com.example.foodyappkotlin.data.source.remote

import android.util.Log
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.google.firebase.database.*
import javax.inject.Singleton

@Singleton
class FoodyRemoteDataSource : FoodyDataSource.Remote {
    var nodeRoot: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun getQuanAns(): List<QuanAn> {
        var quanans: ArrayList<QuanAn> = ArrayList()

        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val dataSnapshotQuanAn = p0.child("quanans")
                for (item in dataSnapshotQuanAn.children) {
                    val quanan = item.getValue(QuanAn::class.java)
                    if (quanan != null) {
                        quanans.add(quanan)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("onCancelled", p0.message)
            }

        }
        nodeRoot.addValueEventListener(postListener)
        return quanans
    }
}
