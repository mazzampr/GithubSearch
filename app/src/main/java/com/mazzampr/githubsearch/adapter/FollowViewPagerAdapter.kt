package com.mazzampr.githubsearch.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mazzampr.githubsearch.ui.FollowFragment

class FollowViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    var model: String? = null
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return FollowFragment.newInstance(position, model)
    }

}