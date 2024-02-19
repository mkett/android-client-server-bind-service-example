package com.example.server.provides.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.Random

class RandomNumberService : Service() {


    private val binder: IAidlRandomNumber.Stub = object : IAidlRandomNumber.Stub() {
        override fun getRandomNumber(): Int {
            return mGenerator.nextInt(100)
        }
    }

    private val mGenerator = Random()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

}
