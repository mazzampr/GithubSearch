package com.mazzampr.githubsearch.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mazzampr.githubsearch.adapter.SearchUserAdapter
import com.mazzampr.githubsearch.data.remote.response.UserResponse
import com.mazzampr.githubsearch.databinding.FragmentFollowBinding
import com.mazzampr.githubsearch.utils.hide
import com.mazzampr.githubsearch.utils.show
import com.mazzampr.githubsearch.viewmodel.FollowViewModel

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private val followViewModel by viewModels<FollowViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val index = arguments?.getInt(ARG_INDEX, 0)
        val user = arguments?.getString(ARG_BUNDLE)

        setUpRecyclerView()

        if(index == 0){
            user?.let { followViewModel.getFollowers(it) }
        }else{
            user?.let { followViewModel.getFollowing(it) }
        }

        observeData()
    }

    companion object {
        private const val ARG_INDEX = "index"
        private const val ARG_BUNDLE = "data"

        @JvmStatic
        fun newInstance(index: Int, user: String?) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_INDEX, index)
                    putString(ARG_BUNDLE, user)
                }
            }
    }

    private fun observeData() {
        followViewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
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

        followViewModel.userFollowing.observe(viewLifecycleOwner) {
            if (it?.isEmpty()!!) {
                binding.tvDataNull.show()
                return@observe
            }
            setUpRvUserFollow(it)
        }
        followViewModel.userFollowers.observe(viewLifecycleOwner) {
            if (it?.isEmpty()!!) {
                binding.tvDataNull.show()
                return@observe
            }
            setUpRvUserFollow(it)
        }

    }
    private fun setUpRvUserFollow(responseItem: List<UserResponse?>?) {
        binding.tvDataNull.hide()
        val adapter = SearchUserAdapter()
        binding.rvUserFollow.adapter = adapter 
        adapter.setUsers(responseItem as ArrayList<UserResponse>)
        adapter.onItemClick = {
            val action = DetailFragmentDirections.actionDetailFragmentSelf(it.login)
            findNavController().navigate(action)
        }
    }

    private fun setUpRecyclerView() {
        binding.rvUserFollow.apply {
            val rvLayoutManager = LinearLayoutManager(requireContext())
            layoutManager = rvLayoutManager
            isNestedScrollingEnabled = true
        }
    }

}