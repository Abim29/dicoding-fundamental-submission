package com.bangkit.githubuser.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailPagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment){
    var username: String = ""
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.POSITION, position + 1)
            putString(FollowFragment.USERNAME, username)
        }
        return fragment
    }
}