package com.example.news.presentation

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.news.R
import com.example.news.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val databaseViewModel: DatabaseViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseViewModel.renewDatabase()
        val toolBar = binding.topAppbar
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.hide()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .build()
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.setOnItemReselectedListener {
            when (navController.currentDestination!!.id) {
                R.id.searchFragment ->
                    navController.navigate(R.id.searchPrepFragment, null, navOptions)
                else ->
                    navController.navigate(it.itemId, null, navOptions)
            }
        }
        binding.topAppbar.setupWithNavController(
            navController,
            AppBarConfiguration(
                setOf(R.id.headlinesFragment, R.id.libraryFragment)
            )
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.headlinesFragment -> {
                    toolBar.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
                R.id.detailFragment -> {
                    toolBar.visibility = View.VISIBLE
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.searchFragment -> {
                    toolBar.visibility = View.GONE
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
                else -> {
                    toolBar.visibility = View.VISIBLE
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.popBackStack()
    }

    override fun getTheme(): Resources.Theme {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        return super.getTheme()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (Intent.ACTION_VIEW == intent?.action) {
            val uri = intent.data
            if (uri != null) {
                val url = uri.toString()
                // Process the URL as needed
                Toast.makeText(this, "Received URL: $url", Toast.LENGTH_SHORT).show()
            }
        }
    }

//        NavigationUI.setupActionBarWithNavController(
//            this,
//            navController,
//            AppBarConfiguration(navController.graph)
//        )

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main_menu, menu)
//        val searchItem = menu?.findItem(R.id.action_search)
//        val searchView = searchItem?.actionView as SearchView
//        searchView.queryHint = "Search here"
//        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//        })
//
//        searchItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
//            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
//                navController.navigate(R.id.searchFragment)
//                return true
//            }
//
//            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
//                navController.popBackStack()
//                return true
//            }
//        })
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            android.R.id.home -> {
//                true
//            }
//            R.id.action_search -> {
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}