package com.mazzampr.githubsearch.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mazzampr.githubsearch.R
import com.mazzampr.githubsearch.adapter.SearchUserAdapter
import com.mazzampr.githubsearch.data.remote.response.UserResponse
import com.mazzampr.githubsearch.databinding.FragmentHomeBinding
import com.mazzampr.githubsearch.utils.hide
import com.mazzampr.githubsearch.utils.show
import com.mazzampr.githubsearch.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var searchUserAdapter: SearchUserAdapter

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchUserAdapter = SearchUserAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRvUserList()
        setUpSearchBar()

        homeViewModel.getUserList(INIT_QUERY)
        observerIsLoading()
        observeUserListLiveData()
        onUsersItemClicked()


        binding.topAppBar.setOnMenuItemClickListener {menuItem ->
            when(menuItem.itemId) {
                R.id.btnLoveList -> {
                    val action = HomeFragmentDirections.actionHomeFragmentToFavoriteFragment()
                    findNavController().navigate(action)
                    true
                    }
                R.id.btnSetting -> {
                    val action = HomeFragmentDirections.actionHomeFragmentToSettingFragment()
                    findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUIResultCount(count: Int) {
        binding.tvCountResult.text = count.toString()
    }

    private fun setUpRvUserList() {
        binding.rvUserGithub.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = searchUserAdapter
        }
    }

    private fun onUsersItemClicked() {
        searchUserAdapter.onItemClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(it.login)
            findNavController().navigate(action)
        }
    }

    private fun observerIsLoading() {
        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showShimmerLoading(it)
        }
    }

    private fun observeUserListLiveData() {
        homeViewModel.userList.observe(viewLifecycleOwner
        ) {userList ->
            if (userList?.size != 0) {
                searchUserAdapter.setUsers(_userList = userList as ArrayList<UserResponse>)
                binding.rvUserGithub.visibility = View.VISIBLE
                binding.tvUsernameNotFound.visibility = View.GONE
                setUIResultCount(userList.size)
            } else {
                binding.tvUsernameNotFound.visibility = View.VISIBLE
                binding.rvUserGithub.visibility = View.GONE
                setUIResultCount(0)
            }

        }
    }

    private fun setUpSearchBar() {
        binding.apply {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchView.hide()
                homeViewModel.getUserList(searchView.text.toString())
                false
            }
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

    companion object{
        private var INIT_QUERY = "maze"
    }

}