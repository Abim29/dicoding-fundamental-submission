package com.bangkit.githubuser

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bangkit.githubuser.databinding.ActivityMainBinding
import com.bangkit.githubuser.ui.settings.SettingPreferences
import com.bangkit.githubuser.ui.settings.SettingViewModel
import com.bangkit.githubuser.ui.settings.SettingViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val bottomNavView : BottomNavigationView = binding.bottomNavView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.favoriteFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener{_, destination, _ ->
            if(destination.id == R.id.detailFragment || destination.id == R.id.settingsFragment) {
                bottomNavView.visibility = View.GONE
            } else {
                bottomNavView.visibility = View.VISIBLE
            }
        }
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[SettingViewModel::class.java]
        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp()
    }
}