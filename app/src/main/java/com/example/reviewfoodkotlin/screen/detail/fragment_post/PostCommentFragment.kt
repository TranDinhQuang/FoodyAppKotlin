package com.example.reviewfoodkotlin.screen.detail.fragment_post

import android.Manifest
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.reviewfoodkotlin.AppSharedPreference
import com.example.reviewfoodkotlin.BuildConfig
import com.example.reviewfoodkotlin.R
import com.example.reviewfoodkotlin.common.BaseFragment
import com.example.reviewfoodkotlin.data.models.BinhLuan
import com.example.reviewfoodkotlin.data.models.QuanAn
import com.example.reviewfoodkotlin.data.repository.FoodyRepository
import com.example.reviewfoodkotlin.screen.adapter.PicturePostAdapter
import com.example.reviewfoodkotlin.screen.detail.DetailEatingActivity
import com.example.reviewfoodkotlin.screen.detail.DetailViewModel
import com.example.reviewfoodkotlin.view.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_post_comment.*
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class PostCommentFragment : BaseFragment(), PostCommentInterface.View,
    PicturePostAdapter.OnClickListener {

    private var quanAn: QuanAn? = null
    var binhLuan: BinhLuan? = null

    private lateinit var photoURI: Uri
    private lateinit var mAdapter: PicturePostAdapter
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var listImagePost: MutableList<String>

    @Inject
    lateinit var mActivity: DetailEatingActivity

    @Inject
    lateinit var repository: FoodyRepository

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    @Inject
    lateinit var presenter: PostCommentInterface.Presenter

    companion object {
        val ADD_COMMENT = 0
        val EDIT_COMMENT = 1

        val REQUEST_TAKE_PHOTO = 101
        val REQUEST_GALLERY_PHOTO = 102

        var permissions =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        fun newInstance(): Fragment {
            val postCommentFragment = PostCommentFragment()
            return postCommentFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_comment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        mActivity.showActionBack(View.OnClickListener { mActivity.popFragment() })
    }

    override fun removeImage(position: Int) {
        listImagePost.removeAt(position)
    }

    private fun init() {
        listImagePost = ArrayList()
        recycler_picture_post.layoutManager = GridLayoutManager(activityContext, 3)
        val itemDecoration = ItemOffsetDecoration(activityContext, R.dimen.dp_2)
        recycler_picture_post.addItemDecoration(itemDecoration)
        recycler_picture_post.setHasFixedSize(true)

        detailViewModel = activity?.run {
            ViewModelProviders.of(this).get(DetailViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        quanAn = detailViewModel.quanan.value!!
        if (quanAn != null) {
            txt_restaurent_name.text = quanAn!!.tenquanan
            txt_restaurent_address.text = quanAn!!.diachi
        }

        mAdapter = PicturePostAdapter(activityContext, this)
        recycler_picture_post.adapter = mAdapter
        open_camera.setOnClickListener {
            if (checkPermission()) {
                val file = newFile()
                if (file == null) {
                    Toast.makeText(context, "Có lỗi xảy ra", Toast.LENGTH_SHORT)
                } else {
                    startCamera(file)
                }
            } else {
                showPermissionDialog()
            }
        }
        open_library.setOnClickListener {
            if (checkPermission()) {
                chooseGallery()
            } else {
                showPermissionDialog()
            }
        }

        txtPostComment.setOnClickListener {
            postComment()
        }
    }

    private fun postComment() {
        if (txtTitleComment.text.toString().trim() == "" || txtContentComment.text.toString().trim() == "" || (ratingBar.rating.equals(
                0F
            ))
        ) {
            showAlertMessage("Thiếu thông tin", "Bạn cần chọn và nhập đầy đủ các thông tin")
        } else {
            var binhLuan = BinhLuan()
            binhLuan.id_user = appSharedPreference.getToken()!!
            binhLuan.ten_user = appSharedPreference.getUser().tenhienthi
            binhLuan.hinhanh_user = appSharedPreference.getUser().hinhanh
            binhLuan.user = appSharedPreference.getUser().taikhoan
            binhLuan.tieude = txtTitleComment.text.toString()
            binhLuan.noidung = txtContentComment.text.toString()
            binhLuan.chamdiem = ratingBar.rating
            if (!listImagePost.isNullOrEmpty()) {
                var i = 1
                val map = listImagePost.associate {
                    "hinhanh${i++}" to it
                }
                binhLuan.hinhanh.putAll(map)
            }
            presenter.postCommentToServer(this, quanAn!!, binhLuan)
            txtPostComment.isEnabled = false
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {

            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                val selectedImage = data!!.data
                val mPhotoPath = getRealPathFromUri(selectedImage)
                listImagePost.add(mPhotoPath)
                mAdapter.setImagePost(mPhotoPath)
            }
        }
    }

    fun getRealPathFromUri(contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = run { MediaStore.Images.Media.DATA }
            cursor = mActivity.contentResolver.query(contentUri, arrayOf(proj), null, null, null)
            assert(cursor != null)
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        } finally {
            cursor?.close()
        }
    }

    private fun newFile(): File? {
        var cal = Calendar.getInstance()
        var timeInMollis = cal.timeInMillis
        val mFileName = "$timeInMollis.jpeg"
        val mFilePath = getFilePath()
        try {
            var newFile = File(mFilePath.absolutePath, mFileName)
            newFile.createNewFile()
            return newFile
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getFilePath(): File {
        return mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    }


    private fun checkPermission(): Boolean {
        for (mPermission in permissions) {
            val result = ActivityCompat.checkSelfPermission(activityContext, mPermission)
            if (result == PackageManager.PERMISSION_DENIED) return false
        }
        return true
    }

    private fun startCamera(file: File) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(mActivity.packageManager) != null) {
            if (file != null) {
                photoURI =
                    FileProvider.getUriForFile(
                        activityContext,
                        BuildConfig.APPLICATION_ID + ".provider",
                        file
                    )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    private fun chooseGallery() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO)
    }

    override fun postCommentFailure() {
        txtPostComment.isEnabled = true
        progressBar.visibility = View.GONE
        showAlertMessage(
            "Có lỗi",
            "Xảy ra lỗi khi gửi dữ liệu tới Server, vui lòng kiểm tra kết nối và thử lại"
        )
    }

    override fun postCommentSuccess() {
        showAlertListernerOneclick(
            "Thành công",
            "Bình luận của bạn đã được gửi đến cho chúng tôi,cảm ơn bản đã đóng góp",
            "Xem bình luận",
            DialogInterface.OnClickListener { p0, p1 ->
                mActivity.popFragment()
            })
    }

    override fun removeImage(link: String) {
    }

    override fun showAlertFailure(type: Int) {
    }
}
