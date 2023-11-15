package com.carlos_alberto.firebasemvvm.helper

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Permissions {

    companion object {
        const val REQUEST_PERMISSIONS_CODE = 100 // Change this to your desired request code
    }

    fun validatePermissions(
        permissions: Array<String>,
        activity: Activity?,
        requestCode: Int = REQUEST_PERMISSIONS_CODE
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionsToRequest = permissions.filter {
                ContextCompat.checkSelfPermission(activity!!, it) != PackageManager.PERMISSION_GRANTED
            }

            if (permissionsToRequest.isEmpty()) {
                return true
            }

            ActivityCompat.requestPermissions(
                activity!!,
                permissionsToRequest.toTypedArray(),
                requestCode
            )
        }
        return true
    }
}