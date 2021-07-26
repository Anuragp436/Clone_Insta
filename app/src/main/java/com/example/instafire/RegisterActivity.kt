package com.example.instafire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.instafire.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseAuth
private lateinit var firestormDB: FirebaseFirestore
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
firestormDB= FirebaseFirestore.getInstance()
        firestore= FirebaseAuth.getInstance()
        binding.btRegister.setOnClickListener {
            var email=binding.etEmail.text.toString()
            var name=binding.etName.text.toString()
            var password=binding.etPassword.text.toString()
            var age=binding.etAge.text.toString().toLong()
            firestore.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task->
                    if(task.isSuccessful)
                    {
                        Toast.makeText(this@RegisterActivity,"Successfull User Creation",Toast.LENGTH_LONG).show()
                        val userHandle=User(age,name)
                        Log.d("userhandle","$userHandle")
                        firestormDB.collection("user")
                            .document(FirebaseAuth.getInstance().currentUser?.uid as String).set(userHandle)
                    }
                    else
                    {
                        Toast.makeText(this@RegisterActivity,"unSuccessfull User Creation",Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}