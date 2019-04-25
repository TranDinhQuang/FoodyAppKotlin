package com.example.foodyappkotlin.data.source.remote

import com.example.foodyappkotlin.data.models.BinhLuan
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
        var binhluans = ArrayList<BinhLuan>()

        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val dataSnapshotQuanAn = p0.child("quanans")
                for (item in dataSnapshotQuanAn.children) {
                    var quanan = item.getValue(QuanAn::class.java)
                    if (quanan != null) {
                        quanan.id = item.key.toString()
                        val dataSnapshotHinhAnh = p0.child("hinhanhquanans").child(quanan.id)
                        val dataSnapshotBinhLuan = p0.child("binhluans").child(quanan.id)

                        for (itemhinhanh in dataSnapshotHinhAnh.children) {
                            hinhanhquanans.add(itemhinhanh.value as String)
                        }

                        for (itembinhluan in dataSnapshotBinhLuan.children) {
                            binhluans.add(itembinhluan.getValue(BinhLuan::class.java)!!)
                        }

                        quanan.hinhanhquanans.addAll(hinhanhquanans)
                        quanan.binhluans.addAll(binhluans)
                        quanans.add(quanan)
                        hinhanhquanans.clear()
                        binhluans.clear()
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
