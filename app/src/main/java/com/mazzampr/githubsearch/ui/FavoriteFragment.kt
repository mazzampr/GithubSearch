package com.mazzampr.githubsearch.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mazzampr.githubsearch.adapter.FavoriteUserAdapter
import com.mazzampr.githubsearch.data.Result
import com.mazzampr.githubsearch.data.local.entity.UsersEntity
import com.mazzampr.githubsearch.databinding.FragmentFavoriteBinding
import com.mazzampr.githubsearch.utils.hide
import com.mazzampr.githubsearch.utils.show
import com.mazzampr.githubsearch.utils.toast
import com.mazzampr.githubsearch.viewmodel.FavoriteViewModel
import com.mazzampr.githubsearch.viewmodel.ViewModelFactory

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val viewModel by viewModels<FavoriteViewModel> { ViewModelFactory.getInstance(requireActivity()) }

    private val binding get() = _binding!!
    private lateinit var adapter: FavoriteUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = FavoriteUserAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToHomeFragment()
            findNavController().navigate(action)
        }

        setUpRv()
        observe()

    }

    private fun observe() {
        viewModel.getAllFavUsers().observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    showShimmerLoading(true)
                }
                is Result.Success -> {
                    showShimmerLoading(false)
                    setUpRvFavorite(it.data)
                }
                is Result.Error -> {
                    showShimmerLoading(false)
                    toast(it.error)
                }
            }
        }
    }

    private fun setUpRvFavorite(userResponse: List<UsersEntity>) {
        adapter.differ.submitList(userResponse)
        binding.rvFavorite.adapter = adapter
        adapter.onItemClick = {
            val actionToDetail = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(it.username)
            findNavController().navigate(actionToDetail)
        }
    }
    private fun setUpRv() {
        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showShimmerLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerViewContainer.show()
            binding.shimmerViewContainer.startShimmer()
        } else {
            binding.apply {
                shimmerViewContainer.stopShimmer()
                shimmerViewContainer.hide()
            }
        }
    }

}