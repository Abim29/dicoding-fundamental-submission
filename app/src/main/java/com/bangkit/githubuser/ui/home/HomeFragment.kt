package com.bangkit.githubuser.ui.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.bangkit.githubuser.R
import com.bangkit.githubuser.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        userViewModel.status.observe(viewLifecycleOwner) { status : UserApiStatus ->
            when(status) {
                UserApiStatus.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.errorText.visibility = View.GONE
                    binding.userRecycleView.visibility = View.GONE
                }
                UserApiStatus.DONE -> {
                    binding.progressBar.visibility = View.GONE
                    binding.userRecycleView.visibility = View.VISIBLE
                    userViewModel.users.observe(viewLifecycleOwner) { users ->
                        if (users.isEmpty()){
                            binding.errorText.apply {
                                visibility = View.VISIBLE
                                text = context.getString(R.string.empty_user)
                            }

                        } else {
                            binding.errorText.visibility = View.GONE
                            binding.userRecycleView.adapter = UserAdapter(users)
                        }

                    }
                }
                UserApiStatus.ERROR -> {
                    binding.errorText.apply {
                        visibility = View.VISIBLE
                        text = context.getString(R.string.user_fail)
                    }
                    binding.progressBar.visibility = View.GONE
                    binding.userRecycleView.visibility = View.GONE
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search_user).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    userViewModel.findUsers(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.settings -> {
                view?.findNavController()?.navigate(R.id.settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}