package com.bangkit.githubuser.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.githubuser.databinding.UserListBinding
import com.bangkit.githubuser.model.FavoriteUser
import com.bumptech.glide.Glide

class FavoriteAdapter(private val onItemClicked : (FavoriteUser) -> Unit) : ListAdapter<FavoriteUser, FavoriteAdapter.FavoriteViewHolder>(DiffCallback) {
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<FavoriteUser>() {
            override fun areItemsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem.username == newItem.username
            }

            override fun areContentsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem == newItem
            }

        }
    }
    class FavoriteViewHolder(private var binding: UserListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: FavoriteUser) {
            binding.apply {
                Glide.with(this@FavoriteViewHolder.itemView.context)
                    .load(user.avatarUrl)
                    .into(binding.userAvatar)
                username.text = user.username
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val viewHolder = FavoriteViewHolder(
            UserListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}