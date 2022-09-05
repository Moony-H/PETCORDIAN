package com.example.testapplication.view

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

abstract class BasePermissionFragment:Fragment() {

    abstract fun onPermissionResult(permission:String,isGranted:Boolean)

    private val multiplePermissionLauncher=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ results->
        //onPermissionResult(results)
    }

    private var requestedPermission=""


    private val launcher=registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        Log.d("permission state","$isGranted")
        onPermissionResult(requestedPermission,isGranted)
    }

    private val launchPermission={ permission:String->
        requestedPermission=permission
        launcher.launch(permission)
    }




    fun requestPermission(permission: String){
        context?.let {
            if(ContextCompat.checkSelfPermission(it,permission)==PackageManager.PERMISSION_GRANTED)
                onPermissionResult(permission,true)
            else{
                Log.d("permission","denied")
                launchPermission(permission)
            }

        }

    }

}