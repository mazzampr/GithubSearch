package com.mazzampr.githubsearch.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.mazzampr.githubsearch.R
import com.mazzampr.githubsearch.adapter.FollowViewPagerAdapter
import com.mazzampr.githubsearch.adapter.SearchUserAdapter
import com.mazzampr.githubsearch.databinding.FragmentDetailBinding
import com.mazzampr.githubsearch.util.hide
import com.mazzampr.githubsearch.util.show
import com.mazzampr.githubsearch.viewmodel.DetailViewModel
import kotlinx.coroutines.launch


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null

    private val binding get() = _binding!!
    private lateinit var searchListAdapter: SearchUserAdapter
    private val detailViewModel by viewModels<DetailViewModel>()

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
        detailViewModel.viewModelScope.launch { detailViewModel.getDetailUser(user!!) }
    }

    @SuppressLint("SetTextI18n")
    private fun observeData() {
        detailViewModel.isLoading.observe(viewLifecycleOwner) {isLoading->
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
        detailViewModel.user.observe(viewLifecycleOwner) {user ->
            Glide.with(requireContext())
                .load(user.avatarUrl)
                .into(binding.ivUserProfile)
            binding.apply {
                tvName.text = user.name
                tvUsername.text = user.login
                tvTotalFollowers.text = "${user.followers} Followers"
                tvTotalFollowing.text = "${user.following} Following"
            }
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
}