package net.rafaeltoledo.code.ui.screen.topusers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.rafaeltoledo.code.api.User
import net.rafaeltoledo.code.databinding.ItemTopUserBinding

class TopUsersAdapter(private val users: List<User>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemTopUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }
}

class ViewHolder(private val binding: ItemTopUserBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {
        binding.name.text = user.displayName
        binding.location.text = user.location
    }
}