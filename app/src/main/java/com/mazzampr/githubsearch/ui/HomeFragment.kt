package com.mazzampr.githubsearch.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mazzampr.githubsearch.R
import com.mazzampr.githubsearch.adapter.SearchUserAdapter
import com.mazzampr.githubsearch.data.remote.response.UserResponse
import com.mazzampr.githubsearch.databinding.FragmentHomeBinding
import com.mazzampr.githubsearch.util.hide
import com.mazzampr.githubsearch.util.show
import com.mazzampr.githubsearch.util.toast
import com.mazzampr.githubsearch.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var searchUserAdapter: SearchUserAdapter

    private val binding get() = _binding!!

    companion object{
        private var INIT_QUERY = "maze"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchUserAdapter = SearchUserAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
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
                    toast(getString(R.string.fitur_develop_info))
                    true
                    }
                R.id.btnSetting -> {
                    toast(getString(R.string.fitur_develop_info))
                    true
                }
                else -> false
            }
        }

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
            searchView.editText.setOnEditorActionListener { text, actionId, event ->
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

}