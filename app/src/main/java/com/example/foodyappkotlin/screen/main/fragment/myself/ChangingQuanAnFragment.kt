package com.example.foodyappkotlin.screen.main.fragment.myself

import android.Manifest
import android.app.Activity
import android.app.TimePickerDialog
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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.foodyappkotlin.AppSharedPreference
import com.example.foodyappkotlin.BuildConfig
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseFragment
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.screen.adapter.MonAnAdapter
import com.example.foodyappkotlin.screen.adapter.PicturePostAdapter
import com.example.foodyappkotlin.screen.main.MainActivity
import com.example.foodyappkotlin.screen.maps.MapsActivity
import com.example.foodyappkotlin.util.DateUtils
import com.example.foodyappkotlin.view.ItemOffsetDecoration
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_changing_quanan.*
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ChangingQuanAnFragment : BaseFragment(), AdapterView.OnItemSelectedListener,
    PicturePostAdapter.OnClickListener, View.OnClickListener, MonAnAdapter.MonAnOnClickListener {
    var nodeRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
    val storage = FirebaseStorage.getInstance().reference
    var list_of_items = arrayOf("Hà Nội", "Hồ Chí Minh")

    var listImageUpload = ArrayList<String>()
    var listImageDelete = ArrayList<String>()

    var numHinhAnhQuanAn = 0
    var mLatitude = 21.008513
    var mLongitude = 105.846314

    private lateinit var mAdapterImages: PicturePostAdapter
    private lateinit var photoURI: Uri
    lateinit var quanAn: QuanAn

    @Inject
    lateinit var mActivity: MainActivity

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    companion object {
        val REQUEST_TAKE_PHOTO = 101
        val REQUEST_GALLERY_PHOTO = 102
        val REQUEST_GALLERY_PHOTO_MONAN = 105
        val GET_LOCATION = 103

        var permissions =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        fun newInstance(quanAn: QuanAn): Fragment {
            val changingQuanAnFragment = ChangingQuanAnFragment()
            changingQuanAnFragment.quanAn = quanAn
            return changingQuanAnFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_changing_quanan, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        mActivity.showActionBack(View.OnClickListener { mActivity.popFragment() })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ChangingQuanAnFragment.REQUEST_TAKE_PHOTO -> {
                }
                ChangingQuanAnFragment.REQUEST_GALLERY_PHOTO -> {
                    val selectedImage = data!!.data
                    val mPhotoPath = getRealPathFromUri(selectedImage)
                    listImageUpload.add(mPhotoPath)
                    uploadImageShowQuanAn(mPhotoPath)
                }
                ChangingQuanAnFragment.GET_LOCATION -> {
                    val result = data!!.getStringExtra("result")
                    mLatitude = data.getDoubleExtra("latitude", 0.0)
                    mLongitude = data.getDoubleExtra("longitude", 0.0)
                    edt_dia_chi.setText(result)
                }

            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.edt_dia_chi -> {
            }
            R.id.img_open_google_maps -> {
                startActivityForResult(MapsActivity.newInstance(activityContext), GET_LOCATION)
            }
            R.id.layout_take_photo -> {
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
            R.id.layout_open_library -> {
                if (checkPermission()) {
                    chooseGallery()
                } else {
                    showPermissionDialog()
                }
            }
            R.id.btn_changing_quan_an -> {
                changingQuanAnCuaToi()
            }
        }
    }

    private fun initData() {
        recycler_image_quan_an.layoutManager = GridLayoutManager(activityContext, 3)
        val itemDecoration = ItemOffsetDecoration(activityContext, R.dimen.dp_2)
        recycler_image_quan_an.addItemDecoration(itemDecoration)
        recycler_image_quan_an.setHasFixedSize(true)
        mAdapterImages = PicturePostAdapter(activityContext, this)
        recycler_image_quan_an.adapter = mAdapterImages

        spiner_khuvuc!!.onItemSelectedListener = this
        val adapterSpinner =
            ArrayAdapter(activity, android.R.layout.simple_spinner_item, list_of_items)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spiner_khuvuc!!.adapter = adapterSpinner
        setOnClickListerner()
        if (::quanAn.isInitialized) {
            setEditQuanAn()
        }
    }

    private fun setOnClickListerner() {
        edt_dia_chi.setOnClickListener(this)
        layout_take_photo.setOnClickListener(this)
        layout_open_library.setOnClickListener(this)
        btn_changing_quan_an.setOnClickListener(this)
        img_open_google_maps.setOnClickListener(this)
        edt_time_open.setOnClickListener {
            hideKeyboard()
            var mcurrentTime = Calendar.getInstance()
            var hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            var minute = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker = TimePickerDialog(
                activityContext,
                TimePickerDialog.OnTimeSetListener { p0, p1, p2 -> edt_time_open.setText("$p1:$p2") },
                hour,
                minute,
                false
            )
            mTimePicker.show()
        }
        edt_time_close.setOnClickListener {
            hideKeyboard()
            var mcurrentTime = Calendar.getInstance()
            var hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            var minute = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker = TimePickerDialog(
                activityContext,
                TimePickerDialog.OnTimeSetListener { p0, p1, p2 -> edt_time_close.setText("$p1:$p2") },
                hour,
                minute,
                false
            )
            mTimePicker.show()
        }
    }

    fun setEditQuanAn() {
        edt_ten_quan_an.setText(quanAn.tenquanan)
        edt_dia_chi.setText(quanAn.diachi)
        switch_giao_hang.isChecked = quanAn.giaohang
        edt_time_open.setText("${quanAn.giomocua / 60}:${quanAn.giomocua % 60}")
        edt_time_close.setText("${quanAn.giodongcua / 60}:${quanAn.giodongcua % 60}")
        mLatitude = quanAn.latitude
        mLongitude = quanAn.longitude
        spiner_khuvuc.setSelection(quanAn.id_khuvuc - 1)

        val hinhAnhsQuanAn = ArrayList<String>(quanAn.hinhanhs.values)
        hinhAnhsQuanAn.forEach { _ ->
            numHinhAnhQuanAn += 1
        }
        mAdapterImages.setAllImagePost(hinhAnhsQuanAn)
    }

    fun changingQuanAnCuaToi() {
        if (edt_ten_quan_an.text.toString().trim() != "" && edt_dia_chi.text.toString().trim() != ""
            && edt_time_open.text.toString().trim() != "" && edt_time_close.text.toString().trim() != ""
            && mAdapterImages.imgsFile.size > 0
        ) {
            progressBar.visibility = View.VISIBLE
            imageUploadAndDelete()
            quanAn.nguoidang = appSharedPreference.getUser().taikhoan
            quanAn.tenquanan = edt_ten_quan_an.text.toString().trim()
            quanAn.diachi = edt_dia_chi.text.toString().trim()
            quanAn.latitude = mLatitude
            quanAn.longitude = mLongitude
            quanAn.giomocua = convertStringToTime(edt_time_open.text.toString().trim())
            quanAn.giodongcua = convertStringToTime(edt_time_close.text.toString().trim())
            quanAn.ngaytao = DateUtils.getSecondsCurrentTime()
            quanAn.giaohang = switch_giao_hang.isChecked

            var keyHinhAnh = 0
            quanAn.hinhanhs.clear()
            mAdapterImages.imgsFile.forEach {
                quanAn.hinhanhs["hinhanh$keyHinhAnh"] = it
                keyHinhAnh += 1
            }

            if (spiner_khuvuc.selectedItemPosition == 0) {
                quanAn.id_khuvuc = 1
            } else {
                quanAn.id_khuvuc = 2
            }

            pushQuanAnToDataBase(quanAn)
        } else {
            showAlertMessage(
                "Thiếu giá trị",
                "Cần nhập đầy đủ các thông tin và hình ảnh,thực đơn cho quán ăn,Nhập lại?"
            )
        }
    }

    fun imageUploadAndDelete() {
        if (listImageDelete.isNotEmpty()) {
            listImageDelete.forEach {
                deleteImageFileQuanAn(it)
            }
        }
        if (listImageUpload.isNotEmpty()) {
            listImageUpload.forEach {
                uploadImageFileQuanAn(it)
            }
        }
    }

    private fun deleteImageFileQuanAn(url: String) {
        var storageRef: StorageReference = storage.child("monan/$url")
        storageRef.delete().addOnSuccessListener {
        }.addOnFailureListener {
            Log.d("deleteImage",it.message)
        }
    }


    fun convertStringToTime(timeString: String): Long {
        val time = timeString.split(":")
        val hours = time[0].trim()
        val minutes = time[1].trim()
        val timeLong = (hours.toLong() * 60) + minutes.toLong()
        return timeLong
    }

    fun pushQuanAnToDataBase(quanAn: QuanAn) {
        if (quanAn.id_khuvuc == 1) {
            quanAn.hinhanhs
            val refQuanAn = nodeRoot.child("quanans").child("KV1").child(quanAn.id)
            refQuanAn.setValue(quanAn).addOnCompleteListener {
                showAlertListernerOneclick(
                    "Thành công",
                    "Quán ăn của bạn đã được chúng tôi ghi nhận trên hệ thống",
                    "Đóng",
                    DialogInterface.OnClickListener { _, _ ->
                        mActivity.popFragment()
                    })
            }.addOnFailureListener {
                progressBar.visibility = View.GONE
                showAlertMessage(
                    "Có lỗi xảy ra",
                    "Vui lòng kiểm tra lại các kết nối 3G/4G/WIFI và thử lại"
                )
            }
        } else {
            val refQuanAn = nodeRoot.child("quanans").child("KV2").child(quanAn.id)
            refQuanAn.setValue(quanAn).addOnCompleteListener {
                showAlertListernerOneclick(
                    "Thành công",
                    "Quán ăn của bạn đã được chúng tôi ghi nhận trên hệ thống",
                    "Đóng",
                    DialogInterface.OnClickListener { _, _ ->
                        mActivity.popFragment()
                    })
            }.addOnFailureListener {
                showAlertMessage(
                    "Có lỗi xảy ra",
                    "Vui lòng kiểm tra lại các kết nối 3G/4G/WIFI và thử lại"
                )
            }
        }
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
                startActivityForResult(takePictureIntent, PostQuanAnFragment.REQUEST_TAKE_PHOTO)
            }
        }
    }


    private fun chooseGallery() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(pickPhoto, PostQuanAnFragment.REQUEST_GALLERY_PHOTO)
    }

    private fun uploadImageShowQuanAn(url: String) {
        progressBar.visibility = View.VISIBLE
        var file = Uri.fromFile(File(url))
        var storageRef: StorageReference = storage.child("monan/${file.lastPathSegment}")
        var uploadTask = storageRef.putFile(file)

        uploadTask.addOnFailureListener {
            progressBar.visibility = View.GONE
            showAlertMessage("Có lỗi", "Tải ảnh quán ăn lên hệ thống thất bại,vui lòng thử lại")
        }.addOnSuccessListener {
            progressBar.visibility = View.GONE
            mAdapterImages.setImagePost(file.lastPathSegment)
        }
    }

    private fun uploadImageFileQuanAn(url: String) {
        var file = Uri.fromFile(File(url))
        var storageRef: StorageReference = storage.child("monan/${file.lastPathSegment}")
        var uploadTask = storageRef.putFile(file)

        uploadTask.addOnFailureListener {
            showAlertMessage("Có lỗi", "Tải ảnh quán ăn lên hệ thống thất bại,vui lòng thử lại")
        }.addOnSuccessListener {
            mAdapterImages.setImagePost(file.lastPathSegment)
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

    private fun checkPermission(): Boolean {
        for (mPermission in PostQuanAnFragment.permissions) {
            val result = ActivityCompat.checkSelfPermission(activityContext, mPermission)
            if (result == PackageManager.PERMISSION_DENIED) return false
        }
        return true
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }

    override fun removeImage(position: Int) {
    }

    override fun monAnCalculatorMoney(money: Long) {

    }

    override fun removeImage(link: String) {
        listImageDelete.add(link)
    }

    override fun showAlertFailure(type: Int) {
        if (type == PicturePostAdapter.LIST_ONE_ELEMENT) {
            showAlertMessage("Có lỗi!", "Bạn không được phép xoá hết ảnh của quán ăn")
        } else if (type == PicturePostAdapter.CONNECT_FAILURE) {
            showAlertMessage(
                "Có lỗi!",
                "Có lỗi khi liên kết đến hệ thống,vui lòng kiểm tra lại kết nối và thử lại"
            )
        }
    }

}