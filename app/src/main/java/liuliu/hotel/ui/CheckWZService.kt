package liuliu.hotel.ui

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder

class CheckWZService : Service() {
    internal var handler = Handler()

    override fun onCreate() {
        super.onCreate()

        val runnable = Runnable {
            //handler.removeCallbacks(this); //移除定时任务
        }
        handler.postDelayed(runnable, 50)// 打开定时器，50ms后执行runnable
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
