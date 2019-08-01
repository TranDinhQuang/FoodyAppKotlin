package com.example.foodyappkotlin.data.source.remote

import android.net.Uri
import android.util.Log
import com.example.foodyappkotlin.data.models.*
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import java.io.File
import javax.inject.Singleton


@Singleton
class FoodyRemoteDataSource : FoodyDataSource.Remote {
    var nodeRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
    val storage = FirebaseStorage.getInstance().reference

    @Synchronized override fun writeCommentToDataBase(idQuanAn: String, binhluan: BinhLuan, callBack: FoodyDataSource.DataCallBack<String>) {
        var dataRef = nodeRoot.child("binhluans").child(idQuanAn)
        var hinhanhSuccess: HashMap<String, String> = HashMap()
        binhluan.hinhanh.forEach {
            uploadImageFile(it.value)
            var file = Uri.fromFile(File(it.value))
            hinhanhSuccess.put(it.key,file.lastPathSegment)
        }
        binhluan.hinhanh = hinhanhSuccess
        dataRef.push().setValue(binhluan) { databaseError, databaseReference ->
            if (databaseError != null) {
                callBack.onFailure("Data could not be saved " + databaseError.message)
            } else {
                callBack.onSuccess("Data saved successfully!")
            }
        }
    }

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
                nodeRoot.removeEventListener(this)
            }

            override fun onCancelled(p0: DatabaseError) {
                callback.onFailure(p0.message)
            }
        }
        nodeRoot.addValueEventListener(postListener)
    }

    override fun getHinhAnhBinhLuan(callBack: FoodyDataSource.DataCallBack<List<String>>) {

    }

    override fun getQuanAns(
            province: Int,
            page: Int,
            callback: FoodyDataSource.DataCallBack<List<QuanAn>>
    ) {
        var quanans: ArrayList<QuanAn> = ArrayList()
        val hinhanhquanans: ArrayList<String> = ArrayList()
        var binhluans = ArrayList<BinhLuan>()

        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val dataSnapshotQuanAn = p0.child("quanans").child("KV$province").child("PAGE$page")
                for (item in dataSnapshotQuanAn.children) {
                    var quanan = item.getValue(QuanAn::class.java)
                    if (quanan != null) {
                        quanan.id = item.key!!
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
                Log.d("data", gson.toJson(quanans))
                callback.onSuccess(quanans)
                nodeRoot.removeEventListener(this)
            }

            override fun onCancelled(p0: DatabaseError) {
                callback.onFailure(p0.message)
            }
        }
        nodeRoot.addValueEventListener(postListener)
    }

    @Synchronized private fun uploadImageFile(url: String) {
        var file = Uri.fromFile(File(url))
        var storageRef: StorageReference = storage.child("binhluan/${file.lastPathSegment}")
        var uploadTask = storageRef.putFile(file)
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.d("kiemtra", "tai anh that bai")
        }.addOnSuccessListener {
            Log.d("kiemtra", "tai anh thanh cong")
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
        }
    }
}
