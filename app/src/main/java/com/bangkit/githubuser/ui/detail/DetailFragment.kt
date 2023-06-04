package com.bangkit.githubuser.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.githubuser.R
import com.bangkit.githubuser.database.UserApplication
import com.bangkit.githubuser.databinding.FragmentDetailBinding
import com.bangkit.githubuser.model.FavoriteUser
import com.bangkit.githubuser.model.UserDetail
import com.bangkit.githubuser.ui.favorite.UserFavViewModel
import com.bangkit.githubuser.ui.favorite.UserFavViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment() {
    companion object{
        const val USERNAME = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )

        private var status = true
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var username: String
    private var user : UserDetail? = null
    private val userDetailViewModel : UserDetailViewModel by viewModels()
    private val favoriteViewModel : UserFavViewModel by activityViewModels {
        UserFavViewModelFactory(
            (activity?.application as UserApplication).database.userDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(USERNAME).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        userDetailViewModel.getUserDetails(username)
        userDetailViewModel.status.observe(viewLifecycleOwner) { status : UserDetailApiStatus ->
            when(status) {
                UserDetailApiStatus.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.errorText.visibility = View.GONE
                }
                UserDetailApiStatus.DONE -> {
                    binding.progressBar.visibility = View.GONE
                    userDetailViewModel.user.observe(viewLifecycleOwner) { user ->
                        if (user == UserDetail()) {
                            binding.errorText.apply {
                                visibility = View.VISIBLE
                                text = context.getString(R.string.empty_user_detail)
                            }
                        } else {
                            this.user = user
                            binding.errorText.visibility = View.GONE
                            Glide.with(requireContext())
                                .load(user.avatarUrl)
                                .into(binding.userAvatar)
                            binding.name.text = user.name
                            binding.username.text = user.login
                            binding.userCompany.text = user.company
                            binding.follow.text = String.format(
                                resources.getString(R.string.follow),
                                user.followers,
                                user.following
                            )
                            favoriteViewModel.allUsersFav.observe(this.viewLifecycleOwner) {
                                Companion.status = !it.contains(
                                    FavoriteUser(user.login!!, user?.avatarUrl)
                                )
                            }
                            if(Companion.status){
                                binding.favoriteFab.setImageResource(R.drawable.ic_fav_nofill)
                            } else {
                                binding.favoriteFab.setImageResource(R.drawable.ic_favorite)
                            }
                        }
                    }
                }
                UserDetailApiStatus.ERROR -> {
                    binding.errorText.apply {
                        visibility = View.VISIBLE
                        text = context.getString(R.string.user_fail)
                    }
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val detailPagerAdapter = DetailPagerAdapter(this)
        val viewPager : ViewPager2 = view.findViewById(R.id.view_pager)
        viewPager.adapter = detailPagerAdapter
        detailPagerAdapter.username = username
        val tabs: TabLayout = view.findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        binding.favoriteFab.setOnClickListener {
            val mUser = FavoriteUser(
                username = user?.login!!,
                avatarUrl = user?.avatarUrl
            )
            favoriteViewModel.allUsersFav.observe(this.viewLifecycleOwner) {
                status = !it.contains(mUser)
            }
            if(status){
                favoriteViewModel.insert(mUser)
                binding.favoriteFab.setImageResource(R.drawable.ic_favorite)
                Toast.makeText(context, "${user?.name} added to favorite", Toast.LENGTH_SHORT).show()
            } else {
                favoriteViewModel.delete(mUser)
                binding.favoriteFab.setImageResource(R.drawable.ic_fav_nofill)
                Toast.makeText(context, "${user?.name} deleted from favorite", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}