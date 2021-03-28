package com.example.pix.gallery

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pix.camera.CameraFragment
import com.example.pix.databinding.GalleryFragmentBinding


@Suppress("NAME_SHADOWING")
class GalleryFragment : Fragment() {

    private lateinit var binding: GalleryFragmentBinding
    private val imagesList = mutableListOf<Image>()

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = GalleryFragmentBinding.inflate(inflater)

        var allGranted = true

        for (permissions in CameraFragment.REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this.requireContext(), permissions) != PackageManager.PERMISSION_GRANTED)
                allGranted = false
        }

        if (allGranted) {
            displayGallery()
        } else {

            ActivityCompat.requestPermissions(this.requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        val adapter = GalleryAdapter(imagesList)
        binding.imagesList.adapter = adapter

        binding.openCamera.setOnClickListener {
            val navController = findNavController()
            navController.navigate(GalleryFragmentDirections.actionGalleryFragment2ToCameraFragment2())
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == REQUEST_CODE_PERMISSIONS) {

            var allGranted = true

            for (permissions in CameraFragment.REQUIRED_PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(this.requireContext(), permissions) != PackageManager.PERMISSION_GRANTED)
                    allGranted = false
            }

            if (allGranted) {
                displayGallery()
            } else {
                Toast.makeText(this.requireContext(),
                        "Permissions not granted by User",
                        Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun displayGallery() {
        val collection =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                } else {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }

        val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME
        )

        context?.contentResolver?.query(
                collection,
                projection,
                null,
                null,
                null,
                null
        ).use { cursor ->
            val idColumn = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)


            while (cursor?.moveToNext()!!) {

                val id = idColumn?.let { cursor.getLong(it) }
                val name = nameColumn?.let { cursor.getString(it) }


                val contentUri = id?.let { id ->
                    ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                    )
                }

                imagesList += Image(contentUri, name)
            }
        }
    }
}