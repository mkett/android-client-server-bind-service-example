package com.example.client.service

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.TextView
import com.example.server.provides.service.IAidlRandomNumber

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_ASK_PERMISSIONS = 123
    private lateinit var numberService: IAidlRandomNumber
    private var serviceBounded: Boolean = false

    /** Callbacks for service binding */
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            serviceBounded = true
            numberService = IAidlRandomNumber.Stub.asInterface(service)
        }
        override fun onServiceDisconnected(arg0: ComponentName) { serviceBounded = false }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val dangPermToRequest: List<String> = checkMissingPermissions()
        if (dangPermToRequest.isNotEmpty()) {
            requestPermissions(dangPermToRequest)
            return
        }
        bindToService()
    }

    /**
     *  bind to service: create Intent with service name we want to call
     *  service name is defined in server app manifest
     */
    private fun bindToService() {
        Intent("RandomNumberService").also { intent ->
            // set package name of service
            intent.setPackage("com.example.server.provides.service")
            // Bind service through connection object asynchronously
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    override fun onResume() {
        super.onResume()

        findViewById<TextView>(R.id.btnRandomNumber).setOnClickListener {
            if (!serviceBounded) return@setOnClickListener
            // Call service for a random number
            updateUi(numberService.randomNumber.toString())
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        serviceBounded = false
    }

    private fun updateUi(value: String) {
        findViewById<TextView>(R.id.tvRandomNumber).text = value
    }

    /**
     * result after user denied or granted the permission
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            //all permissions have been granted
            if (!grantResults.contains(PackageManager.PERMISSION_DENIED)) {
                bindToService()
                return
            }
        }
    }

    /**
     * verify if user gave already permission
     */
    private fun checkMissingPermissions(): List<String> {
        val permissions: MutableList<String> = ArrayList()
        if (checkSelfPermission("com.example.permission.RANDOM_NUMBER_PERMISSION") != PackageManager.PERMISSION_GRANTED) {
            permissions.add("com.example.permission.RANDOM_NUMBER_PERMISSION")
        }
        return permissions
    }

    /**
     * request needed permission to the user
     */
    private fun requestPermissions(permissions: List<String>) {
        requestPermissions(permissions.toTypedArray<String>(), REQUEST_CODE_ASK_PERMISSIONS)
    }


}