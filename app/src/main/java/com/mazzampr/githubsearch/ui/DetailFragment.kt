package com.mazzampr.githubsearch.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.mazzampr.githubsearch.R
import com.mazzampr.githubsearch.adapter.FollowViewPagerAdapter
import com.mazzampr.githubsearch.adapter.SearchUserAdapter
import com.mazzampr.githubsearch.data.Result
import com.mazzampr.githubsearch.data.local.entity.UsersEntity
import com.mazzampr.githubsearch.data.remote.response.UserResponse
import com.mazzampr.githubsearch.databinding.FragmentDetailBinding
import com.mazzampr.githubsearch.utils.hide
import com.mazzampr.githubsearch.utils.show
import com.mazzampr.githubsearch.utils.toast
import com.mazzampr.githubsearch.viewmodel.DetailViewModel
import com.mazzampr.githubsearch.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val viewModel by viewModels<DetailViewModel> { ViewModelFactory.getInstance(requireActivity()) }

    private val binding get() = _binding!!
    private lateinit var searchListAdapter: SearchUserAdapter

    private var isFavorite = false

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchListAdapter = SearchUserAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = DetailFragmentArgs.fromBundle(arguments as Bundle).userResponse
        prepareDetailPage(user)
        prepareViewPager(user)

        observeData()
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun prepareDetailPage(user: String?) {
        viewModel.viewModelScope.launch { viewModel.getDetailUser(user!!) }
    }

    @SuppressLint("SetTextI18n")
    private fun observeData() {
        viewModel.isLoading.observe(viewLifecycleOwner) {isLoading->
            if (isLoading) {
                binding.shimmerViewContainer.startShimmer()
                binding.viewDetailUser.visibility = View.GONE
            } else {
                binding.apply {
                    shimmerViewContainer.stopShimmer()
                    shimmerViewContainer.visibility = View.GONE
                    viewDetailUser.visibility = View.VISIBLE
                }
            }
        }
        viewModel.user.observe(viewLifecycleOwner) {user ->
            Glide.with(requireContext())
                .load(user.avatarUrl)
                .into(binding.ivUserProfile)
            binding.apply {
                tvName.text = user.name
                tvUsername.text = user.login
                tvTotalFollowers.text = "${user.followers} Followers"
                tvTotalFollowing.text = "${user.following} Following"
            }

            setFavorite(user)
        }

    }

    private fun prepareViewPager(user: String?) {
        val viewPagerAdapter = FollowViewPagerAdapter(this)
        viewPagerAdapter.model = user
        binding.viewPagerFollow.adapter = viewPagerAdapter
        val tabs = binding.tabLayout
        TabLayoutMediator(tabs, binding.viewPagerFollow) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setFavorite(user: UserResponse) {
        val userEntity = UsersEntity(user.login!!, user.id.toString(), user.avatarUrl!!)

        viewModel.getUserFav(user.login).observe(viewLifecycleOwner) {
            isFavorite = it.isNotEmpty()
            val btnFavorite = binding.btnFavorite
            if (isFavorite) {
                btnFavorite.setImageDrawable(ContextCompat.getDrawable(btnFavorite.context, R.drawable.ic_favorite_filled))
            } else {
                btnFavorite.setImageDrawable(ContextCompat.getDrawable(btnFavorite.context, R.drawable.ic_favorite_border))
            }
        }
        binding.btnFavorite.setOnClickListener {
            if (isFavorite) {
                viewModel.deleteUser(userEntity).observe(viewLifecycleOwner) {
                    when(it) {
                        is Result.Loading -> {
                            binding.btnFavorite.hide()
                            binding.progressBarFav.show()
                        }
                        is Result.Success -> {
                            binding.progressBarFav.hide()
                            toast(it.data)
                        }
                        is Result.Error -> {
                            binding.btnFavorite.show()
                            binding.progressBarFav.show()
                            toast("Error")
                        }
                    }
                }
            } else {
                viewModel.saveFavUser(userEntity).observe(viewLifecycleOwner) {
                    when(it) {
                        is Result.Loading -> {
                            binding.btnFavorite.hide()
                            binding.progressBarFav.show()
                        }
                        is Result.Success -> {
                            binding.progressBarFav.hide()
                            binding.btnFavorite.show()
                            toast(it.data)
                        }
                        is Result.Error -> {
                            binding.btnFavorite.show()
                            binding.progressBarFav.show()
                            toast("Error")
                        }
                    }
                }
            }
        }

    }
}