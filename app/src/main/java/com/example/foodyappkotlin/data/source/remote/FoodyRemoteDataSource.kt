package com.example.foodyappkotlin.data.source.remote

import android.net.Uri
import android.util.Log
import com.example.foodyappkotlin.data.models.*
import com.example.foodyappkotlin.data.response.UserResponse
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

    override fun getListLikedOfUser(
        userId: String,
        callBack: FoodyDataSource.DataCallBack<MutableList<String>>
    ) {
        val ref = nodeRoot.child("thanhviens").child(userId).child("liked")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var listLiked: ArrayList<String> = ArrayList()
                if (p0.hasChildren()) {
                    p0.children.forEach {
                        listLiked.add(it.value as String)
                    }
                    callBack.onSuccess(listLiked)
                    ref.removeEventListener(this)
                }
            }

        })
    }

    override fun addQuanAnMyself(
        idKhuVuc: String,
        quanAn: QuanAn,
        callback: FoodyDataSource.DataCallBack<MutableList<String>>
    ) {
        nodeRoot.child("quanans").child(idKhuVuc).push().setValue(quanAn)
    }

    override fun saveUserLoginData(
        user: User,
        callBack: FoodyDataSource.DataCallBack<UserResponse>
    ) {
        val ref = nodeRoot.child("thanhviens").orderByChild("taikhoan").equalTo(user.taikhoan)
        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    var userLogin = p0.children.elementAt(0).getValue(UserResponse::class.java)
                    if (userLogin != null) {
                        userLogin.userId = p0.children.elementAt(0).key!!
                        callBack.onSuccess(userLogin)
                    }
                } else {
                    val refUser = nodeRoot.child("thanhviens").push()
                    refUser.setValue(user).addOnSuccessListener {
                        var userLogin = UserResponse(
                            refUser.key!!,
                            user.taikhoan,
                            user.tenhienthi,
                            user.hinhanh,
                            HashMap(),
                            user.permission
                        )
                        callBack.onSuccess(userLogin)
                    }.addOnFailureListener {
                        // Write failed
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
        var dataRef =
            nodeRoot.child("quanans").child("KV1").child(idQuanan).child("binhluans").orderByKey()
                .limitToFirst(1000)
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
        var dataRef =
            nodeRoot.child("quanans").child("KV1").child(quanAn.id).child("binhluans").push()
        var hinhanhSuccess: HashMap<String, String> = HashMap()
        binhluan.hinhanh.forEach {
            uploadImageFile(it.value)
            var file = Uri.fromFile(File(it.value))
            hinhanhSuccess.put(it.key, file.lastPathSegment)
        }
        binhluan.id = dataRef.key!!
        binhluan.hinhanh = hinhanhSuccess
        dataRef.setValue(binhluan) { databaseError, databaseReference ->
            if (databaseError != null) {
                callBack.onFailure("Data could not be saved " + databaseError.message)
            } else {
                var num_comment = quanAn.num_comments + 1
                var num_image = quanAn.num_images + binhluan.hinhanh.size
                nodeRoot.child("quanans").child("KV${quanAn.id_khuvuc}").child(quanAn.id).child("num_comments").setValue(num_comment)
                nodeRoot.child("quanans").child("KV${quanAn.id_khuvuc}").child(quanAn.id).child("num_images").setValue(num_image)
                callBack.onSuccess("Data saved successfully!")
            }
        }
    }

    //Cần sửa lại
    override fun getThucDons(maThucDon: String, callback: FoodyDataSource.DataCallBack<ThucDon>) {
        var thucdons = ThucDon(ArrayList(), ArrayList())

        val thucDonRef = nodeRoot.child("thucdons").child(maThucDon)
        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val dataSnapshot = p0.child("MONAN")
                for (item in dataSnapshot.children) {
                    var monAn = item.getValue(MonAn::class.java)
                    if (monAn != null) {
                        thucdons.monAns.add(monAn)
                    }
                }
                for (item in p0.child("NUOCUONG").children) {
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
        thucDonRef.addListenerForSingleValueEvent(postListener)
    }

    override fun getHinhAnhBinhLuan(callBack: FoodyDataSource.DataCallBack<List<String>>) {
    }

/*    override fun getQuanAns(
        province: Int,
        page: Int,
        callback: FoodyDataSource.DataCallBack<MutableList<QuanAn>>
    ) {
        var quanans: ArrayList<QuanAn> = ArrayList()
        var hinhanhquanans: ArrayList<String> = ArrayList()
        var binhluans = ArrayList<BinhLuan>()
        var thucdons = ThucDon(ArrayList(), ArrayList())


        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("kiemtra","$province - $page")
                val dataSnapshotQuanAn = p0.child("quanans").child("KV$province").child("PAGE$page")
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
    }*/

    override fun getQuanAns(
        province: Int,
        page: Int, valueAt: String,
        callback: FoodyDataSource.DataCallBack<MutableList<QuanAn>>
    ) {
        val quanans: ArrayList<QuanAn> = ArrayList()
        Log.d("kiemtra", "page $page")
        var refQuanAn = if (page == 1) {
            nodeRoot.child("quanans").child("KV$province").orderByKey().limitToFirst(10)
        } else {
            nodeRoot.child("quanans").child("KV$province").orderByKey().startAt(valueAt)
                .limitToFirst(11)
        }
        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    var quanan = it.getValue(QuanAn::class.java)
                    if (quanan != null) {
                        quanans.add(quanan)
                    }
                }
                var gson = Gson()
                Log.d("data", gson.toJson(quanans))
                if (page != 1) {
                    quanans.removeAt(0)
                }
                callback.onSuccess(quanans)
                refQuanAn.removeEventListener(this)
            }

            override fun onCancelled(p0: DatabaseError) {
                callback.onFailure(p0.message)
            }
        }
        refQuanAn.addValueEventListener(postListener)
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
