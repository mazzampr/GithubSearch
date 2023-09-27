package com.mazzampr.githubsearch.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mazzampr.githubsearch.data.local.entity.UsersEntity
import com.mazzampr.githubsearch.databinding.UserItemBinding

class FavoriteUserAdapter: RecyclerView.Adapter<FavoriteUserAdapter.FavoriteUserViewHolder>() {
    lateinit var onItemClick: ((UsersEntity) -> Unit)
    inner class FavoriteUserViewHolder(val binding: UserItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    private val diffUtil: DiffUtil.ItemCallback<UsersEntity> =
        object : DiffUtil.ItemCallback<UsersEntity>() {
            override fun areItemsTheSame(oldItem: UsersEntity, newItem: UsersEntity): Boolean {
                return oldItem.username == newItem.username
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: UsersEntity, newItem: UsersEntity): Boolean {
                return oldItem == newItem
            }
        }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteUserViewHolder {
        return FavoriteUserViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavoriteUserViewHolder, position: Int) {
        val userFav = differ.currentList[position]
        Glide.with(holder.itemView)
            .load(userFav.avatarUrl)
            .into(holder.binding.ivUserProfile)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(userFav)
        }

        holder.binding.apply {
            tvName.text = userFav.username
            tvId.text = userFav.idUser
        }
    }
}