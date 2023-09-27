package com.mazzampr.githubsearch.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mazzampr.githubsearch.data.local.entity.UsersEntity
import com.mazzampr.githubsearch.data.remote.response.UserResponse
import com.mazzampr.githubsearch.databinding.UserItemBinding

class SearchUserAdapter: RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder>() {

    lateinit var onItemClick: ((UserResponse) -> Unit)
    private var userList = ArrayList<UserResponse>()

    inner class SearchUserViewHolder(val binding: UserItemBinding): RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setUsers(_userList: ArrayList<UserResponse>) {
        this.userList = _userList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        return SearchUserViewHolder(UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(userList[position].avatarUrl)
            .into(holder.binding.ivUserProfile)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(userList[position])
        }

        holder.binding.apply {
            tvName.text = userList[position].login
            tvId.text = userList[position].id.toString()
        }

    }
}