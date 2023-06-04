package com.bangkit.githubuser.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.githubuser.R
import com.bangkit.githubuser.model.Users
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView

class UserAdapter(val userList: List<Users>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    class UserViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val avatar: ImageView = view.findViewById(R.id.user_avatar)
        val username: TextView = view.findViewById(R.id.username)
        val usercard: MaterialCardView = view.findViewById(R.id.user_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_list, parent, false)
        return UserViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(userList[position].avatarUrl)
            .into(holder.avatar)
        val username = userList[position].login
        holder.username.text = username
        holder.usercard.setOnClickListener {
            val action: NavDirections = HomeFragmentDirections.actionHomeFragmentToDetailFragment(username!!)
            holder.view.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

}