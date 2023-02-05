package com.brainstation23.contactbook.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.brainstation23.contactbook.R
import com.brainstation23.contactbook.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var start: Date? = null
    private var end: Date? = null
    private var permissionList =
        arrayOf(Manifest.permission.READ_CONTACTS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_noob, R.id.navigation_pro
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        requestPermission()
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestMultiplePermissions.launch(permissionList)
        }

    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var isAllGrand = false
            permissions.entries.forEach {
                isAllGrand = it.value
            }
            if (isAllGrand) {

            }
        }

    fun startLoading() {
        start = Calendar.getInstance().time
    }

    fun getLoadingTime(msg:String) {
        end = Calendar.getInstance().time
        Toast.makeText(
            this,
            msg+(end!!.time - start!!.time).toString() + " ms",
            Toast.LENGTH_LONG
        ).show()

    }
}