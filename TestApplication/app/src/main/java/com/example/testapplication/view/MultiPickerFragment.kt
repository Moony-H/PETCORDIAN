package com.example.testapplication.view

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.testapplication.TestApplication
import com.example.testapplication.Utils
import com.example.testapplication.databinding.FragmentMultiPickerBinding
import com.example.testapplication.databinding.SourceBottomSheetDialogBinding
import com.example.testapplication.viewModel.MultiPickerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter


class MultiPickerFragment: BasePermissionFragment(),View.OnClickListener {

    private var _binding: FragmentMultiPickerBinding?=null
    private val binding:FragmentMultiPickerBinding
        get()=_binding!!

    private var _bottomSheetBinding:SourceBottomSheetDialogBinding?=null
    private val bottomSheetDialogBinding:SourceBottomSheetDialogBinding
        get()=_bottomSheetBinding!!


    private lateinit var bottomSheetDialog:BottomSheetDialog
    private val viewModel:MultiPickerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //뷰 바인딩
        _binding= FragmentMultiPickerBinding.inflate(layoutInflater,container,false)

        _bottomSheetBinding= SourceBottomSheetDialogBinding.inflate(layoutInflater,container,false)


        binding.fragmentMultiImageSelectButton.setOnClickListener(this)

        bottomSheetDialogBinding.sourceBottomDialogGetPictureFromCamera.setOnClickListener(this)
        bottomSheetDialogBinding.sourceBottomDialogGetPictureFromAlbum.setOnClickListener(this)


        bottomSheetDialog= BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetDialogBinding.root)


        viewModel.image.observe(viewLifecycleOwner){
            binding.fragmentMultiImageLinear.removeAllViews()
            it.forEach {bitmap->
                addImageView(bitmap)
            }
        }




        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
        _bottomSheetBinding=null
    }

    override fun onClick(view: View) {
        when(view){
            binding.fragmentMultiImageSelectButton->{
                bottomSheetDialog.show()
            }

            bottomSheetDialogBinding.sourceBottomDialogGetPictureFromAlbum->{
                Log.d("MultiPickerFragment","get picture from album button clicked")
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                dismissDialog()
            }

            bottomSheetDialogBinding.sourceBottomDialogGetPictureFromCamera->{
                Log.d("MultiPickerFragment","get picture from camera button clicked")
                requestPermission(Manifest.permission.CAMERA)
                dismissDialog()
            }

        }
    }


    private val getResultFromCamera=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult->
        if(activityResult.resultCode==RESULT_OK){
            activityResult.data?.let{ intent->
                val bitmap=intent.extras?.get("data") as Bitmap
                viewModel.setSelectedImagesWithBitmap(listOf(bitmap))
            }?:run{
                Log.d("MultiPickerFragment","activity Result data is null")
            }
        }else if(activityResult.resultCode== RESULT_CANCELED){
            Log.d("MultiPickerFragment","activity request code is RESULT_CANCELED")
        }
    }

    private val getResultFromFishBun=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult->
        if(activityResult.resultCode== RESULT_OK){
            activityResult.data?.let{intent->
                val imageUris=intent.getParcelableArrayListExtra<Uri>(FishBun.INTENT_PATH)
                if (imageUris != null) {

                    viewModel.setSelectedImagesWithUri(imageUris)
                }

            }
        }

    }



    private fun startMultiPicker(){
        this.context?.let{
            val fishBunCreator=FishBun.with(this).setImageAdapter(GlideAdapter())

                .hasCameraInPickerPage(true)
                .setMaxCount(3)
                .exceptMimeType(arrayListOf(MimeType.GIF,MimeType.WEBP,MimeType.PNG))
                .setIsUseDetailView(false)
                .setIsShowCount(true)

            viewModel.imageUri.value?.let { it1 ->
                fishBunCreator
                    .setSelectedImages(it1)
                    .startAlbumWithActivityResultCallback(getResultFromFishBun)
            }?:run {
                fishBunCreator
                    .startAlbumWithActivityResultCallback(getResultFromFishBun)
            }
        }


    }

    private fun addImageView(bitmap: Bitmap){
        val linearLayout=binding.fragmentMultiImageLinear
        val imageView=ImageView(context)
        imageView.setImageBitmap(bitmap)
        imageView.layoutParams= ViewGroup.LayoutParams(linearLayout.width-100,linearLayout.width-100)
        binding.fragmentMultiImageLinear.addView(imageView)
    }

    private fun openAlertDialog(permission:String) {
        Utils.openAlertDialog(
            requireContext(),
            "권한 알림",
            "권한이 거부되었습니다.\n권한 설정 페이지로 이동하여 $permission 권한을 설정해 주세요.",
            "go",
            "cancel",

            ) { _, _ ->
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + TestApplication.getAppContext().packageName)
            )
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            Log.d("MultiPickerFragment", "go permission check page")
        }

    }

    private fun dismissDialog(){
        bottomSheetDialog.dismiss()
    }

    override fun onPermissionResult(permission:String,isGranted: Boolean) {
        when(permission){
            Manifest.permission.CAMERA->{
                if(isGranted){
                    //카메라 실행
                    Log.d("MultiPickerFragment","camera permission granted")
                    val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    getResultFromCamera.launch(intent)
                }else{
                    Toast.makeText(context,"permission denied",Toast.LENGTH_SHORT).show()
                    Log.d("testing","open dialog")
                    openAlertDialog("카메라")

                }
            }

            Manifest.permission.READ_EXTERNAL_STORAGE->{
                if(isGranted){
                    Log.d("MultiPickerFragment","storage permission granted")
                    startMultiPicker()
                }
                else{
                    Toast.makeText(context,"permission denied",Toast.LENGTH_SHORT).show()
                    openAlertDialog("저장 공간")
                }
            }

        }

    }







}