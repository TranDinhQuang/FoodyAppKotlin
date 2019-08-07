package com.example.foodyappkotlin.data.source.remote

import android.location.Location
import android.net.Uri
import android.util.Log
import com.example.foodyappkotlin.AppSharedPreference
import com.example.foodyappkotlin.data.models.BinhLuan
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.models.ThaoLuan
import com.example.foodyappkotlin.data.models.User
import com.example.foodyappkotlin.data.request.QuanAnRequest
import com.example.foodyappkotlin.data.response.ThucDonResponse
import com.example.foodyappkotlin.data.response.UserResponse
import com.example.foodyappkotlin.data.source.FoodyDataSource
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FoodyRemoteDataSource : FoodyDataSource.Remote {

    var nodeRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
    val storage = FirebaseStorage.getInstance().reference

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    companion object {
        val SORT_BY_KEY_DESC = 1
        val SORT_BY_KEY_ASC = 2
        val SORT_BY_DATE_DESC = 3
        val SORT_BY_DATE_ASC = 4
        val FILLTER_BY_NAME = 5
        val FILLTER_BY_ADDRESS = 6
        val SORT_NEAR_ME = 7
    }

    override fun getUser(
        userId: String,
        callBack: FoodyDataSource.DataCallBack<UserResponse>
    ) {
        Log.d("kiemtra", "$userId id_user")
        val ref = nodeRoot.child("thanhviens").child(userId)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val userResponse = p0.getValue(UserResponse::class.java)
                if (userResponse != null) {
                    callBack.onSuccess(userResponse)
                }
                ref.removeEventListener(this)
                /* if (p0.hasChildren()) {
                     val userResponse = p0.children.elementAt(0).getValue(UserResponse::class.java)
                     if (userResponse != null) {
                         callBack.onSuccess(userResponse)
                     }
                     ref.removeEventListener(this)
                 }*/
            }

        })
    }

    override fun getThaoLuanIntoComment(
        idQuanan: String,
        idBinhLuan: String,
        callback: FoodyDataSource.DataCallBack<ThaoLuan>
    ) {
        val refThaoLuan = nodeRoot.child("binhluans").child(idQuanan).child(idBinhLuan)
        refThaoLuan.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val thaoluan = p0.getValue(ThaoLuan::class.java)
                if (thaoluan != null) {
                    callback.onSuccess(thaoluan)
                } else {
                    callback.onFailure("Không có dữ liệu")
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
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
                } else {
                    callback.onFailure("Không có dữ liệu")
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
                nodeRoot.child("quanans").child("KV${quanAn.id_khuvuc}").child(quanAn.id)
                    .child("num_comments").setValue(num_comment)
                nodeRoot.child("quanans").child("KV${quanAn.id_khuvuc}").child(quanAn.id)
                    .child("num_images").setValue(num_image)
                callBack.onSuccess("Data saved successfully!")
            }
        }
    }

    //Cần sửa lại
    override fun getThucDons(
        maThucDon: String,
        callback: FoodyDataSource.DataCallBack<MutableList<ThucDonResponse>>
    ) {
        val thucDonRef = nodeRoot.child("thucdons").child(maThucDon)
        var thucdons: MutableList<ThucDonResponse> = ArrayList()

        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (item in p0.children) {
                    var item = item.getValue(ThucDonResponse::class.java)
                    if (item != null) {
                        thucdons.add(item)
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

    override fun getQuanAnsFollowId(
        quanAnRequest: QuanAnRequest,
        callback: FoodyDataSource.DataCallBack<QuanAn>
    ) {
        var refQuanAn =
            nodeRoot.child("quanans").child(quanAnRequest.idKhuVuc).child(quanAnRequest.idQuanAn)
        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var quanan = p0.getValue(QuanAn::class.java)
                var gson = Gson()
                Log.d("data", gson.toJson(quanan))
                if (quanan != null) {
                    callback.onSuccess(quanan)
                }
                refQuanAn.removeEventListener(this)
            }

            override fun onCancelled(p0: DatabaseError) {
                callback.onFailure(p0.message)
            }
        }
        refQuanAn.addValueEventListener(postListener)
    }

    override fun getQuanAns(
        quanAnRequest: QuanAnRequest,
        callback: FoodyDataSource.DataCallBack<MutableList<QuanAn>>
    ) {
        val quanans: ArrayList<QuanAn> = ArrayList()
        /*    var refQuanAn = if (quanAnRequest.page == 1) {
                nodeRoot.child("quanans").child(quanAnRequest.idKhuVuc).orderByChild("ngaytao")
            } else {
                nodeRoot.child("quanans").child(quanAnRequest.idKhuVuc).orderByChild("ngaytao").startAt(quanAnRequest.valueAt)
                    .limitToFirst(11)
            }*/
        var refQuanAn =
            nodeRoot.child("quanans").child(quanAnRequest.idKhuVuc).orderByChild("ngaytao")
                .limitToFirst(100)

        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                when (quanAnRequest.typeCall) {
                    FoodyRemoteDataSource.SORT_BY_DATE_ASC -> {
                        p0.children.forEach {
                            var quanan = it.getValue(QuanAn::class.java)
                            if (quanan != null) {
                                quanans.add(quanan)
                            }
                        }
                    }
                    FoodyRemoteDataSource.SORT_BY_DATE_DESC -> {
                        p0.children.reversed().forEach {
                            var quanan = it.getValue(QuanAn::class.java)
                            if (quanan != null) {
                                quanans.add(quanan)
                            }
                        }
                    }
                    FoodyRemoteDataSource.SORT_NEAR_ME -> {
                        p0.children.forEach {
                            var quanan = it.getValue(QuanAn::class.java)
                            if (quanan != null) {
                                quanans.add(quanan)
                            }
                        }
                    }
                }
                var gson = Gson()
                Log.d("data", gson.toJson(quanans))
                /*   if (quanAnRequest.page != 1) {
                       quanans.removeAt(0)
                   }*/
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
            Log.d("kiemtra", "Tải ảnh thất bại")
        }.addOnSuccessListener {
            Log.d("kiemtra", "tải ảnh thành công")
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
        }
    }

    override fun searchQuanAn(
        idKhuVuc: String,
        textSearch: String,
        type: Int,
        callback: FoodyDataSource.DataCallBack<MutableList<QuanAn>>
    ) {
        val quanans: ArrayList<QuanAn> = ArrayList()

        val refSearch = nodeRoot.child("quanans").child(idKhuVuc)
        refSearch.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                refSearch.removeEventListener(this)
                callback.onFailure(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach {
                    val quanAn = it.getValue(QuanAn::class.java)
                    if (quanAn != null && quanAn.tenquanan.toLowerCase().contains(textSearch.trim().toLowerCase())) {
                        quanans.add(quanAn)
                    }
                }
                callback.onSuccess(quanans)
                refSearch.removeEventListener(this)
            }
        })
    }
}
