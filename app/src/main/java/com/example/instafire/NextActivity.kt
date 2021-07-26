package com.example.instafire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instafire.databinding.ActivityNextBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source

private const val EXTRA_USERNAME="USERNNAME"
open class NextActivity : AppCompatActivity() {

      var signedUser: User?=null
    private lateinit var firestormDB: FirebaseFirestore
  private var post = ArrayList<Post>()
    private lateinit var binding: ActivityNextBinding
private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNextBinding.inflate(layoutInflater)
        setContentView(binding.root)


         adapter = PostAdapter(this,post)
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter


        firestormDB = FirebaseFirestore.getInstance()

       firestormDB.collection("user").
       document(FirebaseAuth.getInstance().currentUser?.uid as String).get().addOnSuccessListener {
           snapshot->
           signedUser=snapshot.toObject(User::class.java)
           Log.d("signeduser","$signedUser")
       }

        var postReference = firestormDB.collection("post").limit(20)
            .orderBy("creation_time", Query.Direction.DESCENDING)
            .orderBy("description",Query.Direction.ASCENDING)



val username=intent.getStringExtra(EXTRA_USERNAME)
        if(username!=null)
        {
            postReference=postReference.
            whereEqualTo("user.username",username)
            Log.d("username",username)

        }





       postReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {

                Toast.makeText(this, "Toast", Toast.LENGTH_LONG).show()
                Log.d("lets check exception","$exception $$snapshot")
                return@addSnapshotListener

            }
            val postList = snapshot.toObjects(Post::class.java)
           Log.d("postlist","$postList $snapshot")
           for(po in postList)
           {
               Log.d("ans","$po")
           }
            post.clear()
            post.addAll(postList)
           Log.d("acd","$post")
            adapter.notifyDataSetChanged()
        }

binding.floatingActionButton.setOnClickListener {
    startActivity(Intent(this@NextActivity,SubMission::class.java))
}

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_post, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_profile) {
            profileActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun profileActivity() {

        val intent = Intent(this@NextActivity, ProfileActivity::class.java)
        intent.putExtra(EXTRA_USERNAME,signedUser?.username)

        startActivity(intent)
    }
}
