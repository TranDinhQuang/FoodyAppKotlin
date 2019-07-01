package com.example.foodyappkotlin.data.source.remote

import android.util.Log
import com.example.foodyappkotlin.data.models.*
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.google.firebase.database.*
import com.google.gson.Gson
import javax.inject.Singleton

@Singleton
class FoodyRemoteDataSource : FoodyDataSource.Remote {
    var nodeRoot: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun getThucDons(maThucDon: String, callback: FoodyDataSource.DataCallBack<ThucDon>) {
        var thucdons = ThucDon(ArrayList(), ArrayList())

        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val dataSnapshot = p0.child("thucdons").child(maThucDon)
                for (item in dataSnapshot.child("MONAN").children) {
                    var monAn = item.getValue(MonAn::class.java)
                    if (monAn != null) {
                        thucdons.monAns.add(monAn)
                    }
                }
                for (item in dataSnapshot.child("NUOCUONG").children) {
                    var nuocUong = item.getValue(NuocUong::class.java)
                    if (nuocUong != null) {
                        thucdons.nuocUongs.add(nuocUong)
                    }
                }
                callback.onSuccess(thucdons)
            }

            override fun onCancelled(p0: DatabaseError) {
                callback.onFailure(p0.message)
            }
        }
        nodeRoot.addValueEventListener(postListener)
    }

    override fun getHinhAnhBinhLuan(callBack: FoodyDataSource.DataCallBack<List<String>>) {

    }

    override fun getQuanAns(callback: FoodyDataSource.DataCallBack<List<QuanAn>>){
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
                var gson = Gson()
                Log.d("data",gson.toJson(quanans))
                callback.onSuccess(quanans)
            }

            override fun onCancelled(p0: DatabaseError) {
                callback.onFailure(p0.message)
            }
        }
        nodeRoot.addValueEventListener(postListener)
    }
}
