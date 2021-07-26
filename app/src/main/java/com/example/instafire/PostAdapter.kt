package com.example.instafire

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instafire.databinding.CycleBinding
import com.squareup.picasso.Picasso

class PostAdapter(val context: Context, var post: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.vholder>() {
    inner class vholder(val binding: CycleBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vholder {
        val binding = CycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return vholder(binding)
    }

    override fun onBindViewHolder(holder: vholder, position: Int) {
        holder.binding.tvUsername.text = post[position].user?.username

        Picasso.get()
            .load(post[position].image_url).into(holder.binding.imageDisplay)
        Log.d("log", "${post[position].image_url}")
        holder.binding.caption.text = post[position].description
        holder.binding.creationTime.text =
            DateUtils.getRelativeTimeSpanString(post[position].creation_time)

    }

    override fun getItemCount(): Int {
        return post.size
    }

}