package com.example.weanaklie.presentation.main.home

import android.content.ComponentName
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weanaklie.R
import com.example.weanaklie.model.datamodel.SuggestResponse
import com.example.weanaklie.presentation.common.getNavFragment
import kotlinx.android.synthetic.main.activity_wakiline.*
import kotlinx.android.synthetic.main.navigation_view.*


class HomeWakilneActivity : AppCompatActivity() {
    var suggest: SuggestResponse? = SuggestResponse()
    lateinit var navController: NavController
    var userlocation: Location? = null
    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.detailFragment,
                R.id.aboutFragment,
                R.id.settingsFragment
            ),
            drawer_layout
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wakiline)
        suggest = intent.getParcelableExtra("suggest") ?: SuggestResponse()
        userlocation = intent.getParcelableExtra("location")
        setupNavigation()

    }


    override fun onResume() {
        super.onResume()
        toolbar.title = ""
        searchDrawerLayout.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)

        }
        aboutDrawerLayout.setOnClickListener {
            //this for not navigate to about fragment twice
            val fragment = getCurrentFragmentOrNull()
            fragment?.let { nowFragmentit1 ->
                try {
                    when (nowFragmentit1) {
                        is AboutFragment -> {

                        }
                        else ->{
                            navController.navigate(R.id.aboutFragment)

                        }

                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            drawer_layout.closeDrawer(GravityCompat.START)




        }

    }

    override fun onRestart() {
        super.onRestart()
    }

    private fun setupNavigation() {
        findNavController(R.id.nav_home_w)
            .setGraph(
                R.navigation.navigation_home_wakilnie,
                DetailFragmentArgs(suggest, userlocation).toBundle()
            )
        navController = findNavController(R.id.nav_home_w)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        toolbar.setupWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)


    }

    override fun onStart() {
        super.onStart()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.aboutFragment -> {


                }
                else -> {


                }


            }

        }

    }
    private fun getCurrentFragmentOrNull(): Fragment? {
        return (this as FragmentActivity).getNavFragment(R.id.nav_home_w)

    }
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_home_w).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}
