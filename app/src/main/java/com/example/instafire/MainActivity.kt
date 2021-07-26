package com.example.instafire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.instafire.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val auth=FirebaseAuth.getInstance()
       if(auth.currentUser!=null)
       {
           nextActivity()
       }
        binding.btReg.setOnClickListener {
            startActivity(Intent(this@MainActivity,RegisterActivity::class.java))
        }
         binding.btnlogin.setOnClickListener {
             binding.btnlogin.isEnabled=false

             val email=binding.emailid.text.toString()
             val password=binding.passwordid.text.toString()

             if(email.isBlank()||password.isBlank())
             {
                 Toast.makeText(this,"PLEASE ENTER EMAIL AND PASSWORD",Toast.LENGTH_LONG)
                     .show()
                 return@setOnClickListener
             }

auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
    task->
    binding.btnlogin.isEnabled=true
    if(task.isSuccessful)
    {
        Toast.makeText(this@MainActivity,"welcome here",Toast.LENGTH_LONG).show()
        nextActivity()
    }
    else
    {
        Toast.makeText(this@MainActivity,"please enter correct details",Toast.LENGTH_LONG)
            .show()
    }
}
         }

    }

    private fun nextActivity()
    {

       startActivity(Intent(this@MainActivity,NextActivity::class.java))
        finish()

    }
}