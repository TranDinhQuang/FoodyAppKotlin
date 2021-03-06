package com.example.reviewfoodkotlin.common

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.reviewfoodkotlin.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import javax.inject.Named


abstract class BaseFragment : Fragment(), HasSupportFragmentInjector {

    @Inject
    @Named(BaseActivityModule.ACTIVITY_CONTEXT)
    protected lateinit var activityContext: AppCompatActivity

    @Inject
    protected lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>


    override fun onAttach(activity: Activity?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            AndroidSupportInjection.inject(this)
        }
        super.onAttach(activity)
    }

    override fun onAttach(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndroidSupportInjection.inject(this)
        }
        super.onAttach(context)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return childFragmentInjector
    }


    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun showAlertListerner(
        title: String,
        message: String,
        listerner: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(activityContext)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Đồng ý", listerner).setNegativeButton("Hủy bỏ", null).show()
    }

    fun showAlertListernerOneclick(
        title: String,
        message: String,
        txtButton: String,
        listerner: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(activityContext)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(txtButton, listerner).show()
    }


    fun showAlertMessage(title: String, message: String) {
        AlertDialog.Builder(activityContext)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton("Đồng ý", null).show()
    }


    fun showPermissionDialog() {
        Dexter.withActivity(activityContext).withPermissions().withListener(
            object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report != null) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }
        ).withErrorListener { showSettingsDialog() }
            .onSameThread()
            .check()
    }


    fun showSettingsDialog() {
        val builder = AlertDialog.Builder(activityContext)
        builder.setTitle(getString(R.string.message_need_permission))
        builder.setMessage(getString(R.string.message_grant_permission))
        builder.setPositiveButton(getString(R.string.label_setting)) { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activityContext.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }
}

