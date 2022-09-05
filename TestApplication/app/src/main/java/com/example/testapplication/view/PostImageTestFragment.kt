package com.example.testapplication.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.testapplication.RotateTransform
import com.example.testapplication.databinding.FragmentPostImageTestBinding
import com.example.testapplication.viewModel.PostImageTestViewModel
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import java.io.File
import java.io.IOException
import java.util.*

class PostImageTestFragment:Fragment(),View.OnClickListener{


    private var _binding:FragmentPostImageTestBinding?=null
    private val binding:FragmentPostImageTestBinding
        get()=_binding!!

    private var photoUri: Uri?=null
    private val viewModel:PostImageTestViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding=FragmentPostImageTestBinding.inflate(layoutInflater,container,false)


        binding.fragmentPostImageTestButton.setOnClickListener(this)
        binding.fragmentPostImageTestTakePictureButton.setOnClickListener(this)
        binding.fragmentPostImageTestGallery.setOnClickListener(this)


        //서버에서 이미지 가져오면 imageView에 뿌린다.
        viewModel.imageUri.observe(viewLifecycleOwner){

            Glide.with(requireContext())
                .load(it)
                //.apply(options)
                .into(binding.fragmentPostImageTestResultImage)

        }

        viewModel.photoUri.observe(viewLifecycleOwner){
            Glide.with(requireContext())
                .load(it)
                .into(binding.fragmentPostImageTestOriginImage)
        }



        return binding.root
    }

    override fun onClick(view: View?) {
        when(view){
            binding.fragmentPostImageTestButton->{

                //image post
                Log.d("testing","post image")
                viewModel.photoUri.value?.let {
                    viewModel.postImage(it)
                }

            }

            binding.fragmentPostImageTestTakePictureButton->{
                launcher.launch(Manifest.permission.CAMERA)

            }

            binding.fragmentPostImageTestGallery->{
                startMultiPicker()
            }
        }
    }

    private val getResultFromCamera=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult->
        if(activityResult.resultCode== Activity.RESULT_OK){
            activityResult.data?.let{ intent->

                viewModel.photoUri.value?.let {
                    val imageBitmap=ImageDecoder.createSource(requireContext().contentResolver,it)
                    binding.fragmentPostImageTestOriginImage.setImageBitmap(ImageDecoder.decodeBitmap(imageBitmap))
                }

            }?:run{
                Log.d("MultiPickerFragment","activity Result data is null")
            }
        }else if(activityResult.resultCode== Activity.RESULT_CANCELED){
            Log.d("MultiPickerFragment","activity request code is RESULT_CANCELED")
        }
    }

    private val getResultFromFishBun=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult->
        if(activityResult.resultCode== Activity.RESULT_OK){
            activityResult.data?.let{intent->
                val imageUris=intent.getParcelableArrayListExtra<Uri>(FishBun.INTENT_PATH)
                if (imageUris != null) {
                    Log.d("testing","image uris path is ${imageUris[0].path}")

                    viewModel.setPhotoFile(File(getPathFromUri(imageUris[0])))
                    viewModel.setPhotoUri(imageUris[0])

                }

            }
        }

    }

    private val launcher=registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

        val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val photoFile=File.createTempFile(newJpgFileName(),null,requireContext().cacheDir)
        viewModel.setPhotoFile(photoFile)
        viewModel.setPhotoUri(FileProvider.getUriForFile(requireContext(),"com.example.testapplication.file_provider",photoFile))
        intent.putExtra(MediaStore.EXTRA_OUTPUT,viewModel.photoUri.value)
        photoFile.deleteOnExit()
        getResultFromCamera.launch(intent)
    }



    private fun startMultiPicker(){
        this.context?.let{
            FishBun.with(this).setImageAdapter(GlideAdapter())
                .hasCameraInPickerPage(true)
                .setMaxCount(1)
                .exceptMimeType(arrayListOf(MimeType.GIF, MimeType.WEBP, MimeType.PNG))
                .setIsUseDetailView(false)
                .setIsShowCount(true)
                .startAlbumWithActivityResultCallback(getResultFromFishBun)

        }


    }



    private fun newJpgFileName():String{
        val formatter = SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.getDefault())
        val name= formatter.format(Calendar.getInstance().time)
        return "$name.jpg"
    }


    //살제 파일 path 를 가져온다.
    private fun getPathFromUri(uri: Uri):String?{
        val cursor=requireContext().contentResolver.query(uri,null,null,null,null)
        cursor?.let{
            it.moveToNext()
            val v=it.getColumnIndex(MediaStore.MediaColumns.DATA)
                if(v>=0) {
                    return cursor.getString(v)
                }
        }
        cursor?.close()
        return null
    }
}