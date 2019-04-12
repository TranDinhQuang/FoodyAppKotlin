package com.example.foodyappkotlin.data.source.remote

import android.util.Log
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.google.firebase.database.*
import javax.inject.Singleton

@Singleton
class FoodyRemoteDataSource : FoodyDataSource.Remote {

    var nodeRoot: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun getQuanAns(callback: FoodyDataSource.DataCallBack<List<QuanAn>>) {
        var quanans: ArrayList<QuanAn> = ArrayList()
        val hinhanhquanans: ArrayList<String> = ArrayList()

        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val dataSnapshotQuanAn = p0.child("quanans")
                for (item in dataSnapshotQuanAn.children) {
                    var quanan = item.getValue(QuanAn::class.java)
                    if (quanan != null) {
                        quanan.id = item.key.toString()
                        val dataSnapshotHinhAnh = p0.child("hinhanhquanans").child(quanan.id)
                        Log.d("remote", quanan.id)
                        for (item in dataSnapshotHinhAnh.children) {
                            hinhanhquanans.add(item.value as String)
                        }
                        quanan.hinhanhquanans.addAll(hinhanhquanans)
                        quanans.add(quanan)
                        hinhanhquanans.clear()
                    }
                }
                callback.onSuccess(quanans)
            }

            override fun onCancelled(p0: DatabaseError) {
                callback.onFailure(p0.message)
            }
        }
        nodeRoot.addValueEventListener(postListener)
    }
}
