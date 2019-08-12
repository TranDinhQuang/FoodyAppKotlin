package com.example.foodyappkotlin.screen.detail.fragment_detail_comment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.foodyappkotlin.AppSharedPreference
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseFragment
import com.example.foodyappkotlin.data.models.BinhLuan
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.models.ThaoLuan
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.response.ThongSoResponse
import com.example.foodyappkotlin.di.module.GlideApp
import com.example.foodyappkotlin.screen.adapter.ThaoLuanAdapter
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_detail_comment.*
import kotlinx.android.synthetic.main.multiimage_layout.*
import javax.inject.Inject

class FragmentDetailComment : BaseFragment(), DetailCommentInterface.View, View.OnClickListener {

    var nodeRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
    val storage = FirebaseStorage.getInstance().reference
    var isLiked = false
    lateinit var binhLuan: BinhLuan
    lateinit var mQuanAn: QuanAn
    lateinit var mPresenter: DetailCommentPresenter
    lateinit var thaoLuanAdapter: ThaoLuanAdapter
    lateinit var storageRef: StorageReference
    lateinit var refThongSo: Query
    lateinit var listernerThongSo: ChildEventListener
    var thongSo = ThongSoResponse()

    @Inject
    lateinit var repository: FoodyRepository
    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    companion object {
        fun newInstance(quanAn: QuanAn, binhLuan: BinhLuan): Fragment {
            val fragmentDetailComment = FragmentDetailComment()
            fragmentDetailComment.mQuanAn = quanAn
            fragmentDetailComment.binhLuan = binhLuan
            return fragmentDetailComment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_comment, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.layout_like -> {
                isLiked = !isLiked
                if (isLiked) {
                    appSharedPreference.getUser().liked[binhLuan.id] = binhLuan.id
                    img_like.setImageResource(R.drawable.ic_like_red)
                    thongSo.num_like += 1
                    nodeRoot.child("thongsobinhluans").child(mQuanAn.id).child(binhLuan.id)
                        .child("num_like").setValue(thongSo.num_like).addOnCompleteListener {
                        }
                } else {
                    appSharedPreference.getUser().liked.values.remove(binhLuan.id)
                    img_like.setImageResource(R.drawable.ic_like)
                    if (thongSo.num_like > 0) {
                        thongSo.num_like -= 1
                    }
                    nodeRoot.child("thongsobinhluans").child(mQuanAn.id).child(binhLuan.id)
                        .child("num_like")
                        .setValue(thongSo.num_like)
                }
                nodeRoot.child("thanhviens").child(appSharedPreference.getToken()!!).child("liked")
                    .setValue(appSharedPreference.getUser().liked)
            }

            R.id.layout_comment -> {
                layout_post_comment.visibility = View.VISIBLE
                layout_post_comment.requestFocus()

            }
            R.id.layout_share -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "${binhLuan.ten_user} đã bình luận về ${mQuanAn.tenquanan} - ${mQuanAn.diachi} với nội dung: ${binhLuan.noidung}"
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }
            R.id.btn_post -> {
                if (edt_comment.text.trim().toString() != "") {
                    val thaoLuan = ThaoLuan()
                    thaoLuan.username = appSharedPreference.getUser().tenhienthi
                    thaoLuan.taikhoan = appSharedPreference.getUser().taikhoan
                    thaoLuan.hinhanh = appSharedPreference.getUser().hinhanh
                    thaoLuan.noidung = edt_comment.text.trim().toString()
                    val refThaoLuan =
                        nodeRoot.child("binhluans").child(mQuanAn.id).child(binhLuan.id).push()
                    thaoLuan.id_thaoluan = refThaoLuan.key!!
                    refThaoLuan.setValue(thaoLuan)
                        .addOnCompleteListener {
                            edt_comment.setText("")
                            hideKeyboard()
                            layout_post_comment.visibility = View.GONE
                            thongSo.num_comment += 1
                            nodeRoot.child("thongsobinhluans").child(mQuanAn.id).child(binhLuan.id)
                                .child("num_comment")
                                .setValue(thongSo.num_comment)
                        }
                } else {
                    showAlertMessage("Có lỗi", "Không được để trống comment của bạn!")
                }
            }
            R.id.txt_delete_comment -> {
            }
        }
    }

    private fun initData() {
        getThongSoBinhLuan()
        mPresenter = DetailCommentPresenter(repository)
        thaoLuanAdapter = ThaoLuanAdapter(
            activityContext,
            ArrayList(),
            appSharedPreference.getUser().taikhoan,
            mQuanAn.id,
            binhLuan.id,
            nodeRoot
        )
        recycler_thao_luan.adapter = thaoLuanAdapter
        getThaoLuanIntoComment(mQuanAn.id, binhLuan.id)
        text_title.text = binhLuan.tieude
        text_content_comment.text = binhLuan.noidung
        text_title.text = binhLuan.tieude
        glideLoadImage(image_avatar_user, binhLuan.hinhanh_user)
        text_name_user.text = binhLuan.ten_user

        if (binhLuan.hinhanh.isNotEmpty()) {
            val imageUrl: ArrayList<String> = ArrayList(binhLuan.hinhanh.values)
            loadImage(imageUrl)
        }
        appSharedPreference.getUser().liked.filterValues {
            it == binhLuan.id
        }.mapNotNull {
            img_like.setImageResource(R.drawable.ic_like_red)
            isLiked = true
        }

        if (binhLuan.id_user == appSharedPreference.getToken()) {
            layout_function.visibility = View.VISIBLE
        } else {
            layout_function.visibility = View.GONE
        }

        layout_like.setOnClickListener(this)
        layout_comment.setOnClickListener(this)
        layout_share.setOnClickListener(this)
        btn_post.setOnClickListener(this)
        txt_delete_comment.setOnClickListener(this)
    }

    fun getThongSoBinhLuan() {
        refThongSo =
            nodeRoot.child("thongsobinhluans").child(mQuanAn.id).orderByKey().equalTo(binhLuan.id)
        listernerThongSo = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                thongSo = p0.getValue(ThongSoResponse::class.java)!!
                txt_num_like_comment!!.text = "${thongSo.num_like} like"
                txt_num_comment!!.text = "${thongSo.num_comment} comment"
                txt_num_share!!.text = "${thongSo.num_share} share"
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                thongSo = p0.getValue(ThongSoResponse::class.java)!!
                txt_num_like_comment!!.text = "${thongSo.num_like} like"
                txt_num_comment!!.text = "${thongSo.num_comment} comment"
                txt_num_share!!.text = "${thongSo.num_share} share"
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                thongSo = p0.getValue(ThongSoResponse::class.java)!!
                txt_num_like_comment.text = "${thongSo?.num_like} like"
                txt_num_comment.text = "${thongSo?.num_comment} comment"
                txt_num_share.text = "${thongSo?.num_share} share"
            }

        }
        refThongSo.addChildEventListener(listernerThongSo)
    }

    fun getThaoLuanIntoComment(
        idQuanan: String,
        idBinhLuan: String
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
                    getThaoLuanSuccess(thaoluan)
                } else {
                    getThaoLuanFailure("Không có dữ liệu")
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                if (thongSo.num_comment > 0) {
                    thongSo.num_comment -= 1
                }
                nodeRoot.child("thongsobinhluans").child(mQuanAn.id).child(binhLuan.id)
                    .child("num_comment")
                    .setValue(thongSo.num_comment)
                val thaoLuan = p0.getValue(ThaoLuan::class.java)
                thaoLuanAdapter.removeThaoLuan(thaoLuan!!)
            }
        })
    }

    override fun getThaoLuanSuccess(data: ThaoLuan) {
        thaoLuanAdapter.setThaoLuan(data)
    }

    override fun getThaoLuanFailure(message: String) {
        Log.d("kiemtra", "ThaoLuanFailure $message")
    }

    private fun loadImage(hinhanhs: ArrayList<String>) {
        when (hinhanhs.size) {
            1 -> {
                group_one_picture.visibility = View.VISIBLE
                storageRef = storage.child("binhluan").child(hinhanhs[0])
                glideLoadImage(img_one_one, storageRef)
            }
            2 -> {
                group_two_picture.visibility = View.VISIBLE
                storageRef = storage.child("binhluan").child(hinhanhs[0])
                glideLoadImage(img_two_one, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[1])
                glideLoadImage(img_two_two, storageRef)
            }
            3 -> {
                group_three_picture.visibility = View.VISIBLE
                storageRef = storage.child("binhluan").child(hinhanhs[0])
                glideLoadImage(img_three_one, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[1])
                glideLoadImage(img_three_two, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[2])
                glideLoadImage(img_three_three, storageRef)

            }
            4 -> {
                group_four_picture.visibility = View.VISIBLE
                storageRef = storage.child("binhluan").child(hinhanhs[0])
                glideLoadImage(img_four_one, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[1])
                glideLoadImage(img_four_two, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[2])
                glideLoadImage(img_four_three, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[3])
                glideLoadImage(img_four_four, storageRef)
            }
            else -> {
                group_four_picture.visibility = View.VISIBLE
                storageRef = storage.child("binhluan").child(hinhanhs[0])
                glideLoadImage(img_four_one, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[1])
                glideLoadImage(img_four_two, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[2])
                glideLoadImage(img_four_three, storageRef)
                storageRef = storage.child("binhluan").child(hinhanhs[3])
                glideLoadImage(img_four_four, storageRef)
                img_transparent.visibility = View.VISIBLE
                text_number.visibility = View.VISIBLE
            }
        }
    }

    private fun glideLoadImage(img: ImageView, url: StorageReference) {
        GlideApp.with(activityContext)
            .load(url)
            .error(R.drawable.placeholder)
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(img)
    }

    private fun glideLoadImage(img: ImageView, url: String) {
        GlideApp.with(activityContext)
            .load(url)
            .error(R.drawable.placeholder)
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(img)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        refThongSo.removeEventListener(listernerThongSo)
    }
}