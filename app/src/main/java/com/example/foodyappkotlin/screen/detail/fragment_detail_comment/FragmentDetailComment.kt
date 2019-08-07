package com.example.foodyappkotlin.screen.detail.fragment_detail_comment

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
import com.example.foodyappkotlin.data.response.ThaoLuanResponse
import com.example.foodyappkotlin.di.module.GlideApp
import com.example.foodyappkotlin.screen.adapter.ThaoLuanAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_detail_comment.*
import kotlinx.android.synthetic.main.multiimage_layout.*
import javax.inject.Inject

class FragmentDetailComment : BaseFragment(), DetailCommentInterface.View, View.OnClickListener {


    var isLiked = false

    lateinit var binhLuan: BinhLuan

    private lateinit var mQuanAn: QuanAn

    lateinit var mPresenter: DetailCommentPresenter

    lateinit var thaoLuanAdapter: ThaoLuanAdapter

    val storage = FirebaseStorage.getInstance().reference

    lateinit var storageRef: StorageReference

    @Inject
    lateinit var repository: FoodyRepository

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    var nodeRoot: DatabaseReference = FirebaseDatabase.getInstance().reference

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.layout_like -> {
                isLiked = !isLiked
                if (isLiked) {
                    appSharedPreference.getUser().liked.put(binhLuan.id, binhLuan.id)
                    img_like.setImageResource(R.drawable.ic_like_red)
                    binhLuan.num_like += 1
                   /* nodeRoot.child("quanans").child("KV${mQuanAn.id_khuvuc}").child(mQuanAn.id)
                        .child("binhluans").child(binhLuan.id).child("num_comment")
                        .setValue(binhLuan.num_like)*/
                } else {
                    appSharedPreference.getUser().liked.values.remove(binhLuan.id)
                    img_like.setImageResource(R.drawable.ic_like)
                    binhLuan.num_like -= 1
                /*    nodeRoot.child("quanans").child("KV${mQuanAn.id_khuvuc}").child(mQuanAn.id)
                        .child("binhluans").child(binhLuan.id).child("num_comment")
                        .setValue(binhLuan.num_like)*/
                }
                nodeRoot.child("thanhviens").child(appSharedPreference.getToken()!!).child("liked")
                    .setValue(appSharedPreference.getUser().liked)
                txt_num_like.text = "${binhLuan.num_like} like"
            }

            R.id.layout_comment -> {
                layout_post_comment.visibility = View.VISIBLE

            }
            R.id.layout_share -> {

            }
            R.id.btn_post -> {
                if (edt_comment.text.trim().toString() != "") {
                    val thaoLuan = ThaoLuan()
                    thaoLuan.username = appSharedPreference.getUser().tenhienthi
                    thaoLuan.taikhoan = appSharedPreference.getUser().taikhoan
                    thaoLuan.hinhanh = appSharedPreference.getUser().hinhanh
                    thaoLuan.noidung = edt_comment.text.trim().toString()
                    nodeRoot.child("binhluans").child(mQuanAn.id).child(binhLuan.id).push().setValue(thaoLuan)
                        .addOnCompleteListener {
                            binhLuan.num_comment += 1
                       /*     nodeRoot.child("quanans").child("KV${mQuanAn.id_khuvuc}")
                                .child(mQuanAn.id)
                                .child("binhluans").child(binhLuan.id).child("num_comment")
                                .setValue(binhLuan.num_comment)*/
                            txt_num_comment.text = "${binhLuan.num_share} share"
                            layout_post_comment.visibility = View.GONE
                        }
                } else {
                    showAlertMessage("Có lỗi", "Không được để trống comment của bạn!")
                }
            }
        }
    }

    private fun initData() {
        mPresenter = DetailCommentPresenter(repository)
        thaoLuanAdapter = ThaoLuanAdapter(activityContext, ArrayList())
        recycler_thao_luan.adapter = thaoLuanAdapter
        mPresenter.getThaoLuan(mQuanAn.id, binhLuan.id, this)

        text_title.text = binhLuan.tieude
        text_content_comment.text = binhLuan.noidung
        text_title.text = binhLuan.tieude
        txt_num_like.text = "${binhLuan.num_like} like"
        txt_num_comment.text = "${binhLuan.num_comment} comment"
        txt_num_share.text = "${binhLuan.num_share} share"
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
        layout_like.setOnClickListener(this)
        layout_comment.setOnClickListener(this)
        layout_share.setOnClickListener(this)
        btn_post.setOnClickListener(this)
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

}