package it.step.moviecatalog

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import java.util.Locale


class MainActivity : BaseActivity() {
    private lateinit var topAppBar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var navController: NavController
    private lateinit var myApp : Application

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        topAppBar = findViewById(R.id.topAppBar)
        setSupportActionBar(topAppBar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.ma_nav_graph_host) as NavHostFragment
        navController = navHostFragment.navController


        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)


        topAppBar.setNavigationOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.categoryFragment || destination.id == R.id.detailsFragment || destination.id == R.id.searchFragment || destination.id == R.id.settingsFragment || destination.id == R.id.cinemaMapFragment) {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_24)

                topAppBar.setNavigationOnClickListener {
                    navController.navigateUp()

                }
            } else {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_hamburger)
                topAppBar.setNavigationOnClickListener {
                    drawerLayout.openDrawer(navigationView)
                }
            }
        }


        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.categorie -> {
                    navController.navigate(R.id.categoryFragment)
                    drawerLayout.closeDrawers()
                    true
                }

                R.id.cerca -> {
                    navController.navigate(R.id.searchFragment)
                    drawerLayout.closeDrawers()
                    true
                }

                R.id.our_cinemas -> {
                    navController.navigate(R.id.cinemaMapFragment)
                    drawerLayout.closeDrawers()
                    true
                }

                else -> false
            }
        }

        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }


                else -> false
            }
        }

    }


    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun setAppLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Infla il menu; ciò aggiunge elementi alla barra delle azioni se presente.
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }


    fun exitAndRelaunchApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        this?.finish()
    }



}








