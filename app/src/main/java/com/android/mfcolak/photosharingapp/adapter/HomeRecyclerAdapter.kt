package com.android.mfcolak.photosharingapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.mfcolak.photosharingapp.R
import com.android.mfcolak.photosharingapp.model.Post
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val postList: ArrayList<Post>) : RecyclerView.Adapter<HomeRecyclerAdapter.PostHolder>(){

    class PostHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val textView: TextView = itemView.findViewById(R.id.recyclerview_email)
        val textView2: TextView = itemView.findViewById(R.id.user_comment)
        val imageView: ImageView = itemView.findViewById(R.id.rec_row_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row, parent, false)
       // val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.recycler_row, parent, false)
        return PostHolder(itemView)
    }
    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {


        holder.textView.text = postList[position].userEmail
        holder.textView2.text= postList[position].userComment
        Picasso.get().load(postList[position].imgUrl).into(holder.imageView)

    }

}