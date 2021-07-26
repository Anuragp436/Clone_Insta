package com.example.instafire

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instafire.databinding.ActivitySubMissionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class SubMission : AppCompatActivity() {

    var signedUser: User?=null
    private lateinit var binding: ActivitySubMissionBinding
    var dbRef=FirebaseFirestore.getInstance()
    var storageRef=FirebaseStorage.getInstance().reference
    var imageUrl:Uri?=null
    val firebaseUser= FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySubMissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef.collection("user").
        document(FirebaseAuth.getInstance().currentUser?.uid as String).get().addOnSuccessListener {
                snapshot->
            signedUser=snapshot.toObject(User::class.java)
            Log.d("signeduser","$signedUser")
        }

        val result=registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri->
                binding.ivNewImage.setImageURI(uri)
                imageUrl=uri
                Log.d("imageUrl","$imageUrl")
            })
        Log.d("imageUri","$imageUrl")

        binding.btnGallery.setOnClickListener {
            result.launch("image/*")
        }
        binding.btnUpload.setOnClickListener {
            if(imageUrl!=null){
                val photoRef=storageRef.child("images/${System.currentTimeMillis()}-photo.jpg")
                photoRef.putFile(imageUrl!!).continueWithTask { photoUploadTask->
                    photoRef.downloadUrl
                }.continueWithTask { downloadUrlTask->
                    val post= Post(
                        System.currentTimeMillis(),
                        binding.etDescription.text.toString(),
                        downloadUrlTask.result.toString(),
                        signedUser
                    )
                    dbRef.collection("post").add(post)
                }.addOnCompleteListener { postCreationTask->
                    if(!postCreationTask.isSuccessful) {
                        Toast.makeText(this@SubMission,"${postCreationTask.exception}",Toast.LENGTH_SHORT).show()
                    }
                    val i=Intent(this@SubMission, NextActivity::class.java)
                    startActivity(i)
                }
            }
            else {
                Toast.makeText(this@SubMission,"Image cant be empty",Toast.LENGTH_SHORT).show()
            }

        }



    }
}