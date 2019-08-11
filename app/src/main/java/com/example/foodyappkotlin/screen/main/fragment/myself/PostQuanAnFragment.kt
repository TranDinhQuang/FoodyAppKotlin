package com.example.foodyappkotlin.screen.main.fragment.myself

import android.Manifest
import android.app.Activity
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.example.foodyappkotlin.AppSharedPreference
import com.example.foodyappkotlin.BuildConfig
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseFragment
import com.example.foodyappkotlin.data.models.QuanAn
import com.example.foodyappkotlin.data.response.ThucDonResponse
import com.example.foodyappkotlin.di.module.GlideApp
import com.example.foodyappkotlin.screen.adapter.MonAnAdapter
import com.example.foodyappkotlin.screen.adapter.PicturePostAdapter
import com.example.foodyappkotlin.screen.main.MainActivity
import com.example.foodyappkotlin.screen.maps.MapsActivity
import com.example.foodyappkotlin.util.DateUtils
import com.example.foodyappkotlin.view.ItemOffsetDecoration
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_add_restaurent.*
import kotlinx.android.synthetic.main.fragment_post_comment.*
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PostQuanAnFragment : BaseFragment(), AdapterView.OnItemSelectedListener,
    PicturePostAdapter.OnClickListener, View.OnClickListener, MonAnAdapter.MonAnOnClickListener {
    var nodeRoot: DatabaseReference = FirebaseDatabase.getInstance().reference
    val storage = FirebaseStorage.getInstance().reference
    var refThucDon = nodeRoot.child("thucdons").push()
    var listernerThucDon: ValueEventListener? = null
    var listernerQuanAn: ValueEventListener? = null
    var list_of_items = arrayOf("Hà Nội", "Hồ Chí Minh")
    var thucDonsRequest = HashMap<String, ThucDonResponse>()
    var thucDon = ThucDonResponse()

    var numHinhAnhQuanAn = 0
    var numThucDon = 0
    var mLatitude = 21.008513
    var mLongitude = 105.846314

    private lateinit var listImagePost: HashMap<String, String>
    private lateinit var mAdapterImages: PicturePostAdapter
    private lateinit var mAdapterMenu: MonAnAdapter
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

        fun newInstance(): Fragment {
            val postQuanAnFragment = PostQuanAnFragment()
            return postQuanAnFragment
        }

        fun newInstance(quanAn: QuanAn): Fragment {
            val postQuanAnFragmentEdit = PostQuanAnFragment()
            postQuanAnFragmentEdit.quanAn = quanAn
            return postQuanAnFragmentEdit
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidSupportInjection.inject(this)
        return inflater.inflate(R.layout.fragment_add_restaurent, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PostQuanAnFragment.REQUEST_TAKE_PHOTO -> {
                }
                PostQuanAnFragment.REQUEST_GALLERY_PHOTO -> {
                    val selectedImage = data!!.data
                    val mPhotoPath = getRealPathFromUri(selectedImage)
                    uploadImageFileQuanAn(mPhotoPath)

                }
                PostQuanAnFragment.REQUEST_GALLERY_PHOTO_MONAN -> {
                    val selectedImage = data!!.data
                    val mPhotoPath = getRealPathFromUri(selectedImage)
                    btn_add_mon_an.isEnabled = false
                    uploadImageFileThucDon(mPhotoPath)

                }
            }
            if (requestCode == GET_LOCATION) {
                val result = data!!.getStringExtra("result")
                mLatitude = data.getDoubleExtra("latitude", 0.0)
                mLongitude = data.getDoubleExtra("longitude", 0.0)
                edt_dia_chi.setText(result)
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
            R.id.btn_add_mon_an -> {
                addMonAn()
            }
            R.id.layout_img_mon_an -> {
                chooseGalleryMonAn()
            }
            R.id.btn_add_quan_an -> {
                addQuanAnCuaToi()
            }
        }
    }

    private fun initData() {
        listImagePost = HashMap()
        recycler_image_quan_an.layoutManager = GridLayoutManager(activityContext, 3)
        val itemDecoration = ItemOffsetDecoration(activityContext, R.dimen.dp_2)
        recycler_image_quan_an.addItemDecoration(itemDecoration)
        recycler_image_quan_an.setHasFixedSize(true)
        mAdapterImages = PicturePostAdapter(activityContext, this)
        recycler_image_quan_an.adapter = mAdapterImages

        mAdapterMenu = MonAnAdapter(activityContext, ArrayList(), MonAnAdapter.TYPE_VIEW, this)
        recycler_menu.adapter = mAdapterMenu

        spiner_khuvuc!!.onItemSelectedListener = this
        val adapterSpinner =
            ArrayAdapter(activity, android.R.layout.simple_spinner_item, list_of_items)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spiner_khuvuc!!.adapter = adapterSpinner
        setOnClickListerner()
    }

    private fun setOnClickListerner() {
        edt_dia_chi.setOnClickListener(this)
        layout_take_photo.setOnClickListener(this)
        layout_open_library.setOnClickListener(this)
        btn_add_mon_an.setOnClickListener(this)
        layout_img_mon_an.setOnClickListener(this)
        btn_add_quan_an.setOnClickListener(this)
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

    fun addMonAn() {
        thucDon.ten = edt_ten_mon_an.text.toString()
        if (edt_gia_tien.text.toString() != "") {
            thucDon.gia = edt_gia_tien.text.toString().toLong()
        }
        if (thucDon.ten == "" || thucDon.hinhanh == "") {
            showAlertMessage(
                "Lỗi thực đơn",
                "Không được để trống các giá trị trong thực đơn của bạn"
            )
        } else {
            thucDonsRequest["monan$numThucDon"] = thucDon
            mAdapterMenu.addThucDon(thucDon)
            edt_gia_tien.setText("")
            edt_ten_mon_an.setText("")
            thucDon = ThucDonResponse()
            numThucDon += 1
        }
    }

    fun addQuanAnCuaToi() {
        if (edt_ten_quan_an.text.toString().trim() != "" && edt_dia_chi.text.toString().trim() != ""
            && edt_time_open.text.toString().trim() != "" && edt_time_close.text.toString().trim() != ""
            && listImagePost.size > 0 && thucDonsRequest.size > 0
        ) {
            refThucDon.setValue(thucDonsRequest).addOnSuccessListener {
                quanAn = QuanAn()
                quanAn.nguoidang = appSharedPreference.getUser().taikhoan
                quanAn.thucdon = refThucDon.key!!
                quanAn.tenquanan = edt_ten_quan_an.text.toString().trim()
                quanAn.diachi = edt_dia_chi.text.toString().trim()
                quanAn.latitude = mLatitude
                quanAn.longitude = mLongitude
                quanAn.giomocua = convertStringToTime(edt_time_open.text.toString().trim())
                quanAn.giodongcua = convertStringToTime(edt_time_close.text.toString().trim())
                quanAn.hinhanhs = listImagePost
                quanAn.ngaytao = DateUtils.getCurrentTime()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    quanAn.giaohang = switch_giao_hang.showText
                }
                if (spiner_khuvuc.selectedItemPosition == 0) {
                    quanAn.id_khuvuc = 1
                } else {
                    quanAn.id_khuvuc = 2
                }
                pushQuanAnToDataBase(quanAn)
            }.addOnFailureListener {
                showAlertMessage(
                    "Hệ thống đang xảy ra lỗi",
                    "Vui lòng thử lại"
                )
            }
        } else {
            showAlertMessage(
                "Thiếu giá trị",
                "Cần nhập đầy đủ các thông tin và hình ảnh,thực đơn cho quán ăn,Nhập lại?"
            )
        }
    }

    fun convertStringToTime(timeString: String): Long {
        val time = timeString.split(":")
        val hours = time[0].trim()
        val minutes = time[1].trim()
        val timeLong = (hours.toLong() * 60) + minutes.toLong()
        return timeLong
    }

    override fun onStop() {
        super.onStop()
    }

    fun pushQuanAnToDataBase(quanAn: QuanAn) {
        if (quanAn.id_khuvuc == 1) {
            val refQuanAn = nodeRoot.child("quanans").child("KV1").push()
            quanAn.id = refQuanAn.key!!
            refQuanAn.setValue(quanAn).addOnCompleteListener {
                showAlertListernerOneclick(
                    "Thành công",
                    "Quán ăn của bạn đã được chúng tôi ghi nhận trên hệ thống",
                    "Đóng",
                    DialogInterface.OnClickListener { p0, p1 ->
                        mActivity.popFragment()
                    })
            }.addOnFailureListener {
                showAlertMessage("Có lỗi xảy ra","Vui lòng kiểm tra lại các kết nối 3G/4G/WIFI và thử lại")
            }
        } else {
            val refQuanAn = nodeRoot.child("quanans").child("KV2").push()
            quanAn.id = refQuanAn.key!!
            refQuanAn.setValue(quanAn).addOnCompleteListener {
                showAlertListernerOneclick(
                    "Thành công",
                    "Quán ăn của bạn đã được chúng tôi ghi nhận trên hệ thống",
                    "Đóng",
                    DialogInterface.OnClickListener { p0, p1 ->
                        mActivity.popFragment()
                    })
            }.addOnFailureListener {
                showAlertMessage("Có lỗi xảy ra","Vui lòng kiểm tra lại các kết nối 3G/4G/WIFI và thử lại")
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

    private fun chooseGalleryMonAn() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(pickPhoto, PostQuanAnFragment.REQUEST_GALLERY_PHOTO_MONAN)
    }

    private fun uploadImageFileQuanAn(url: String) {
        var file = Uri.fromFile(File(url))
        var storageRef: StorageReference = storage.child("monan/${file.lastPathSegment}")
        var uploadTask = storageRef.putFile(file)

        uploadTask.addOnFailureListener {
            showAlertMessage("Có lỗi", "Tải ảnh quán ăn lên hệ thống thất bại,vui lòng thử lại")
        }.addOnSuccessListener {
            listImagePost["hinhanh$numHinhAnhQuanAn"] = file.lastPathSegment
            numHinhAnhQuanAn += 1
            mAdapterImages.setImagePost(url)
        }
    }

    private fun uploadImageFileThucDon(url: String) {
        var file = Uri.fromFile(File(url))
        var storageRef: StorageReference = storage.child("monan/${file.lastPathSegment}")
        var uploadTask = storageRef.putFile(file)

        uploadTask.addOnFailureListener {
            btn_add_mon_an.isEnabled = true
            showAlertMessage("Có lỗi", "Tải ảnh món ăn lên hệ thống thất bại,vui lòng thử lại")
        }.addOnSuccessListener {
            btn_add_mon_an.isEnabled = true
            thucDon.hinhanh = url
            glideLoadImage(img_mon_an, url)
        }
    }

    private fun glideLoadImage(img: ImageView, url: String) {
        GlideApp.with(activityContext)
            .load(url)
            .error(R.drawable.placeholder)
            .thumbnail(0.1f)
            .placeholder(R.drawable.placeholder)
            .into(img)
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

}