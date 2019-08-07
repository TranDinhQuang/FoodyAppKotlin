package com.example.foodyappkotlin.screen.main.fragment.myself

import android.Manifest
import android.app.Activity
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
import com.example.foodyappkotlin.BuildConfig
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseFragment
import com.example.foodyappkotlin.screen.adapter.PicturePostAdapter
import com.example.foodyappkotlin.screen.detail.fragment_post.PostCommentFragment
import com.example.foodyappkotlin.screen.main.MainActivity
import com.example.foodyappkotlin.screen.maps.MapsActivity
import com.example.foodyappkotlin.view.ItemOffsetDecoration
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_add_restaurent.*
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class PostQuanAnFragment : BaseFragment(), AdapterView.OnItemSelectedListener,
    PicturePostAdapter.OnClickListener, View.OnClickListener {

    var list_of_items = arrayOf("Hà Nội", "Hồ Chí Minh")

    private lateinit var listImagePost: HashMap<String, String>
    private lateinit var mAdapterImages: PicturePostAdapter
    private lateinit var photoURI: Uri

    val ai = AtomicInteger(0)

    @Inject
    lateinit var mActivity: MainActivity

    companion object {
        val REQUEST_TAKE_PHOTO = 101
        val REQUEST_GALLERY_PHOTO = 102
        val GET_LOCATION = 103

        var permissions =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        fun newInstance(): Fragment {
            val postQuanAnFragment = PostQuanAnFragment()
            return postQuanAnFragment
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
            if (requestCode == PostCommentFragment.REQUEST_TAKE_PHOTO) {

            } else if (requestCode == PostCommentFragment.REQUEST_GALLERY_PHOTO) {
                val selectedImage = data!!.data
                val mPhotoPath = getRealPathFromUri(selectedImage)
                listImagePost.put("hinhanh$ai", mPhotoPath)
                mAdapterImages.setImagePost(mPhotoPath)
            }
            if (requestCode == GET_LOCATION) {
                Log.d("kiemtra", "get location")
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.edt_dia_chi -> {
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
                startActivityForResult(takePictureIntent, PostCommentFragment.REQUEST_TAKE_PHOTO)
            }
        }
    }


    private fun chooseGallery() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(pickPhoto, PostCommentFragment.REQUEST_GALLERY_PHOTO)
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
}