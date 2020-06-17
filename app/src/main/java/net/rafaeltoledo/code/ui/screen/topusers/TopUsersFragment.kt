package net.rafaeltoledo.code.ui.screen.topusers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import dagger.hilt.android.AndroidEntryPoint
import net.rafaeltoledo.code.R
import net.rafaeltoledo.code.databinding.FragmentTopUsersBinding

@AndroidEntryPoint
class TopUsersFragment : Fragment(R.layout.fragment_top_users) {

    private val viewModel: TopUsersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentTopUsersBinding.bind(view)

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.content.displayedChild = 0
        }

        viewModel.result.observe(viewLifecycleOwner) {
            binding.content.displayedChild = 1
            binding.recyclerView.adapter = TopUsersAdapter(it)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            binding.content.displayedChild = 2
        }

        binding.buttonTryAgain.setOnClickListener { fetch() }

        fetch()
    }

    private fun fetch() {
        viewModel.fetchUsers()
    }
}