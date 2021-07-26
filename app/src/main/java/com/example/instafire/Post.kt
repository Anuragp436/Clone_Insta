package com.example.instafire

import com.google.firebase.firestore.PropertyName

data class Post(
                var creation_time:Long=0,
                var description:String="",

                var image_url:String ="",
                var user:User?=null) {
}