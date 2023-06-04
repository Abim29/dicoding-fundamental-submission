package com.bangkit.githubuser.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.bangkit.githubuser.database.UserApplication
import com.bangkit.githubuser.databinding.FragmentFavoriteBinding


class FavoriteFragment : Fragment() {
    private var _binding : FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel : UserFavViewModel by viewModels {
        UserFavViewModelFactory(
            (activity?.application as UserApplication).database.userDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FavoriteAdapter{
            val action: NavDirections = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(it.username)
            view.findNavController().navigate(action)
        }
        binding.favRecycleView.adapter = adapter
        favoriteViewModel.allUsersFav.observe(this.viewLifecycleOwner) {
            items -> items.let { adapter.submitList(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}