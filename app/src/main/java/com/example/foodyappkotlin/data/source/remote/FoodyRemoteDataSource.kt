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

    override fun saveUserLoginData(user: User, callBack: FoodyDataSource.DataCallBack<User>) {
        val ref = nodeRoot.child("thanhviens").orderByChild("taikhoan").equalTo(user.taikhoan)
        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    var userLogin = User(p0.key!!,user.taikhoan,user.tenhienthi,user.hinhanh,user.permission)
                    callBack.onSuccess(userLogin)
                } else {
                   val refUser = nodeRoot.child("thanhviens").push()
                    refUser.setValue(user).addOnSuccessListener {
                        var userLogin = User(refUser.key!!,user.taikhoan,user.tenhienthi,user.hinhanh,user.permission)
                        callBack.onSuccess(userLogin)
                    }.addOnFailureListener {
                            // Write failed
                            // ...
                        }

                }
                nodeRoot.removeEventListener(this)
            }

            override fun onCancelled(p0: DatabaseError) {
                callBack.onFailure(p0.message)
                Log.d("kiemtra", "co loi xay ra trong qua trinh luu tru tai khoan")
                nodeRoot.removeEventListener(this)
            }
        }
        ref.addListenerForSingleValueEvent(postListener)
    }

    override fun getAllCommentFollowQuanAn(
        idQuanan: String,
        callback: FoodyDataSource.DataCallBack<BinhLuan>
    ) {
        var dataRef = nodeRoot.child("binhluans").child(idQuanan).orderByKey().limitToFirst(1000)
        var binhluans = ArrayList<BinhLuan>()

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val comment = p0.getValue(BinhLuan::class.java)
                if (comment != null) {
                    binhluans.add(comment)
                    callback.onSuccess(comment)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        }
        dataRef.addChildEventListener(childEventListener)
    }

    override fun writeCommentToDataBase(
        quanAn: QuanAn,
        binhluan: BinhLuan,
        callBack: FoodyDataSource.DataCallBack<String>
    ) {
        var dataRef = nodeRoot.child("binhluans").child(quanAn.id)
        var hinhanhSuccess: HashMap<String, String> = HashMap()
        binhluan.hinhanh.forEach {
            uploadImageFile(it.value)
            var file = Uri.fromFile(File(it.value))
            hinhanhSuccess.put(it.key, file.lastPathSegment)
        }
        binhluan.hinhanh = hinhanhSuccess
        dataRef.push().setValue(binhluan) { databaseError, databaseReference ->
            if (databaseError != null) {
                callBack.onFailure("Data could not be saved " + databaseError.message)
            } else {
                var num_comment = quanAn.num_comments + 1
                var num_image = quanAn.num_images + binhluan.hinhanh.size
                nodeRoot.child("quanans").child("KV${quanAn.id_khuvuc}")
                    .child("PAGE${quanAn.id_page}")
                    .child(quanAn.id).child("num_comments").setValue(num_comment)
                nodeRoot.child("quanans").child("KV${quanAn.id_khuvuc}")
                    .child("PAGE${quanAn.id_page}")
                    .child(quanAn.id).child("num_images").setValue(num_image)
                callBack.onSuccess("Data saved successfully!")
            }
        }
    }

    //Cần sửa lại
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
                nodeRoot.removeEventListener(this)
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
        var hinhanhquanans: ArrayList<String> = ArrayList()
        var binhluans = ArrayList<BinhLuan>()
        var thucdons = ThucDon(ArrayList(), ArrayList())


        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val dataSnapshotQuanAn = p0.child("quanans").child("KV$province").child("PAGE$page")
//                dataSnapshotQuanAn.children.sortedByDescending { it.key }.forEach {
                dataSnapshotQuanAn.children.sortedByDescending { it.key }.forEach {
                    var quanan = it.getValue(QuanAn::class.java)
                    if (quanan != null) {
                        quanan.id = it.key!!
                        val dataSnapshotHinhAnh = p0.child("hinhanhquanans").child(quanan.id)
                        val dataSnapshotBinhLuan = p0.child("binhluans").child(quanan.id)
                        if (quanan.thucdon != "") {
                            val dataSnapshotThucDon = p0.child("thucdons").child(quanan.thucdon)
                            for (item in dataSnapshotThucDon.child("MONAN").children) {
                                var monAn = item.getValue(MonAn::class.java)
                                if (monAn != null) {
                                    thucdons.monAns.add(monAn)
                                }
                            }
                            for (item in dataSnapshotThucDon.child("NUOCUONG").children) {
                                var nuocUong = item.getValue(NuocUong::class.java)
                                if (nuocUong != null) {
                                    thucdons.nuocUongs.add(nuocUong)
                                }
                            }
                        }
                        for (itemhinhanh in dataSnapshotHinhAnh.children) {
                            hinhanhquanans.add(itemhinhanh.value as String)
                        }
                        dataSnapshotBinhLuan.children.sortedByDescending { it.key }.forEach {
                            binhluans.add(it.getValue(BinhLuan::class.java)!!)
                        }
                        /*   for (itembinhluan in dataSnapshotBinhLuan.children) {
                               binhluans.add(itembinhluan.getValue(BinhLuan::class.java)!!)
                           }*/
                        quanan.thucdons = thucdons
                        quanan.hinhanhquanans.addAll(hinhanhquanans)
                        quanan.binhluans.addAll(binhluans)
                        quanans.add(quanan)
                        hinhanhquanans.clear()
                        binhluans.clear()
                        thucdons.monAns.clear()
                        thucdons.nuocUongs.clear()
                    }
                }
                /*   for (item in dataSnapshotQuanAn.children.) {
                       var quanan = item.getValue(QuanAn::class.java)
                       if (quanan != null) {
                           quanan.id = item.key!!
                           val dataSnapshotHinhAnh = p0.child("hinhanhquanans").child(quanan.id)
                           val dataSnapshotBinhLuan = p0.child("binhluans").child(quanan.id)
                           *//*for (itemhinhanh in dataSnapshotHinhAnh.children) {
                            hinhanhquanans.add(itemhinhanh.value as String)
                        }*//*
                        for (itembinhluan in dataSnapshotBinhLuan.children) {
                            binhluans.add(itembinhluan.getValue(BinhLuan::class.java)!!)
                        }
                        hinhanhquanans = getHinhAnhQuanAn(quanan.id)
                        
                        quanan.hinhanhquanans.addAll(hinhanhquanans)
                        quanan.binhluans.addAll(binhluans)
                        quanans.add(quanan)
                        hinhanhquanans.clear()
                        binhluans.clear()
                    }
                }*/
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

    private fun uploadImageFile(url: String) {
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

    private fun getHinhAnhQuanAn(idQuanAn: String): ArrayList<String> {
        val dataSnapshotHinhAnhQuanAn = nodeRoot.child("hinhanhquanans").child(idQuanAn)
        val hinhanhquanans: ArrayList<String> = ArrayList()
        val hinhanhListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (item in p0.children) {
                    hinhanhquanans.add(item.value as String)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        dataSnapshotHinhAnhQuanAn.addListenerForSingleValueEvent(hinhanhListener)
        return hinhanhquanans
    }
}
