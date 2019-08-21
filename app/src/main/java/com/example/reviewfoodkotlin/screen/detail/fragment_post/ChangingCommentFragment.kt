package com.example.reviewfoodkotlin.screen.detail.fragment_post

import android.Manifest
import android.app.Activity
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
import com.example.reviewfoodkotlin.screen.adapter.PicturePostBinhLuanAdapter
import com.example.reviewfoodkotlin.screen.detail.DetailEatingActivity
import com.example.reviewfoodkotlin.screen.detail.DetailViewModel
import com.example.reviewfoodkotlin.view.ItemOffsetDecoration
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_post_comment.*
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ChangingCommentFragment : BaseFragment(),
    PicturePostBinhLuanAdapter.OnClickListener {
    var nodeRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
    val storage = FirebaseStorage.getInstance().reference

    private var quanAn: QuanAn? = null

    lateinit var binhLuan: BinhLuan
    private lateinit var photoURI: Uri
    private lateinit var mAdapter: PicturePostBinhLuanAdapter
    private lateinit var detailViewModel: DetailViewModel

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

        fun newInstance(binhLuan: BinhLuan): Fragment {
            val changingCommentFragment = ChangingCommentFragment()
            changingCommentFragment.binhLuan = binhLuan
            return changingCommentFragment
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
    }

    private fun init() {
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

        mAdapter = PicturePostBinhLuanAdapter(activityContext, this)
        recycler_picture_post.adapter = mAdapter
        setValueInit()

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


    fun setValueInit() {
        txtTitleComment.setText(binhLuan.tieude)
        txtContentComment.setText(binhLuan.noidung)
        ratingBar.rating = binhLuan.chamdiem
        val listImageBinhLuan = ArrayList<String>(binhLuan.hinhanh.values)
        mAdapter.setAllImagePost(listImageBinhLuan)
    }

    private fun postComment() {
        if (txtTitleComment.text.toString().trim() == "" || txtContentComment.text.toString().trim() == "" || (ratingBar.rating.equals(
                0F
            ))
        ) {
            showAlertMessage("Thiếu thông tin", "Bạn cần chọn và nhập đầy đủ các thông tin")
        } else {
            binhLuan.id_user = appSharedPreference.getToken()!!
            binhLuan.ten_user = appSharedPreference.getUser().tenhienthi
            binhLuan.hinhanh_user = appSharedPreference.getUser().hinhanh
            binhLuan.user = appSharedPreference.getUser().taikhoan
            binhLuan.tieude = txtTitleComment.text.toString()
            binhLuan.noidung = txtContentComment.text.toString()
            binhLuan.chamdiem = ratingBar.rating
            txtPostComment.isEnabled = false
            progressBar.visibility = View.VISIBLE
            editCommentToDataBase(quanAn!!, binhLuan)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {

            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                val selectedImage = data!!.data
                val mPhotoPath = getRealPathFromUri(selectedImage)
                uploadImageFile(mPhotoPath)
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

    fun editCommentToDataBase(quanAn: QuanAn, binhLuan: BinhLuan) {
        var dataRef =
            nodeRoot.child("quanans").child("KV${quanAn.id_khuvuc}").child(quanAn.id)
                .child("binhluans").child(binhLuan.id)
        var numHinhAnh = 1
        if (mAdapter.imgsFile.size > 0) {
            binhLuan.hinhanh.clear()
            mAdapter.imgsFile.forEach {
                binhLuan.hinhanh["hinhanh$numHinhAnh"] = it
                numHinhAnh += 1
            }
        }
        dataRef.setValue(binhLuan).addOnSuccessListener {
            showAlertMessage("Thành công!", "Bạn đã thay đổi bình luận thành công")
            mActivity.popFragment()
        }.addOnFailureListener {
            progressBar.visibility = View.GONE
            print(it.message)
            showAlertMessage(
                "Có lỗi!",
                "Có lỗi kết nối đến hệ thống,vui lòng kiểm tra lại các kết nối và thử lại"
            )
        }
    }

    private fun uploadImageFile(url: String) {
        var file = Uri.fromFile(File(url))
        var storageRef: StorageReference = storage.child("binhluan/${file.lastPathSegment}")
        var uploadTask = storageRef.putFile(file)

        uploadTask.addOnFailureListener {
            showAlertMessage("Có lỗi", "Tải ảnh quán ăn lên hệ thống thất bại,vui lòng thử lại")
        }.addOnSuccessListener {
            mAdapter.setImagePost(file.lastPathSegment)
        }
    }

    private fun deleteImage(url: String, position: Int) {
        var storageRef: StorageReference = storage.child("binhluan/$url")
        storageRef.delete().addOnSuccessListener {
            mAdapter.imgsFile.removeAt(position)
            mAdapter.notifyDataSetChanged()
            showAlertMessage("Thông báo", "Xoá hình ảnh thành công")
        }.addOnFailureListener {
            showAlertMessage(
                "Có lỗi",
                "Xoá hình ảnh thất bại, vui lòng kiểm tra lại kết nối và thử lại"
            )
        }
    }


    override fun removeImage(link: String, position: Int) {
        showAlertListerner("Thông báo", "Bạn có chắc chắn muốn xoá hình ảnh này?",
            DialogInterface.OnClickListener { _, _ -> deleteImage(link, position) })
    }
}