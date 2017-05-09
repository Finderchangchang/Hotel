package liuliu.hotel.ui

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder

class CheckWZService : Service() {

    override fun onCreate() {
        super.onCreate()
        val timeInterval: Long = 1000
        val runnable = Runnable {
            while (true) {
                println("Hello !!")
                try {
                    Thread.sleep(timeInterval)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }
        val thread = Thread(runnable)
        thread.start()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        val TAG = "MyService"
    }
}
