package com.example.client.service

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.TextView
import com.example.server.provides.service.IAidlRandomNumber

class MainActivity : AppCompatActivity() {

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

}