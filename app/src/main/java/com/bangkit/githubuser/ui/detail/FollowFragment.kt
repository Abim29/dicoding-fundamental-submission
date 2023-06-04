package com.bangkit.githubuser.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bangkit.githubuser.R
import com.bangkit.githubuser.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    companion object{
        const val POSITION = "position"
        const val USERNAME = "username"
    }

    private var _binding : FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private val followerViewModel : FollowerViewModel by viewModels()
    private val followingViewModel : FollowingViewModel by viewModels()

    private lateinit var username: String
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(POSITION)
            username = it.getString(USERNAME).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        followerViewModel.getUserFollowers(username)
        followingViewModel.getUserFollowing(username)
        if (position == 1){
            followerViewModel.status.observe(viewLifecycleOwner) { status: FollowerApiStatus ->
                when (status) {
                    FollowerApiStatus.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorText.visibility = View.GONE
                        binding.userRecycleView.visibility = View.GONE
                    }
                    FollowerApiStatus.DONE -> {
                        binding.progressBar.visibility = View.GONE
                        binding.userRecycleView.visibility = View.VISIBLE
                        followerViewModel.userFollowers.observe(viewLifecycleOwner) { followerList ->
                            if(followerList.isEmpty()){
                                binding.errorText.apply {
                                    visibility = View.VISIBLE
                                    text = context.getString(R.string.empty_follower)
                                }
                            } else {
                                binding.errorText.visibility = View.GONE
                            }
                            binding.userRecycleView.adapter = FollowAdapter(followerList)
                        }
                    }
                    FollowerApiStatus.ERROR -> {
                        binding.errorText.apply {
                            visibility = View.VISIBLE
                            text = context.getString(R.string.data_fail)
                        }
                        binding.progressBar.visibility = View.GONE
                        binding.userRecycleView.visibility = View.GONE
                    }
                }
            }
        } else {
            followingViewModel.status.observe(viewLifecycleOwner) { status: FollowingApiStatus ->
                when (status) {
                    FollowingApiStatus.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorText.visibility = View.GONE
                        binding.userRecycleView.visibility = View.GONE
                    }
                    FollowingApiStatus.DONE -> {
                        binding.progressBar.visibility = View.GONE
                        binding.userRecycleView.visibility = View.VISIBLE
                        followingViewModel.userFollowing.observe(viewLifecycleOwner) { followingList ->
                            if(followingList.isEmpty()){
                                binding.errorText.apply {
                                    visibility = View.VISIBLE
                                    text = context.getString(R.string.empty_following)
                                }
                            } else {
                                binding.errorText.visibility = View.GONE
                            }
                            binding.userRecycleView.adapter = FollowAdapter(followingList)
                        }
                    }
                    FollowingApiStatus.ERROR -> {
                        binding.errorText.apply {
                            visibility = View.VISIBLE
                            text = context.getString(R.string.data_fail)
                        }
                        binding.progressBar.visibility = View.GONE
                        binding.userRecycleView.visibility = View.GONE
                    }
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}