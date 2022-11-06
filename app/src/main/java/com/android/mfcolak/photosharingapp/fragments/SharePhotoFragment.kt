package com.android.mfcolak.photosharingapp.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.android.mfcolak.photosharingapp.R
import com.android.mfcolak.photosharingapp.databinding.FragmentSharePhotoBinding
import com.android.mfcolak.photosharingapp.databinding.FragmentSignInBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.UUID

class SharePhotoFragment : Fragment() {
    private var selectedPhotoUri: Uri? = null
    private var selectedBitmap: Bitmap? =null

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSharePhotoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSharePhotoBinding.inflate(inflater, container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()


        binding.choose.setOnClickListener {
            choosePhoto(view)
        }
        binding.button.setOnClickListener {
            sharePhoto(view)
        }

    }

    private fun sharePhoto(view: View) {

        //STORAGE IMG
        //UUID -> UNIVERSAL UNIQUE ID FOR IMG NAME
        val uuid = UUID.randomUUID()
        val reference = storage.reference

        val imageReference = reference.child("images").child("${uuid}.jpg")
        if (selectedPhotoUri != null) {
            imageReference.putFile(selectedPhotoUri!!).addOnSuccessListener {
                val uploadedImgReference = FirebaseStorage.getInstance().reference.child("images").child("${uuid}.jpg")
                uploadedImgReference.downloadUrl.addOnSuccessListener { uri ->
                   val downloadUri = uri.toString()
                    val currentUserEmail = auth.currentUser!!.email.toString()
                    val userComment = binding.editTextComment.text.toString()
                    val date = Timestamp.now()

                    //DATABASE OPERATIONS

                    val hashMap = hashMapOf<String, Any>()
                    hashMap.put("imageUrl",downloadUri)
                    hashMap.put("userEmail", currentUserEmail)
                    hashMap.put("userComment", userComment)
                    hashMap.put("date", date)

                    database.collection("Post").add(hashMap).addOnCompleteListener{ task ->
                        if (task.isSuccessful){
                            navController.navigate(R.id.action_sharePhotoFragment_to_homeFragment)
                        }
                    }.addOnFailureListener {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun choosePhoto(view: View) {


        if (ContextCompat.checkSelfPermission(view.context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else{

            val storeIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(storeIntent,2)
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val storeIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(storeIntent,2)

            }
        }
        return

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data

            if (selectedPhotoUri != null){

                if (Build.VERSION.SDK_INT >= 28){
                    val source = activity?.contentResolver?.let { ImageDecoder.createSource(it, selectedPhotoUri!!) }
                    selectedBitmap = source?.let { ImageDecoder.decodeBitmap(it) }
                    binding.choose.setImageBitmap(selectedBitmap)

                }else{
                    selectedBitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedPhotoUri)
                    binding.choose.setImageBitmap(selectedBitmap)
                }

            }
        }
        return
    }

}